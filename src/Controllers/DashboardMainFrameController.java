package Controllers;

import Model.DBEnums.DateEnum;
import Model.DatabaseManager;
import Model.TableModels.Click;
import Model.TableModels.Impression;
import Model.TableModels.ServerVisit;
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
        SwingUtilities.invokeLater(() -> frame.displayChart(MetricType.TOTAL_CLICKS, model.getClickCountPer(DateEnum.DAYS, false)));
    }

    private Map<MetricType, Number> calculateKeyMetrics() {
        Map<MetricType, Number> results = new HashMap<>();

        List<Impression> impressionList = model.selectAllImpressions();
        List<Click> clickList = model.selectAllClicks();
        List<Double> clickCosts = clickList.stream().map(Click::getCost).collect(Collectors.toList());
        List<ServerVisit> visits = model.getAllServerVisits();

        if (availableLogs.contains(LogType.IMPRESSION)) {
            int impressionCount = MetricUtils.getImpressionCount(impressionList);
            results.put(MetricType.TOTAL_IMPRESSIONS, impressionCount);
        }
        if (availableLogs.contains(LogType.CLICK)) {
            results.put(MetricType.TOTAL_COST, MetricUtils.calculateTotalCost(clickCosts));
            results.put(MetricType.TOTAL_CLICKS, clickList.size());
            results.put(MetricType.CPC, MetricUtils.getCostPerClick(clickCosts, clickList.size()));
        }
        if (availableLogs.contains(LogType.CLICK) && availableLogs.contains(LogType.IMPRESSION)) {
            int clickCount = clickList.size();
            int impressionCount = MetricUtils.getImpressionCount(impressionList);
            results.put(MetricType.CTR, MetricUtils.calculateCTR(clickCount, impressionCount));
            results.put(MetricType.CPM, MetricUtils.getCostPerImpression(impressionList, clickCosts));
        }

        if(availableLogs.contains(LogType.SERVER_LOG)){
            results.put(MetricType.TOTAL_BOUNCES, MetricUtils.getBounceCount(visits));
            results.put(MetricType.TOTAL_CONVERSIONS, MetricUtils.getConversionCount(visits));
        }

        if(availableLogs.contains(LogType.SERVER_LOG) && availableLogs.contains(LogType.CLICK)){
            results.put(MetricType.CPA, MetricUtils.getCostPerAcquisition(visits, clickCosts));
            results.put(MetricType.BOUNCE_RATE, MetricUtils.getBounceRate(clickList, visits));
        }


        return results;

    }
}
