package Views.ViewPresets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enum to define the different attributes that data can be sorted or filtered by
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

    //Sorts a series of data of the Income attribute (nastily and dirtily but hey ho)
    private static int incomeSort(String s1, String s2) {
        if (s1.equals("Low")) {
            return -1;
        } else if (s2.equals("High")) {
            return 1;
        } else {
            return 0;
        }
    }

    //Sorts a series of data of the Age attribute
    private static int ageSort(String s1, String s2) {
        if (s1.startsWith("<")) {
            return -1;
        }
        if (s1.startsWith(">")) {
            return 1;
        }
        String stripped1 = s1.replaceAll("[^0-9 -]", "").split("-")[0]; //Get rid of all non number characters
        String stripped2 = s2.replaceAll("[^0-9 -]", "").split("-")[0];

        return stripped1.compareTo(stripped2);
    }
}
