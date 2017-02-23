package DataStructures.CsvInterfaces;

import java.time.Instant;

/**
 * Created by rhys on 21/02/17.
 */
public interface IClickLog {
    Instant getDate();

    Long getID();

    Double getClick_Cost();
}
