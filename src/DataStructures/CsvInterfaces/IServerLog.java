package DataStructures.CsvInterfaces;

/**
 * Created by rhys on 21/02/17.
 */
public interface IServerLog {

    Long getID();

    String getEntryDate();

    String getExitDate();

    Integer getPagesViewed();

    Boolean getConversion();
}
