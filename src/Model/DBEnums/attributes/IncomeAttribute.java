package Model.DBEnums.attributes;

/**
 * Created by marro on 14/03/2017.
 */
public enum IncomeAttribute implements Attribute {
    HIGH ("High"),
    MEDIUM ("Medium"),
    LOW ("Low");

    private String name;

    private IncomeAttribute(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }
}
