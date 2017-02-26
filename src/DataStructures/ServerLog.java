package DataStructures;

import DataStructures.CsvInterfaces.IServerLog;

import java.time.*;

/**
 * Created by rhys on 26/02/17.
 */
public class ServerLog implements IServerLog {
    private String Entry_Date;
    private long secondsEpochEntry_Date;
    private Long ID;
    private String Exit_Date;
    private int Pages_Viewed;
    private boolean Conversion;
    private long secondsEpochExit_Date;

    public ServerLog setEntry_Date(String entry_Date) {
        LocalDate localDate = LocalDate.parse(entry_Date.split(" ")[0]);
        LocalDateTime localDateTime = localDate.atTime(LocalTime.parse(entry_Date.split(" ")[1]));
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        secondsEpochEntry_Date = instant.getEpochSecond();
        Entry_Date = entry_Date;
        return this;
    }

    public ServerLog setExit_Date(String exit_Date) {
        LocalDate localDate = LocalDate.parse(exit_Date.split(" ")[0]);
        LocalDateTime localDateTime = localDate.atTime(LocalTime.parse(exit_Date.split(" ")[1]));
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        secondsEpochExit_Date = instant.getEpochSecond();
        Exit_Date = exit_Date;
        return this;
    }

    public ServerLog setPages_Viewed(String pages_Viewed) {
        Pages_Viewed = Integer.parseInt(pages_Viewed);
        return this;
    }

    @Override
    public Long getID() {
        return ID;
    }

    public ServerLog setID(String ID) {
        this.ID = Long.parseLong(ID);
        return this;
    }

    @Override
    public String getEntryDate() {
        return Entry_Date;
    }

    @Override
    public String getExitDate() {
        return Exit_Date;
    }

    @Override
    public Integer getPagesViewed() {
        return Pages_Viewed;
    }

    @Override
    public Boolean getConversion() {
        return this.Conversion;
    }

    public ServerLog setConversion(String conversion) {
        switch (conversion.toLowerCase()) {
            case "yes":
                Conversion = true;
                break;
            case "y":
                Conversion = true;
                break;
            case "true":
                Conversion = true;
                break;
            case "t":
                Conversion = true;
                break;
            case "1":
                Conversion = true;
                break;
            case "no":
                Conversion = false;
                break;
            case "n":
                Conversion = false;
                break;
            case "false":
                Conversion = false;
                break;
            case "f":
                Conversion = false;
                break;
            case "0":
                Conversion = false;
                break;
        }
        return this;
    }


}
