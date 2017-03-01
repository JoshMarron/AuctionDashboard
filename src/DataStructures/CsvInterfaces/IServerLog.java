package DataStructures.CsvInterfaces;

/**
 * Created by rhys on 21/02/17.
 */
public interface IServerLog {

    Long getID();

    IServerLog setID(String x);

    String getEntryDate();

    String getExitDate();

    Integer getPagesViewed();

    Boolean getConversion();

    IServerLog setConversion(String x);

    IServerLog setEntry_Date(String x);

    IServerLog setExit_Date(String x);

    IServerLog setPages_Viewed(String x);

}
