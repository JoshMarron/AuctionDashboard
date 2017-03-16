package Tests;


import Model.DBEnums.LogType;
import Model.DatabaseManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestMetricGet {

    DatabaseManager model;
    File testDB;
    Number testVal;

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
    public void testGetImpressions() {
        testVal = model.getTotalImpressions();

        Assert.assertEquals(4, testVal.intValue());
    }

    @Test
    public void testGetClicks() {
        testVal = model.getTotalClicks();

        Assert.assertEquals(2, testVal.intValue());
    }

    @Test
    public void testGetUniques() {
        String[] clickDummy3 = {"2015-01-01 12:00:00", "1", "1200"};
        List<String[]> moreData = new ArrayList<>();
        moreData.add(clickDummy3);
        model.insertData(LogType.CLICK, moreData);

        testVal = model.getTotalUniques();

        Assert.assertEquals(2, testVal.intValue());
    }

    @Test
    public void testGetConversions() {
        testVal = model.getTotalConversions();

        Assert.assertEquals(2, testVal.intValue());
    }

    @Test
    public void testGetBounces() {
        testVal = model.getTotalBounces();

        Assert.assertEquals(2, testVal.intValue());
    }

    @Test
    public void testGetTotalCost() {
        testVal = model.getTotalCampaignCost();

        Assert.assertEquals(5600.0, testVal.doubleValue(), 0.00001);
    }

    @Test
    public void testGetCPA() {
        testVal = model.getCPA();

        Assert.assertEquals(2800.0, testVal.doubleValue(), 0.00001);
    }

    @Test
    public void testGetCPC() {
        testVal = model.getCPC();

        Assert.assertEquals(2800.0, testVal.doubleValue(), 0.00001);
    }

    @Test
    public void testGetCPM() {
        testVal = model.getCPM();

        Assert.assertEquals(1400000.0, testVal.doubleValue(), 0.00001);
    }

    @Test
    public void testGetCTR() {
        testVal = model.getCTR();

        Assert.assertEquals(0.5, testVal.doubleValue(), 0.00001);
    }

    @Test
    public void testGetBounceRate() {
        testVal = model.getBounceRate();

        Assert.assertEquals(1.0, testVal.doubleValue(), 0.00001);
    }
}
