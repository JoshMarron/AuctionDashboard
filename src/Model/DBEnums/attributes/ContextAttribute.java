package Model.DBEnums.attributes;

/**
 * Created by marro on 14/03/2017.
 */
public enum ContextAttribute implements Attribute {

    BLOG ("Blog"),
    SHOPPING ("Shopping"),
    SOCIAL_MEDIA ("Social Media"),
    NEWS ("News");

    private String name;

    private ContextAttribute(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }
}
