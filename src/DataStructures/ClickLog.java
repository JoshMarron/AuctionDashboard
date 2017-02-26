package DataStructures;

import DataStructures.CsvInterfaces.IClickLog;

import java.time.*;

/**
 * Created by rhys on 23/02/17.
 */
public class ClickLog implements IClickLog {
    private String rawDate;
    private Long ID;
    private Double Click_Cost;

    public ClickLog() {
    }

    @Override
    public String getDate() {
        return rawDate;
    }

    public ClickLog setDate(String date) {
        LocalDate localDate = LocalDate.parse(date.split(" ")[0]);
        LocalDateTime localDateTime = localDate.atTime(LocalTime.parse(date.split(" ")[1]));
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        rawDate = date;
        return this;
    }

    @Override
    public Long getID() {
        return ID;
    }

    public ClickLog setID(String ID) {
        this.ID = Long.parseLong(ID);
        return this;
    }

    @Override
    public String toString() {
        return "ClickLog{" +
                "Date=" + rawDate +
                ", ID=" + ID +
                ", Click_Cost=" + Click_Cost +
                '}';
    }

    @Override
    public Double getClick_Cost() {
        return Click_Cost;
    }

    public ClickLog setClick_Cost(String click_Cost) {
        Click_Cost = Double.parseDouble(click_Cost);
        return this;
    }
}
