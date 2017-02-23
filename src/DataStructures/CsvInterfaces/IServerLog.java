package DataStructures.CsvInterfaces;

import java.time.Instant;

/**
 * Created by rhys on 21/02/17.
 */
public interface IServerLog extends CsvTable {

    Long getID();

    Instant getEntryDate();

    Instant getExitDate();

    Integer getPagesViewed();

    Boolean getConversion();
}
