package Views;

/**
 * Created by marro on 01/03/2017.
 */
public enum MetricType {
    TOTAL_IMPRESSIONS ("Total Number of Impressions"),
    CTR ("Click Through Rate (CTR)"),
    TOTAL_COST ("Total Cost (in pence)");

    private final String name;

    private MetricType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
