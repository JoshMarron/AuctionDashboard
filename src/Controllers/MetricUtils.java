package Controllers;

import Model.TableModels.Impression;

import java.util.ArrayList;
import java.util.List;

public class MetricUtils {

    public static double calculateCTR(int noOfClicks, int noOfImpressions){
        return ((double) noOfClicks / (double) noOfImpressions);
    }

    public static double calculateTotalCost(List<Double> clickCost){
        return clickCost.stream().mapToDouble(Double::doubleValue).sum(); //Returns the sum of all numbers in the list
    }

    public static int getImpressionCount(List<Impression> impressions){
        return impressions.size();
    }
}
