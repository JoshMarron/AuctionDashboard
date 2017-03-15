package Views.ViewPresets;

/**
 * Created by marro on 14/03/2017.
 */
public enum AttributeType {

    GENDER ("Gender", "gender"),
    INCOME ("Income", "income"),
    AGE ("Age", "age"),
    CONTEXT ("Context", "context");

    private String name;
    private String querybit;

    private AttributeType(String name, String querybit) {
        this.name = name;
        this.querybit = querybit;
    }

    public String toString() {
        return this.name;
    }

    public String getQueryBit() {
        return this.querybit;
    }
}
