package Controllers;

import Model.CorruptTableException;
import Model.DBEnums.LogType;
import Model.DatabaseManager;
import Views.DashboardStartupFrame;
import org.apache.commons.io.FileUtils;
import sun.rmi.runtime.Log;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * DashboardStartupController handles interactions between the Startup View and the model. This is only used
 * when the program is initally launched.
 */
public class DashboardStartupController {

    private DashboardStartupFrame frame;
    private DatabaseManager model;
    private DashboardMainFrameController mainController;
    private List<Future<?>> runningImports;
    private ExecutorService helpers = Executors.newFixedThreadPool(4);

    public DashboardStartupController(DashboardStartupFrame frame, DatabaseManager model, DashboardMainFrameController mainController) {
        this.frame = frame;
        this.model = model;
        this.mainController = mainController;
        this.runningImports = new ArrayList<>();
    }

    public List<File> getRecentProjects() throws IOException {
        File savedFile = new File("data/saved.txt");
        if (!savedFile.exists()) {
            return new ArrayList<>();
        }

        List<String> lines = FileUtils.readLines(savedFile, "utf-8");
        List<File> files = lines.stream().map(File::new).collect(Collectors.toList());

        return files;
    }

    public void processFiles(Map<LogType, File> files, String projectName) {
        files.forEach((type, file) -> {
            SwingUtilities.invokeLater(frame::displayLoading);
            model.createDB("db/" + projectName + ".cat");
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
                });
                List<LogType> addedLogs = files.keySet().stream().collect(Collectors.toList());
                mainController.displayMainFrame(addedLogs);
            }
        }).start();
    }

    public void loadOldProject(File oldProject) {
        SwingUtilities.invokeLater(() -> {
            frame.displayLoading();
        });
        Future<?> f = helpers.submit(() -> model.loadDB(oldProject.getAbsolutePath()));
        new Thread(() -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (f.isDone()) {
                SwingUtilities.invokeLater((() -> {
                    frame.finishedLoading();
                    frame.setVisible(false);
                }));
                List<LogType> list = Arrays.asList(LogType.IMPRESSION, LogType.CLICK, LogType.SERVER_LOG);
                mainController.displayMainFrame(list);

            }
        }).start();

    }
}
