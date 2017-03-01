package Model.TableModels;

import java.time.Instant;

/**
 * The Impression class models an Impression after it comes out of the database
 */
public class Impression {

    private long siteImpressionID;
    private Instant impressionDate;
    private long userID;
    private String context;
    private double impressionCost;

    public Impression(long siteImpressionID, long userID, String context, double impressionCost, Instant impressionDate) {
        this.siteImpressionID = siteImpressionID;
        this.impressionDate = impressionDate;
        this.userID = userID;
        this.context = context;
        this.impressionCost = impressionCost;
    }

    public long getSiteImpressionID() {
        return siteImpressionID;
    }

    public long getUserID() {
        return userID;
    }

    public String getContext() {
        return context;
    }

    public double getImpressionCost() {
        return impressionCost;
    }

    public String toString() {
        return "" + this.siteImpressionID + " " + this.userID;
    }

    public Instant getImpressionDate() {
        return impressionDate;
    }
}
