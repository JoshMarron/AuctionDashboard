import java.util.ArrayList;

public class MetricUtils {

    public static double calculateCTR(int noOfClicks, int noOfImpressions){
        return (noOfClicks/noOfImpressions);
    }

    public static double calculateTotalCost(ArrayList<Double> clickCost){
        return clickCost.stream().mapToDouble(Double::doubleValue).sum();
    }
}
