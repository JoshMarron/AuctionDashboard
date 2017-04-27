package Views;

import Model.DBEnums.LogType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by marro on 01/03/2017.
 */
public enum MetricType {
    TOTAL_IMPRESSIONS ("Total Number of Impressions",
            "You need an Impressions file to view this metric.",
            Arrays.asList(LogType.IMPRESSION),
            "impression_date"),
    TOTAL_CLICKS ("Total Number of Clicks",
            "You need a Click file to view this metric.",
            Arrays.asList(LogType.CLICK),
            "click_date"),
    TOTAL_UNIQUES ("Total Number of Unique Clicks",
            "You need a Click file to view this metric.",
            Arrays.asList(LogType.CLICK),
            "click_date"),
    TOTAL_BOUNCES ("Total Number of Bounces",
            "You need a Server Log file to view this metric",
            Arrays.asList(LogType.SERVER_LOG), ""),
    TOTAL_CONVERSIONS ("Total Number of Conversions",
            "You need a Server Log file to view this metric",
            Arrays.asList(LogType.SERVER_LOG),
            "entry_date"),
    CPA ("Cost per Acquisition (CPA)",
            "You need Click and Server Log files to view this metric",
            Arrays.asList(LogType.SERVER_LOG, LogType.CLICK), ""),
    CPC ("Cost per Click (CPC)",
            "You need a Click file to view this metric",
            Arrays.asList(LogType.CLICK), ""),
    CPM ("Cost per Thousand Impressions (CPM)",
            "You need an Impression file to view this metric",
            Arrays.asList(LogType.IMPRESSION), ""),
    BOUNCE_RATE ("Bounce Rate",
            "You need a Server Log file to view this metric",
            Arrays.asList(LogType.SERVER_LOG), ""),
    CTR ("Click Through Rate (CTR)",
            "You need Click and Impressions files to view this metric.",
            Arrays.asList(LogType.IMPRESSION), ""),
    TOTAL_COST ("Total Cost (in pence)",
            "You need a Click file to view this metric.",
            Arrays.asList(LogType.CLICK), "");

    private final String name;
    private final String error;
    private final List<LogType> requiredLogs;
    private final String dateString;

    private MetricType(String name, String error, List<LogType> requiredLogs, String dateString) {
        this.name = name;
        this.error = error;
        this.requiredLogs = requiredLogs;
        this.dateString = dateString;
    }

    public String toString() {
        return this.name;
    }

    public String getErrorMessage() {
        return this.error;
    }

    public List<LogType> getRequiredLogs() {
        return this.requiredLogs;
    }
}
