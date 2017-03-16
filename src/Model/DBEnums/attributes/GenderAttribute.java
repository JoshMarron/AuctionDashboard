package Model.DBEnums.attributes;

/**
 * Created by marro on 14/03/2017.
 */
public enum GenderAttribute implements Attribute {
    MALE ("Male"),
    FEMALE ("Female");

    private String name;

    private GenderAttribute(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
