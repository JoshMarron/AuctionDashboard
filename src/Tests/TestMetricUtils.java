package Tests;

import Controllers.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by mati on 23/02/2017.
 */
public class TestMetricUtils {
//OLD TESTS BEFORE MODIFICATION
//    @Test
//    public void testNormal1CalculateCTR(){
//        assertEquals("should be 20.0", 0.05, MetricUtils.calculateCTR(5 , 100), 0.00005);
//    }
//    @Test
//    public void testNormal2CalculateCTR(){
//        assertEquals("should be 20.0", 0.0507, MetricUtils.calculateCTR(5.07 , 100), 0.00005);
//    }
// NEW TESTS
    @Test
    public void testNormal3CalculateCTR(){
        assertEquals("should be 20.0", 0.0606, MetricUtils.calculateCTR(6 , 99), 0.00005);
    }
    @Test
    public void testBoundaryLowCalculateCTR(){
        assertEquals("should be 20.0", 0.0, MetricUtils.calculateCTR(0 , 6), 0.00005);
    }
    @Test
    public void testBoundaryHighCalculateCTR(){
        assertEquals("should be 20.0", 1.0, MetricUtils.calculateCTR(6 , 6), 0.00005);
    }
    @Test
    public void testOutOfBoundary1CalculateCTR(){
        assertEquals("should be 20.0", 1.5, MetricUtils.calculateCTR(6 , 4), 0.00005);
    }
    @Test
    public void testOutOfBoundary2CalculateCTR(){
        assertEquals("should be 20.0", -1.5, MetricUtils.calculateCTR(6 , -4), 0.00005);
    }
    @Test
    public void testErroneousCalculateCTR(){
        assertEquals("should be infite",true,  Double.isInfinite(MetricUtils.calculateCTR(100, 0)));
    }




    @Test
    public void testNormalCalculateTotalCost(){
        List<Double> list = Arrays.asList(51.5, 0.064, 0.125);
        assertEquals("should be 51.689", 51.689, MetricUtils.calculateTotalCost(list), 0.00005);
    }
    @Test
    public void testBoundaryCalculateTotalCost(){
        List<Double> list = Arrays.asList(51.5);
        assertEquals("should be 51.5", 51.5, MetricUtils.calculateTotalCost(list), 0.00005);
    }
    @Test
    public void testNullCalculateTotalCost(){
        List<Double> list = Arrays.asList();
        assertEquals("should be 0",  0, MetricUtils.calculateTotalCost(list), 0.00005);
    }

//To do
//    @Test
//    public void testGetImpressionCount(){
//
//    }
}
