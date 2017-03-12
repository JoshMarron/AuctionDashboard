package Controllers;

import Model.DatabaseManager;
import Model.TableModels.Click;
import Model.TableModels.Impression;
import Views.DashboardMainFrame;
import Model.DBEnums.LogType;
import Views.MetricType;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
        availableLogs.addAll(addedLogs);
        Map<MetricType, Number> results = this.calculateKeyMetrics();
        this.displayMetrics(results);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    public void displayMetrics(Map<MetricType, Number> data) {
        SwingUtilities.invokeLater(() -> frame.displayMetrics(data));
    }

    private Map<MetricType, Number> calculateKeyMetrics() {
        Map<MetricType, Number> results = new HashMap<>();

        List<Impression> impressionList = model.selectAllImpressions();
        List<Click> clickList = model.selectAllClicks();
        List<Double> clickCosts = clickList.stream().map(Click::getCost).collect(Collectors.toList());

        if (availableLogs.contains(LogType.IMPRESSION)) {
            int impressionCount = MetricUtils.getImpressionCount(impressionList);
            results.put(MetricType.TOTAL_IMPRESSIONS, impressionCount);
        }
        if (availableLogs.contains(LogType.CLICK)) {
            results.put(MetricType.TOTAL_COST, MetricUtils.calculateTotalCost(clickCosts));
        }
        if (availableLogs.contains(LogType.CLICK) && availableLogs.contains(LogType.IMPRESSION)) {
            int clickCount = clickList.size();
            int impressionCount = MetricUtils.getImpressionCount(impressionList);
            results.put(MetricType.CTR, MetricUtils.calculateCTR(clickCount, impressionCount));
        }

        return results;

    }
}
