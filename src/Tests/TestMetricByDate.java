package Tests;


import Controllers.Queries.TimeDataQuery;
import Controllers.Queries.TimeQueryBuilder;
import Controllers.Results.TimeQueryResult;
import Model.DBEnums.DateEnum;
import Model.DBEnums.LogType;
import Model.DatabaseManager;
import Views.MetricType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.sun.xml.internal.ws.dump.LoggingDumpTube.Position.Before;

public class TestMetricByDate {
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

    @Test
    public void testImpressionsPerDay() {
        TimeDataQuery query = new TimeQueryBuilder(MetricType.TOTAL_IMPRESSIONS).build();
        testMap = ((TimeQueryResult) model.resolveQuery(query)).getData();

        Assert.assertEquals(3, testMap.size());

        Assert.assertEquals(2, testMap.get(day1).intValue());
        Assert.assertEquals(1, testMap.get(day2).intValue());
        Assert.assertEquals(1, testMap.get(day3).intValue());
    }

    @Test
    public void testClicksPerDay() {
        TimeDataQuery query = new TimeQueryBuilder(MetricType.TOTAL_CLICKS).build();
        testMap = ((TimeQueryResult) model.resolveQuery(query)).getData();

        Assert.assertEquals(2, testMap.size());

        Assert.assertEquals(1, testMap.get(day1).intValue());
        Assert.assertEquals(1, testMap.get(day2).intValue());
    }

    @Test
    public void testConversionsPerDay() {
        TimeDataQuery query = new TimeQueryBuilder(MetricType.TOTAL_CONVERSIONS).build();
        testMap = ((TimeQueryResult) model.resolveQuery(query)).getData();

        Assert.assertEquals(2, testMap.size());

        Assert.assertEquals(1, testMap.get(day2).intValue());
        Assert.assertEquals(1, testMap.get(day5).intValue());
    }

    @Test
    public void testUniquesPerDay() {
        String[] clickDummy3 = {"2015-01-01 12:00:00", "1", "1200"};
        List<String[]> moreData = new ArrayList<>();
        moreData.add(clickDummy3);
        model.insertData(LogType.CLICK, moreData);

        TimeDataQuery query = new TimeQueryBuilder(MetricType.TOTAL_UNIQUES).build();
        testMap = ((TimeQueryResult) model.resolveQuery(query)).getData();

        Assert.assertEquals(2, testMap.size());

        Assert.assertEquals(1, testMap.get(day1).intValue());
        Assert.assertEquals(1, testMap.get(day2).intValue());
    }

    @Test
    public void testBouncesPerDay() {
        TimeDataQuery query = new TimeQueryBuilder(MetricType.TOTAL_BOUNCES).build();
        testMap = ((TimeQueryResult) model.resolveQuery(query)).getData();

        Assert.assertEquals(2, testMap.size());

        Assert.assertEquals(1, testMap.get(day4).intValue());
        Assert.assertEquals(1, testMap.get(day1).intValue());
    }

    @Test
    public void testTotalCostPerDay() {
        TimeDataQuery query = new TimeQueryBuilder(MetricType.TOTAL_COST).build();
        testMap = ((TimeQueryResult) model.resolveQuery(query)).getData();
        System.out.println(testMap);
        Assert.assertEquals(3, testMap.size());

        Assert.assertEquals(1800.0, testMap.get(day1).doubleValue(), 0.00001);
        Assert.assertEquals(3000.0, testMap.get(day2).doubleValue(), 0.00001);
        Assert.assertEquals(800.0, testMap.get(day3).doubleValue(), 0.00001);
    }

}
