package Controllers;

import Model.TableModels.Click;
import Model.TableModels.Impression;

import java.util.ArrayList;
import java.util.List;

public class MetricUtils {
	
	/**
	 * Calculate CTR
	 * @param noOfClicks overall, non-unique number of clicks
	 * @param noOfImpressions overall, non-unique number of impressions
	 * @return CTR
	 */
    public static double calculateCTR(int noOfClicks, int noOfImpressions){
        return ((double) noOfClicks / (double) noOfImpressions);
    }
	
	/**
	 * Calculate CTC
	 * @param clickCost list of costs
	 * @return CTC
	 */
	public static double calculateTotalCost(List<Double> clickCost){
        return clickCost.stream().mapToDouble(Double::doubleValue).sum(); //Returns the sum of all numbers in the list
    }
	
    // TODO do this in the DatabaseManager
	public static int getImpressionCount(List<Impression> impressions){
        return impressions.size();
    }
    
    // TODO do this in the DatabaseManager
    public static int getClickCount(List<Click> clicks){return clicks.size();}
	
	/**
	 * Calculate CPC
	 * @param clickCost list of click  costs
	 * @param noOfClicks non-unique number of clicks
	 * @return CPC
	 */
	public static double getCostPerClick(List<Double> clickCost, int noOfClicks){
        return calculateTotalCost(clickCost)/noOfClicks;
    }
}
