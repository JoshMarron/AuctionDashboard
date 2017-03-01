package Views;

/**
 * Created by marro on 01/03/2017.
 */
public enum MetricType {
    TOTAL_IMPRESSIONS ("Total Number of Impressions", "Load the Impressions file to view this metric."),
    CTR ("Click Through Rate (CTR)", "Load the Click and Impressions file to view this metric"),
    TOTAL_COST ("Total Cost (in pence)", "Load the Click file to view this metric");

    private final String name;
    private final String error;

    private MetricType(String name, String error) {
        this.name = name;
        this.error = error;
    }

    public String toString() {
        return this.name;
    }

    public String getErrorMessage() {
        return this.error;
    }
}
