package Views.ViewPresets;

/**
 * Created by marro on 14/03/2017.
 */
public enum AttributeType {

    GENDER ("Gender", "gender"),
    INCOME ("Income", "income"),
    AGE ("Age", "age"),
    CONTEXT ("Context", "income");
    
    private final String sql;
    private final String name;

    private AttributeType(String name, String sql) {
        this.name = name;
        this.sql = sql;
    }

    public String toString() {
        return this.name;
    }
    
    public String toSQL() {
        return this.sql;
    }
}
