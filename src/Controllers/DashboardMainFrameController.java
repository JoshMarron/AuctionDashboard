package Controllers;

import Controllers.Queries.AttributeDataQuery;
import Controllers.Queries.TimeDataQuery;
import Controllers.Queries.TimeQueryBuilder;
import Controllers.Results.AttributeQueryResult;
import Controllers.Results.TimeQueryResult;
import Model.CorruptTableException;
import Model.DatabaseManager;
import Views.DashboardMainFrame;
import Model.DBEnums.LogType;
import Views.DashboardStartupFrame;
import Views.MetricType;
import Views.ViewPresets.AttributeType;
import org.apache.commons.io.FileUtils;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

/**
 * DashboardMainFrameController is in charge of relaying events from the GUI to the backend
 * It does this with a pool of threads so that the GUI does not lock up
 */
public class DashboardMainFrameController {
    //TODO add reference to backend CSV parser + data access
    private DashboardMainFrame frame;
    private DatabaseManager model;
    private List<LogType> availableLogs;
    private GraphCache cache;

    //TODO allow this to be set based on the device?
    private ExecutorService helpers = Executors.newFixedThreadPool(4);

    public DashboardMainFrameController(DashboardMainFrame frame, DatabaseManager model) {
        this.frame = frame;
        this.model = model;
        this.cache = new GraphCache(model);
        availableLogs = new ArrayList<>();
    }

    public void displayMainFrame(List<LogType> addedLogs) {
        availableLogs = addedLogs;
        Future<?> future = helpers.submit(() -> {
            SwingUtilities.invokeLater(() -> {
                frame.setVisible(true);
                frame.displayLoading();
            });
            Map<AttributeType, List<String>> attributeValues = model.getAllValuesOfAttributes();
            frame.setUpFilterOptions(attributeValues);
            Map<MetricType, Number> results = this.calculateKeyMetrics();
            this.displayMetrics(results);
        });
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (future.isDone()) {
            SwingUtilities.invokeLater(() -> frame.finishedLoading());
        }
    }

    public void displayMetrics(Map<MetricType, Number> data) {
        SwingUtilities.invokeLater(() -> {
            frame.displayMetrics(data);
        });
        if (availableLogs.contains(LogType.IMPRESSION)) {
            TimeDataQuery q = new TimeQueryBuilder(MetricType.TOTAL_IMPRESSIONS).build();
            requestTimeChart(q);
        } else if (availableLogs.contains(LogType.CLICK)) {
            TimeDataQuery q = new TimeQueryBuilder(MetricType.TOTAL_CLICKS).build();
            requestTimeChart(q);
        } else if (availableLogs.contains(LogType.SERVER_LOG)) {
            TimeDataQuery q = new TimeQueryBuilder(MetricType.TOTAL_BOUNCES).build();
            requestTimeChart(q);
        }
    }

    public void requestTimeChart(TimeDataQuery query) {
        helpers.submit(() -> {
            if (cache.isInCache(query)) {
                TimeQueryResult result = (TimeQueryResult) cache.hitCache(query);
                SwingUtilities.invokeLater(() -> frame.displayChart(query.getMetric(), query.getGranularity(), result.getData()));
            } else {
                TimeQueryResult result = (TimeQueryResult) model.resolveQuery(query);
                cache.addToCache(query, result);
                Map<Instant, Number> data = result.getData();
                SwingUtilities.invokeLater(() -> frame.displayChart(query.getMetric(), query.getGranularity(), data));
            }
        });
    }

    public void requestAttributeChart(AttributeDataQuery query) {
        helpers.submit(() -> {
            if (cache.isInCache(query)) {
                AttributeQueryResult result = (AttributeQueryResult) cache.hitCache(query);
                SwingUtilities.invokeLater(() -> frame.displayChart(query.getMetric(), query.getAttribute(), result.getData()));
            } else {
                AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);
                cache.addToCache(query, result);
                Map<String, Number> data = result.getData();
                SwingUtilities.invokeLater(() -> frame.displayChart(query.getMetric(), query.getAttribute(), data));
            }
        });
    }

    public void saveProject(File filename) throws IOException {
        System.out.println(filename.getAbsolutePath());
        File savedProjects = new File("data/saved.txt");
        savedProjects.getParentFile().mkdirs();
        savedProjects.createNewFile();
        File db = model.saveDB();
        FileUtils.copyFile(db.getAbsoluteFile(), filename);
        List<String> previousProjects = FileUtils.readLines(savedProjects, "utf-8");
        FileUtils.writeStringToFile(savedProjects, filename.getAbsolutePath() + '\n', "utf-8");
        FileUtils.writeLines(savedProjects, previousProjects, "\n", true);
    }

    public void closeProject() {
        frame.setVisible(false);
        DashboardStartupFrame startupFrame = new DashboardStartupFrame(frame.getHomeDir());
        DashboardStartupController startupController = new DashboardStartupController(startupFrame, model, this);
        startupFrame.setController(startupController);
        startupFrame.initStartup();
    }

    private Map<MetricType, Number> calculateKeyMetrics() {
        Map<MetricType, Number> results = new HashMap<>();

        if (availableLogs.contains(LogType.IMPRESSION)) {
            results.put(MetricType.TOTAL_IMPRESSIONS, model.getTotalImpressions());
        }
        if (availableLogs.contains(LogType.CLICK)) {
            results.put(MetricType.TOTAL_CLICKS, model.getTotalClicks());
            results.put(MetricType.TOTAL_UNIQUES, model.getTotalUniques());
        }
        if (availableLogs.contains(LogType.CLICK) && availableLogs.contains(LogType.IMPRESSION)) {
            results.put(MetricType.TOTAL_COST, model.getTotalCampaignCost());
            results.put(MetricType.CPC, model.getCPC());
            results.put(MetricType.CTR, model.getCTR());
            results.put(MetricType.CPM, model.getCPM());
        }

        if(availableLogs.contains(LogType.SERVER_LOG)){
            results.put(MetricType.TOTAL_BOUNCES, model.getTotalBounces());
            results.put(MetricType.TOTAL_CONVERSIONS, model.getTotalConversions());
        }

        if(availableLogs.contains(LogType.SERVER_LOG) && availableLogs.contains(LogType.CLICK)){
            results.put(MetricType.CPA, model.getCPA());
            results.put(MetricType.BOUNCE_RATE, model.getBounceRate());
        }


        return results;

    }

    public void addSecondCampaign(File campaign) {
        DatabaseManager secondModel = new DatabaseManager();
        try {
            secondModel.loadDB(campaign.getAbsolutePath());
        } catch (SQLException | CorruptTableException e) {
            // TODO display a warning on the frame that the second project couldn't be loaded
            e.printStackTrace();
        }

        DashboardDoubleMainFrameController newController = new DashboardDoubleMainFrameController(frame, model, secondModel);
        frame.setController(newController);

    }

    public DashboardMainFrame getFrame() {
        return frame;
    }

    public DatabaseManager getModel() {
        return model;
    }

    public GraphCache getCache() {
        return cache;
    }
}
