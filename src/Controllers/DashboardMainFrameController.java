package Controllers;

import DataStructures.CSVParser;
import Model.DatabaseManager;
import Model.TableModels.Click;
import Model.TableModels.Impression;
import Views.DashboardMainFrame;
import Model.LogType;
import Views.MetricType;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * DashboardMainFrameController is in charge of relaying events from the GUI to the backend
 * It does this with a pool of threads so that the GUI does not lock up
 */
public class DashboardMainFrameController {
    //TODO add reference to backend CSV parser + data access
    private DashboardMainFrame frame;
    private DatabaseManager model;
    private List<Future<?>> futures;

    //TODO allow this to be set based on the device?
    private ExecutorService helpers = Executors.newFixedThreadPool(4);

    public DashboardMainFrameController(DashboardMainFrame frame, DatabaseManager model) {
        this.frame = frame;
        this.model = model;
        this.futures = new ArrayList<>();
    }

    public void processFiles(Map<LogType, File> files) {
        files.forEach((type, file) -> {
            SwingUtilities.invokeLater(frame::displayLoading);
            Future<?> f = helpers.submit(() ->
            {
                List<String[]> list = CSVParser.parseLog(file);
                model.insertData(type, list);
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

        List<Impression> impressionList = model.getAllImpressions();
        List<Click> clickList = model.getAllClicks();
        List<Double> clickCosts = clickList.stream().map(Click::getCost).collect(Collectors.toList());

        int impressionCount = MetricUtils.getImpressionCount(impressionList);
        int clickCount = clickList.size();

        results.put(MetricType.TOTAL_IMPRESSIONS, impressionCount);
        results.put(MetricType.TOTAL_COST, MetricUtils.calculateTotalCost(clickCosts));
        results.put(MetricType.CTR, MetricUtils.calculateCTR(clickCount, impressionCount));
        return results;

    }
}
