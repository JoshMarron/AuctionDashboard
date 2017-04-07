package Views.ViewPresets;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by marro on 14/03/2017.
 */
public enum AttributeType {

    GENDER ("Gender", "gender"),
    INCOME ("Income", "income"),
    AGE ("Age", "age"),
    CONTEXT ("Context", "context");
    
    private final String sql;
    private final String name;

    private AttributeType(String name, String sql) {
        this.name = name;
        this.sql = sql;
    }

    public String toString() {
        return this.name;
    }
    
    public String getQueryBit() {
        return this.sql;
    }

    public static List<String> sortAttributeValues(AttributeType type, List<String> values) {

        switch (type) {
            case AGE:
                return values.stream().sorted(AttributeType::ageSort).collect(Collectors.toList());
            case INCOME:
                return values.stream().sorted(AttributeType::incomeSort).collect(Collectors.toList());
            default:
                return values.stream().sorted().collect(Collectors.toList());
        }
    }

    private static int incomeSort(String s1, String s2) {
        if (s1.equals("Low")) {
            return -1;
        } else if (s2.equals("High")) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int ageSort(String s1, String s2) {
        if (s1.startsWith("<")) {
            return -1;
        }
        if (s1.startsWith(">")) {
            return 1;
        }
        String stripped1 = s1.replaceAll("[^0-9 -]", "").split("-")[0];
        String stripped2 = s2.replaceAll("[^0-9 -]", "").split("-")[0];

        return stripped1.compareTo(stripped2);
    }
}
