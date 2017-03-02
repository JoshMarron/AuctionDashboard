package DataStructures.CsvInterfaces;

/**
 * Created by rhys on 21/02/17.
 */
public interface IClickLog {
    String getDate();

    IClickLog setDate(String x);

    Long getID();

    IClickLog setID(String x);

    Double getClick_Cost();

    IClickLog setClick_Cost(String x);

}
