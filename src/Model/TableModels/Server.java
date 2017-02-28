package Model.TableModels;

import java.time.Instant;

/**
 * The Server class models an entry in the Server table after it comes out of the database
 */
public class Server {

    private long ServerLogID;
    private long userID;
    private Instant entryDate;
    private Instant exitDate;
    private int pagesViewed;
    private boolean conversion;

    public Server(long ServerLogID, long userID, Instant entryDate,
                  Instant exitDate, int pagesViewed, boolean conversion) {
        this.ServerLogID = ServerLogID;
        this.userID = userID;
        this.entryDate = entryDate;
        this.exitDate = exitDate;
        this.pagesViewed = pagesViewed;
        this.conversion = conversion;
    }

    public long getServerLogID() {
        return ServerLogID;
    }

    public long getUserID() {
        return userID;
    }

    public Instant getEntryDate() {
        return entryDate;
    }

    public Instant getExitDate() {
        return exitDate;
    }

    public int getPagesViewed() {
        return pagesViewed;
    }

    public boolean isConversion() {
        return conversion;
    }
}
