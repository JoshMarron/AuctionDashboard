package Tests;

import Controllers.*;

import org.junit.Test;
import org.junit.runner.notification.RunListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by mati on 23/02/2017.
 */
public class TestControl {

//    @Test
//    public void testNormal1CalculateCTR(){
//        assertEquals("should be 20.0", 0.05, MetricUtils.calculateCTR(5 , 100), 0.00005);
//    }
//    @Test
//    public void testNormal2CalculateCTR(){
//        assertEquals("should be 20.0", 0.0507, MetricUtils.calculateCTR(5.07 , 100), 0.00005);
//    }
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


//    @Test
//    public void testCalculateTotalCost(){
//
//    }
//
//
//    @Test
//    public void testGetImpressionCount(){
//
//    }
}
