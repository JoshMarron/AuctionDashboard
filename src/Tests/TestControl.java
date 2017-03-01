package Tests;

import Controllers.*;

import org.junit.Test;
import org.junit.runner.notification.RunListener;

import static org.junit.Assert.assertEquals;

/**
 * Created by mati on 23/02/2017.
 */
public class TestControl {

    @Test
    public void testJunit() {
        System.out.println("Testing begun...");
        boolean i = true;
        assertEquals("Junit should work (should be true)", i, true);
    }

    @Test
    public void testNormalCalculateCTR(){
        assertEquals("should be 20.0", 20.0, MetricUtils.calculateCTR(100 , 5), 0.00005);

    }
    @Test
    public void testBoundaryCalculateCTR(){
        assertEquals("should be 20.0", 16.66667, MetricUtils.calculateCTR(100 , 6), 0.00005);

    }
    @Test
    public void testBoundaryCalculateCTR(){
        assertEquals("should be 20.0", 20.0, MetricUtils.calculateCTR(100 , 5), 0.00005);

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
