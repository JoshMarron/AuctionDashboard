package Model.TableModels;

import java.time.Instant;

/**
 * This class models a Click after it comes out of the database
 */
public class Click {

    private long clickID;
    private long userID;
    private Instant clickDate;
    private double cost;

    public Click(long clickID, long userID, Instant clickDate, double cost) {
        this.clickID = clickID;
        this.userID = userID;
        this.clickDate = clickDate;
        this.cost = cost;
    }

    public long getClick_id() {
        return clickID;
    }

    public long getUser_id() {
        return userID;
    }

    public Instant getClick_date() {
        return clickDate;
    }

    public double getCost() {
        return cost;
    }

}
