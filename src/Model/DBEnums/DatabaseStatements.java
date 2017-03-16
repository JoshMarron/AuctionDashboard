package Model.DBEnums;

/**
 * Created by marro on 01/03/2017.
 */
public enum DatabaseStatements {

    DROP_USER ("DROP TABLE IF EXISTS user;"),
    DROP_CLICK ("DROP TABLE IF EXISTS click;"),
    DROP_SITE_IMPRESSION ("DROP TABLE IF EXISTS site_impression;"),
    DROP_SERVER_LOG ("DROP TABLE IF EXISTS server_log;"),
    CREATE_USER ("" +
            "CREATE TABLE user (\n" +
            " user_id INTEGER PRIMARY KEY ON CONFLICT IGNORE, \n" +
            " age TEXT NOT NULL, \n" +
            " gender TEXT NOT NULL, \n" +
            " income TEXT NOT NULL \n" +
            ");"),
    CREATE_SITE_IMPRESSION ("" +
            "CREATE TABLE site_impression (\n" +
            " site_impression_id INTEGER PRIMARY KEY, \n" +
            " user_id INTEGER NOT NULL, \n" +
            " context TEXT NOT NULL, \n" +
            " impression_cost REAL NOT NULL, \n" +
            " impression_date STRING NOT NULL," +
            " FOREIGN KEY (user_id) REFERENCES user(user_id) " +
            ");"),
    CREATE_CLICK ("" +
            "CREATE TABLE click (\n" +
            " click_id INTEGER PRIMARY KEY, \n" +
            " user_id INTEGER NOT NULL, \n" +
            " click_date TEXT NOT NULL, \n" +
            " cost REAL NOT NULL \n," +
            " FOREIGN KEY (user_id) REFERENCES user(user_id)" +
            ");"),
    CREATE_SERVER_LOG ("" +
            "CREATE TABLE server_log (\n" +
            " server_log_id INTEGER PRIMARY KEY, \n" +
            " user_id INTEGER, \n" +
            " entry_date TEXT NOT NULL, \n" +
            " exit_date TEXT NOT NULL, \n" +
            " pages_viewed INTEGER NOT NULL, \n" +
            " conversion TEXT NOT NULL, \n" +
            " FOREIGN KEY (user_id) REFERENCES user(user_id)" +
            ");"),
    INDEX_IMPRESSION_DATE("" +
            "CREATE INDEX impression_date_index ON site_impression(impression_date);"),
    INDEX_CLICK_DATE("" +
            "CREATE INDEX click_date_index ON click(click_date);"),
    INDEX_SERVER_LOG_DATE("" +
            "CREATE INDEX server_log_date_index ON server_log(entry_date);");

    private String statement;

    private DatabaseStatements(String statement) {
        this.statement = statement;
    }

    public String getStatement() {
        return this.statement;
    }
}
