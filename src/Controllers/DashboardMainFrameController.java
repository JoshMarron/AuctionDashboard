package Controllers;

import Views.DashboardMainFrame;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by marro on 22/02/2017.
 */
public class DashboardMainFrameController {
    //TODO add reference to backend CSV parser + data access
    private DashboardMainFrame frame;

    //TODO allow this to be set based on the device?
    private ExecutorService helpers = Executors.newFixedThreadPool(4);

    public DashboardMainFrameController(DashboardMainFrame frame) {
        this.frame = frame;
    }

    public void processFiles(ArrayList<File> files) {
        //TODO fill out this method, should send the files to the CSV parser and report progress to a progress bar
        //helpers.submit(whatever method from the backend)
    }
}
