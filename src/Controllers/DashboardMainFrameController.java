package Controllers;

import Controllers.Queries.*;
import Controllers.Results.AttributeQueryResult;
import Controllers.Results.TimeQueryResult;
import Controllers.Results.TotalQueryResult;
import Model.CorruptTableException;
import Model.DatabaseManager;
import Views.DashboardMainFrame;
import Model.DBEnums.LogType;
import Views.DashboardStartupFrame;
import Views.MetricType;
import Views.ViewPresets.AttributeType;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
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
    private DashboardMainFrame frame;
    private DatabaseManager model;
    private List<LogType> availableLogs;
    private GraphCache cache;

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
            Map<MetricType, Number> results = this.fastNoFilterKeyMetrics();
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

    private Map<MetricType, Number> fastNoFilterKeyMetrics() {
        Map<MetricType, Number> results = new HashMap<>();

        if (availableLogs.contains(LogType.IMPRESSION)) {
            results.put(MetricType.TOTAL_IMPRESSIONS, model.getTotalImpressions());
        }
        if (availableLogs.contains(LogType.CLICK)) {
            results.put(MetricType.TOTAL_CLICKS, model.getTotalClicks());
            results.put(MetricType.TOTAL_UNIQUES, model.getTotalUniques());
        }
        if (availableLogs.contains(LogType.IMPRESSION) && availableLogs.contains(LogType.CLICK)) {
            results.put(MetricType.TOTAL_COST, model.getTotalCampaignCost());
            results.put(MetricType.CPC, model.getCPC());
            results.put(MetricType.CPM, model.getCPM());
            results.put(MetricType.CTR, model.getCTR());
        }
        if (availableLogs.contains(LogType.SERVER_LOG)) {
            results.put(MetricType.TOTAL_BOUNCES, model.getTotalBounces());
            results.put(MetricType.TOTAL_CONVERSIONS, model.getTotalConversions());
        }
        if (availableLogs.contains(LogType.SERVER_LOG) && availableLogs.contains(LogType.CLICK)) {
            results.put(MetricType.CPA, model.getCPA());
            results.put(MetricType.BOUNCE_RATE, model.getBounceRate());
        }

        return results;
    }

    // This one is slow and should only be used when there are filters
    private Map<MetricType, Number> calculateKeyMetrics(TotalQuery query) {
        Map<MetricType, Number> results = new HashMap<>();
        query = query.deriveQuery(MetricType.TOTAL_IMPRESSIONS);
        TotalQueryResult result;

        if (availableLogs.contains(LogType.IMPRESSION)) {
            result = (TotalQueryResult) model.resolveQuery(query);
            results.put(MetricType.TOTAL_IMPRESSIONS, result.getData());
        }
        if (availableLogs.contains(LogType.CLICK)) {
            query = query.deriveQuery(MetricType.TOTAL_CLICKS);
            result = (TotalQueryResult) model.resolveQuery(query);
            results.put(MetricType.TOTAL_CLICKS, result.getData());

            query = query.deriveQuery(MetricType.TOTAL_UNIQUES);
            result = (TotalQueryResult) model.resolveQuery(query);
            results.put(MetricType.TOTAL_UNIQUES, result.getData());
        }
        if (availableLogs.contains(LogType.CLICK) && availableLogs.contains(LogType.IMPRESSION)) {
            query = query.deriveQuery(MetricType.TOTAL_COST);
            result = (TotalQueryResult) model.resolveQuery(query);
            results.put(MetricType.TOTAL_COST, result.getData());

            query = query.deriveQuery(MetricType.CPC);
            result = (TotalQueryResult) model.resolveQuery(query);
            results.put(MetricType.CPC, result.getData());

            query = query.deriveQuery(MetricType.CTR);
            result = (TotalQueryResult) model.resolveQuery(query);
            results.put(MetricType.CTR, result.getData());

            query = query.deriveQuery(MetricType.CPM);
            result = (TotalQueryResult) model.resolveQuery(query);
            results.put(MetricType.CPM, result.getData());
        }

        if(availableLogs.contains(LogType.SERVER_LOG)){
            query = query.deriveQuery(MetricType.TOTAL_BOUNCES);
            result = (TotalQueryResult) model.resolveQuery(query);
            results.put(MetricType.TOTAL_BOUNCES, result.getData());

            query = query.deriveQuery(MetricType.TOTAL_CONVERSIONS);
            result = (TotalQueryResult) model.resolveQuery(query);
            results.put(MetricType.TOTAL_CONVERSIONS, result.getData());
        }

        if(availableLogs.contains(LogType.SERVER_LOG) && availableLogs.contains(LogType.CLICK)){
            query = query.deriveQuery(MetricType.CPA);
            result = (TotalQueryResult) model.resolveQuery(query);
            results.put(MetricType.CPA, result.getData());

            query = query.deriveQuery(MetricType.BOUNCE_RATE);
            result = (TotalQueryResult) model.resolveQuery(query);
            results.put(MetricType.BOUNCE_RATE, result.getData());
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

    public void refreshKeyMetrics(TotalQuery query) {
        Map<MetricType, Number> results;
        if (query.getFilters().isEmpty()) {
            results = fastNoFilterKeyMetrics();
        } else {
            results = calculateKeyMetrics(query);
        }

        frame.displayMetrics(results);
    }

    public void startMultiFilter() {
        DashboardMultiFilterController newController = new DashboardMultiFilterController(frame, model);
        newController.setAvailableLogs(availableLogs);
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

    public void setAvailableLogs(List<LogType> availableLogs) {
        this.availableLogs = availableLogs;
    }

    public void saveChartExport(WritableImage image, File saveLocation){
        try{
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", saveLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
