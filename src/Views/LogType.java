package Views;

/**
 * Created by marro on 21/02/2017.
 */
public enum LogType {
    IMPRESSION ("Impression"),
    CLICK ("Click"),
    SERVER ("Server");

    private final String name;

    private LogType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name + " Log";
    }

}
