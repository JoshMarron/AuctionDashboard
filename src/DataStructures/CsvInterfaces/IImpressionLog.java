package DataStructures.CsvInterfaces;

/**
 * Created by rhys on 21/02/17.
 */
public interface IImpressionLog {

    String getDate();

    IImpressionLog setDate(String x);

    Long getID();

    IImpressionLog setID(String x);

    Gender getGender();

    IImpressionLog setGender(String x);

    Integer getMinAge();

    Integer getMaxAge();

    Income getIncome();

    IImpressionLog setIncome(String x);

    String getContext();

    IImpressionLog setContext(String x);

    Double getImpressionCost();

    IImpressionLog setAge(String x);

    IImpressionLog setImpression_Cost(String x);

}

