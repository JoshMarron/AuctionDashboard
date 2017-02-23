package DataStructures;

import DataStructures.CsvInterfaces.IClickLog;

import java.time.Instant;

/**
 * Created by rhys on 23/02/17.
 */
public class ClickLog implements IClickLog {
    String Date;
    Long ID;
    Double Click_Cost;

    @Override
    public Instant getDate() {
        return null;
    }

    @Override
    public Long getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "ClickLog{" +
                "Date=" + Date +
                ", ID=" + ID +
                ", Click_Cost=" + Click_Cost +
                '}';
    }

    @Override
    public Double getClick_Cost() {
        return Click_Cost;
    }
}
