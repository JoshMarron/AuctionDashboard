package Model;

/**
 * Created by marro on 21/02/2017.
 */
public enum LogType {
    IMPRESSION ("impression"),
    CLICK ("click"),
    SERVER_LOG("server_log");

    private final String name;

    private LogType(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name + " Log";
    }

}
