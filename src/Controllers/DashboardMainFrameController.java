package Controllers;

import DataStructures.CSVParser;
import Model.DatabaseManager;
import Model.TableModels.Impression;
import Model.TableType;
import Views.DashboardMainFrame;
import Model.LogType;

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
                Map<String, Double> results = this.calculateKeyMetrics(files);
                SwingUtilities.invokeLater(() -> {
                    frame.finishedLoading();
                    frame.displayMetrics(results);
                });
            }
        }).start();
    }

    public void displayMetrics(Map<String, Double> data) {
        SwingUtilities.invokeLater(() -> frame.displayMetrics(data));
    }

    public Map<String, Double> calculateKeyMetrics(Map<LogType, File> files) {
        Map<String, Double> results = new HashMap<>();

        List<Impression> impressionList = model.getAllImpressions();

        results.put("Total number of Impressions", (double) impressionList.size());
        return results;

    }
}
