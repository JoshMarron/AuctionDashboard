package Views.ViewPresets;

/**
 * Created by marro on 16/03/2017.
 */
public enum ChartType {

    LINE ("Line Graph"),
    BAR ("Bar Chart"),
    PIE ("Pie Chart");

    private String name;

    private ChartType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}
