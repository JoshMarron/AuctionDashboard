package DataStructures.CsvInterfaces;

/**
 * Created by rhys on 21/02/17.
 */
public interface IImpressionLog {

    String getDate();

    Long getID();

    Gender getGender();

    Integer getMinAge();

    Integer getMaxAge();

    Income getIncome();

    String getContext();

    Double getImpressionCost();

}

