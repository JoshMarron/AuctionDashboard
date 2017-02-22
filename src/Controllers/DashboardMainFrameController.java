package Controllers;

import Views.DashboardMainFrame;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * DashboardMainFrameController is in charge of relaying events from the GUI to the backend
 * It does this with a pool of threads so that the GUI does not lock up
 */
public class DashboardMainFrameController {
    //TODO add reference to backend CSV parser + data access
    private DashboardMainFrame frame;

    //TODO allow this to be set based on the device?
    private ExecutorService helpers = Executors.newFixedThreadPool(4);

    public DashboardMainFrameController(DashboardMainFrame frame) {
        this.frame = frame;
    }

    public void processFiles(List<File> files) {
        //TODO fill out this method, should send the files to the CSV parser and report progress to a progress bar
        helpers.submit(() -> files.forEach((file) -> System.out.println(file.getName())));
    }
}
