package Controllers;

import DataStructures.CSVParser;
import Model.DatabaseModel;
import Views.DashboardMainFrame;
import Views.LogType;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * DashboardMainFrameController is in charge of relaying events from the GUI to the backend
 * It does this with a pool of threads so that the GUI does not lock up
 */
public class DashboardMainFrameController {
    //TODO add reference to backend CSV parser + data access
    private DashboardMainFrame frame;
    private DatabaseModel model;

    //TODO allow this to be set based on the device?
    private ExecutorService helpers = Executors.newFixedThreadPool(4);

    public DashboardMainFrameController(DashboardMainFrame frame, DatabaseModel model) {
        this.frame = frame;
        this.model = model;
    }

    public void processFiles(Map<LogType, File> files) {
        files.forEach((type, file) -> {
            helpers.submit(() ->
            {
                List<String[]> list = CSVParser.parseLog(file);
                model.insertData(type, list);
            });
        });
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
