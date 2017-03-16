package Controllers;

import Model.DBEnums.DateEnum;
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

    //TODO allow this to be set based on the device?
    private ExecutorService helpers = Executors.newFixedThreadPool(4);

    public DashboardMainFrameController(DashboardMainFrame frame, DatabaseManager model) {
        this.frame = frame;
        this.model = model;
        availableLogs = new ArrayList<>();
    }

    public void displayMainFrame(List<LogType> addedLogs) {
        availableLogs = addedLogs;
        Future<?> future = helpers.submit(() -> {
            SwingUtilities.invokeLater(() -> {
                frame.setVisible(true);
                frame.displayLoading();
            });
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
        this.requestTimeChart(MetricType.TOTAL_IMPRESSIONS);
    }

    public void requestTimeChart(MetricType type) {
        helpers.submit(() -> {
            Map<Instant, Number> data = getDataForChartFromType(type, DateEnum.DAYS);
            SwingUtilities.invokeLater(() -> frame.displayChart(type, DateEnum.DAYS, data));
        });
    }

    public void requestAttributeChart(MetricType type, AttributeType attr) {
        helpers.submit(() -> {
            Map<String, Number> data = getDataForChartFromAttribute(type, attr);
            SwingUtilities.invokeLater(() -> frame.displayChart(type, attr, data));
        });
    }

    private Map<Instant, Number> getDataForChartFromType(MetricType type, DateEnum granularity) {
        switch(type) {
            case TOTAL_CLICKS:
                return model.getClickCountPer(granularity, false);
            case TOTAL_UNIQUES:
                return model.getClickCountPer(granularity, true);
            case TOTAL_IMPRESSIONS:
                return model.getImpressionCountPer(granularity);
            case TOTAL_CONVERSIONS:
                return model.getConversionNumberPer(granularity);
            case TOTAL_BOUNCES:
                return model.getBounceNumberPer(granularity);
            case BOUNCE_RATE:
                return model.getBounceRatePer(granularity);
            case CPA:
                return model.getCPAPer(granularity);
            case CPC:
                return model.getCPCPer(granularity);
            case TOTAL_COST:
                return model.getTotalCostPer(granularity);
            case CPM:
                return model.getCPMPer(granularity);
            case CTR:
                return model.getCTRPer(granularity);
            default:
                return null;
        }
    }

    private Map<String, Number> getDataForChartFromAttribute(MetricType type, AttributeType attr) {
        switch (type) {
            case TOTAL_CLICKS:
                return model.getTotalClicksForAttribute(attr);
            case TOTAL_IMPRESSIONS:
                return model.getTotalImpressionsForAttribute(attr);
            case TOTAL_UNIQUES:
                return model.getTotalUniquesForAttribute(attr);
            case TOTAL_COST:
                return model.getTotalCostForAttribute(attr);
            case TOTAL_CONVERSIONS:
                return model.getTotalConversionsForAttribute(attr);
            case TOTAL_BOUNCES:
                return model.getTotalBouncesForAttribute(attr);
            case BOUNCE_RATE:
                return model.getBounceRateForAttribute(attr);
            case CPA:
                return model.getCPAForAttribute(attr);
            case CPC:
                return model.getCPCForAttribute(attr);
            case CPM:
                return model.getCPMForAttribute(attr);
            case CTR:
                return model.getCTRForAttribute(attr);
            default:
                return null;
        }
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
}
