package DataStructures.CsvInterfaces;

import java.time.Instant;

/**
 * Created by rhys on 21/02/17.
 */
public interface IServerLog {

    Long getID();

    Instant getEntryDate();

    Instant getExitDate();

    Integer getPagesViewed();

    Boolean getConversion();
}
