package Tests;

import Controllers.DashboardDoubleMainFrameController;
import Controllers.DashboardMainFrameController;
import Controllers.Queries.TimeDataQuery;
import Controllers.Queries.TimeQueryBuilder;
import Controllers.Results.TimeQueryResult;
import Model.DBEnums.LogType;
import Model.DatabaseManager;
import Views.DashboardMainFrame;
import Views.MetricType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by marro on 27/04/2017.
 */
public class ComponentAndIntegrationTests {

    DatabaseManager model;
    File testDB;
    Map<Instant, Number> testMap;
    Instant day1 = Instant.parse("2015-01-01T12:00:00Z").truncatedTo(ChronoUnit.DAYS); //1st jan
    Instant day2 = Instant.parse("2015-01-02T12:00:00Z").truncatedTo(ChronoUnit.DAYS); //2nd jan
    Instant day3 = Instant.parse("2015-01-03T12:00:00Z").truncatedTo(ChronoUnit.DAYS); //3rd jan
    Instant day4 = Instant.parse("2015-01-04T12:00:00Z").truncatedTo(ChronoUnit.DAYS); //4th jan
    Instant day5 = Instant.parse("2015-01-05T12:00:00Z").truncatedTo(ChronoUnit.DAYS); //5th jan

    @Before
    public void setUp() {

        testDB = new File("db/testdb");
        model = new DatabaseManager();
        model.createDB(testDB.getAbsolutePath());

        String[] dummyImpression1 = {"2015-01-01 12:00:00", "1", "Male", "25-34", "High", "Blog", "200"};
        String[] dummyImpression2 = {"2015-01-01 14:00:00", "2", "Female", "<25", "High", "Fashion", "400"};
        String[] dummyImpression3 = {"2015-01-02 12:00:00", "3", "Male", "35-44", "Low", "Social Media", "600"};
        String[] dummyImpression4 = {"2015-01-03 15:00:00", "4", "Female", ">55", "Medium", "Social Media", "800"};

        String[] dummyClick1 = {"2015-01-01 12:00:00", "1", "1200"};
        String[] dummyClick2 = {"2015-01-02 12:00:00", "3", "2400"};

        String[] dummyServer1 = {"2015-01-01 12:00:00", "1", "n/a", "1", "No"};
        String[] dummyServer2 = {"2015-01-02 14:00:00", "3", "n/a", "10", "Yes"};
        String[] dummyServer3 = {"2015-01-04 16:00:00", "2", "n/a", "1", "No"};
        String[] dummyServer4 = {"2015-01-05 17:00:00", "4", "n/a", "2", "Yes"};

        List<String[]> dummyImpression = Arrays.asList(
                dummyImpression1,
                dummyImpression2,
                dummyImpression3,
                dummyImpression4
        );

        List<String[]> dummyClick = Arrays.asList(
                dummyClick1,
                dummyClick2
        );

        List<String[]> dummyServer = Arrays.asList(
                dummyServer1,
                dummyServer2,
                dummyServer3,
                dummyServer4
        );

        model.insertData(LogType.IMPRESSION, dummyImpression);
        model.insertData(LogType.CLICK, dummyClick);
        model.insertData(LogType.SERVER_LOG, dummyServer);
    }

    @After
    public void tearDown() {
        testDB.delete();
        System.out.println(testDB.exists());
    }

    // Proves the map properly goes into the cache + that the controller interfaces with the model
    @Test
    public void testControllerPutsMapInCache() {
        DashboardMainFrame frame = new DashboardMainFrame(null);
        DashboardMainFrameController controller = new DashboardMainFrameController(frame, model);
        frame.setController(controller);
        TimeDataQuery query = new TimeQueryBuilder(MetricType.TOTAL_IMPRESSIONS).build();
        SwingUtilities.invokeLater(() -> {
            frame.init();
            controller.requestTimeChart(query);
            TimeQueryResult result = (TimeQueryResult) controller.getCache().hitCache(query);
            Assert.assertEquals(MetricType.TOTAL_IMPRESSIONS, result.getMetric());

            Assert.assertEquals(3, result.getData().size());
            Assert.assertEquals(2, result.getData().get(day1));
        });

    }

    @Test
    public void testSwitchToDashboardDoubleMainFrameController() {
        DashboardMainFrame frame = new DashboardMainFrame(null);
        DashboardMainFrameController controller = new DashboardMainFrameController(frame, model);
        frame.setController(controller);
        TimeDataQuery query = new TimeQueryBuilder(MetricType.TOTAL_CLICKS).build();
        SwingUtilities.invokeLater(() -> {
            frame.init();
            // Test the controller isn't already a double controller
            Assert.assertFalse(frame.getController() instanceof DashboardDoubleMainFrameController);
            frame.addSecondCampaign();
            controller.addSecondCampaign(new File("db/testdb"));
            // Test that the controller was switched
            Assert.assertTrue(frame.getController() instanceof DashboardDoubleMainFrameController);

            controller.requestTimeChart(query);

            // Check the data was cached properly inside the new controller
            TimeQueryResult result1 = (TimeQueryResult) controller.getCache().hitCache(query);
            TimeQueryResult result2 = (TimeQueryResult) ((DashboardDoubleMainFrameController) controller).getSecondCache().hitCache(query);

            // Since we are using the same database twice, these should be the same
            Assert.assertEquals(result1, result2);

            // Check we actually got the correct data
            Assert.assertEquals(2, result1.getData().size());
            Assert.assertEquals(1, result1.getData().get(day1));
        });
    }

    @Test
    public void testSwitchToMultiFilterMode() {
        DashboardMainFrame frame = new DashboardMainFrame(null);
        DashboardMainFrameController controller = new DashboardMainFrameController(frame, model);
        frame.setController(controller);
        TimeDataQuery query = new TimeQueryBuilder(MetricType.TOTAL_CLICKS).build();
        SwingUtilities.invokeLater(() -> {
            frame.init();
            // Check we don't already have a DashboardMultiController
        });
    }

}
