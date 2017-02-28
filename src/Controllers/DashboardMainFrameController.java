package Controllers;

import DataStructures.CSVParser;
import Model.DatabaseManager;
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
            frame.displayLoading();
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
                frame.finishedLoading();
            }
        }).start();
    }

    public void displayMetrics(Map<String, Double> data) {
        SwingUtilities.invokeLater(() -> frame.displayMetrics(data));
    }

    public void returnData() {
        HashMap<String, Double> data = new HashMap<>();
        data.put("Click Through Rate (CTR)", 1.00);
        data.put("Total Campaign Cost", 1200424240.0);
        data.put("Number of Impressions", 12535360.0);

        this.displayMetrics(data);
    }
}
