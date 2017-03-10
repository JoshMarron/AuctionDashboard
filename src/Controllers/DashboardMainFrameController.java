package Controllers;

import DataStructures.CSVParser;
import Model.DatabaseManager;
import Model.TableModels.Click;
import Model.TableModels.Impression;
import Views.DashboardMainFrame;
import Model.DBEnums.LogType;
import Views.MetricType;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * DashboardMainFrameController is in charge of relaying events from the GUI to the backend
 * It does this with a pool of threads so that the GUI does not lock up
 */
public class DashboardMainFrameController {
    //TODO add reference to backend CSV parser + data access
    private DashboardMainFrame frame;
    private DatabaseManager model;
    private CopyOnWriteArrayList<Future<?>> futures;

    //TODO allow this to be set based on the device?
    private ExecutorService helpers = Executors.newFixedThreadPool(8);

    public DashboardMainFrameController(DashboardMainFrame frame, DatabaseManager model) {
        this.frame = frame;
        this.model = model;
        this.futures = new CopyOnWriteArrayList<>();
    }

    public void processFiles(Map<LogType, File> files) {
        files.forEach((type, file) -> {
            SwingUtilities.invokeLater(frame::displayLoading);
            Future<?> f = helpers.submit(() ->
            {
                int maxLength = 0;
                try {
                    maxLength = (int) Files.lines(Paths.get(file.getPath())).count();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int step = 500000;
                int start = 1;
                model.wipeTable(type);
                while (start < maxLength) {
                    List<String[]> lines = CSVParser.parseLog(file, start, step);
                    model.insertData(type, lines);
                    start += step;
                }
            });
            futures.add(f);
        });

        new Thread(() -> {
            for (Future<?> f: futures) {
                try {
                    f.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            boolean finished = true;
            for (Future<?> f: futures) {
                finished &= f.isDone();
            }

            if (finished) {
                Map<MetricType, Number> results = this.calculateKeyMetrics(files);
                SwingUtilities.invokeLater(() -> {
                    frame.finishedLoading();
                    frame.displayMetrics(results);
                });
            }
        }).start();
    }

    public void displayMetrics(Map<MetricType, Number> data) {
        SwingUtilities.invokeLater(() -> frame.displayMetrics(data));
    }

    private Map<MetricType, Number> calculateKeyMetrics(Map<LogType, File> files) {
        Map<MetricType, Number> results = new HashMap<>();

        List<Impression> impressionList = model.selectAllImpressions();
        List<Click> clickList = model.selectAllClicks();
        List<Double> clickCosts = clickList.stream().map(Click::getCost).collect(Collectors.toList());

        if (files.containsKey(LogType.IMPRESSION)) {
            int impressionCount = MetricUtils.getImpressionCount(impressionList);
            results.put(MetricType.TOTAL_IMPRESSIONS, impressionCount);
        }
        if (files.containsKey(LogType.CLICK)) {
            results.put(MetricType.TOTAL_COST, MetricUtils.calculateTotalCost(clickCosts));
        }
        if (files.containsKey(LogType.CLICK) && files.containsKey(LogType.IMPRESSION)) {
            int clickCount = clickList.size();
            int impressionCount = MetricUtils.getImpressionCount(impressionList);
            results.put(MetricType.CTR, MetricUtils.calculateCTR(clickCount, impressionCount));
        }

        return results;

    }
}
