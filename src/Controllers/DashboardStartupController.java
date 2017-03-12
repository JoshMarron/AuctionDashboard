package Controllers;

import Model.DBEnums.LogType;
import Model.DatabaseManager;
import Views.DashboardStartupFrame;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * DashboardStartupController handles interactions between the Startup View and the model. This is only used
 * when the program is initally launched.
 */
public class DashboardStartupController {

    DashboardStartupFrame frame;
    DatabaseManager model;
    DashboardMainFrameController mainController;
    List<Future<?>> runningImports;
    ExecutorService helpers = Executors.newFixedThreadPool(4);

    public DashboardStartupController(DashboardStartupFrame frame, DatabaseManager model, DashboardMainFrameController mainController) {
        this.frame = frame;
        this.model = model;
        this.mainController = mainController;
        this.runningImports = new ArrayList<>();
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
            runningImports.add(f);
        });

        new Thread(() -> {
            for (Future<?> f: runningImports) {
                try {
                    f.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            boolean finished = true;
            for (Future<?> f: runningImports) {
                finished &= f.isDone();
            }

            if (finished) {
                SwingUtilities.invokeLater(() -> {
                    frame.finishedLoading();
                    frame.setVisible(false);
                    //TODO data should be passed to main frame via the main controller
                    mainController.displayMainFrame();
                });
            }
        }).start();
    }
}
