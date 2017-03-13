package Controllers;

import Model.TableModels.Click;
import Model.TableModels.Impression;
import Model.TableModels.ServerVisit;

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

    public static int getBounceCount(List<ServerVisit> log){
        int count = 0;
        for(ServerVisit visit : log){
            if (visit.getPagesViewed() == 1){
                count++;
            }
        }
        return count;
    }

    public static double getBounceRate(List<Click> clicks, List<ServerVisit> visits){
        return (getBounceCount(visits)/clicks.size());
    }

    public static int getConversionCount(List<ServerVisit> serverLog){
        int count = 0;
        for(ServerVisit visit : serverLog){
            if(visit.isConversion()){
                count ++;
            }
        }
        return count;
    }

    public static double getCostPerAcquisition(List<ServerVisit> serverLog, List<Double> clickCost){
        return (calculateTotalCost(clickCost)/getConversionCount(serverLog));
    }

    public static double getCostPerImpression(List<Impression> impressions, List<Double> clickCosts){
        return(calculateTotalCost(clickCosts)/getImpressionCount(impressions));
    }

    //CPM
    public static double getCostPerThousandImpressions(List<Impression>impressions, List<Double> clickCosts){
        return (getCostPerImpression(impressions, clickCosts)/1000);
    }
}
