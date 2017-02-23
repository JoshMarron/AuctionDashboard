package DataStructures.CsvInterfaces;

import java.time.Instant;

/**
 * Created by rhys on 21/02/17.
 */
public interface IImpressionLog extends CsvTable {

    Instant getDate();

    Long getID();

    Gender getGender();

    Integer getMinAge();

    Integer getMaxAge();

    Income getIncome();

    String getContext();

    Double getImpressionCost();

}

