package Views.ViewPresets;

/**
 * Created by marro on 14/03/2017.
 */
public enum AttributeType {

    GENDER ("Gender"),
    INCOME ("Income"),
    AGE ("Age"),
    CONTEXT ("Context");

    private String name;

    private AttributeType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
