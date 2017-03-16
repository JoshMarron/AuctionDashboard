package Model.DBEnums.attributes;

/**
 * Created by marro on 14/03/2017.
 */
public enum AgeAttribute implements Attribute {
    UNDER_25 (1, "<25"),
    RANGE_25_34 (2, "25-34"),
    RANGE_35_44 (3, "35-44"),
    RANGE_45_54 (4, "45-54"),
    OVER_55 (5, ">55");

    private int order;
    private String name;

    private AgeAttribute(int order, String name) {
        this.order = order;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public int getOrder() {
        return this.order;
    }
}
