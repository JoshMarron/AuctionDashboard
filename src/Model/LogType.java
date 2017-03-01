package Model;

/**
 * Enum to model the three types of log the program can receive
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

    public String prettyPrint() {
        String temp = this.name.replace("_log", "") + " Log";
        return temp.substring(0, 1).toUpperCase() + temp.substring(1);
    }

}
