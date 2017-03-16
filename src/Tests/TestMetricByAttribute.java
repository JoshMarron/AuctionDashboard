package Tests;

import Model.DBEnums.LogType;
import Model.DatabaseManager;
import Views.ViewPresets.AttributeType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestMetricByAttribute {

    DatabaseManager model;
    File testDB;

    @Before
    public void setUp() {

        testDB = new File("db/testdb");
        model = new DatabaseManager();
        model.createDB(testDB.getAbsolutePath());

        String[] dummyImpression1 = {"2015-01-01 12:00:00", "1", "Male", "25-34", "High", "Blog", "200"};
        String[] dummyImpression2 = {"2015-01-01 14:00:00", "2", "Female", "<25", "Low", "Fashion", "400"};
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
    public void testGetImpressionsByDifferentAttributes() {
        Map<String, Number> testMap = model.getTotalImpressionsForAttribute(AttributeType.AGE);

        //We expect 4 entries because all 4 users have different ages
        Assert.assertEquals(testMap.size(), 4);

        //Test some of the actual values
        Assert.assertEquals(1, testMap.get("<25").intValue());
        Assert.assertEquals(1, testMap.get("25-34").intValue());
    }

    @Test
    public void testGetImpressionsByMatchingAttributes() {
        Map<String, Number> testMap = model.getTotalImpressionsForAttribute(AttributeType.GENDER);

        //We expect to see two genders
        Assert.assertEquals(testMap.size(), 2);

        Assert.assertEquals(2, testMap.get("Male").intValue());
        Assert.assertEquals(2, testMap.get("Female").intValue());
    }

    @Test
    public void testGetClicksByDifferentAttributes() {
        Map<String, Number> testMap = model.getTotalClicksForAttribute(AttributeType.AGE);

        //There are only 2 clicks and both users have different ages
        Assert.assertEquals(2, testMap.size());

        Assert.assertEquals(1, testMap.get("25-34").intValue());
        Assert.assertEquals(1, testMap.get("35-44").intValue());
    }

    @Test
    public void testGetClicksByMatchingAttributes() {
        Map<String, Number> testMap = model.getTotalClicksForAttribute(AttributeType.GENDER);

        //Both users who clicked have the same gender
        Assert.assertEquals(1, testMap.size());

        Assert.assertEquals(2, testMap.get("Male").intValue());
        //No female users clicked so we should get a null result
        Assert.assertNull(testMap.get("Female"));
    }

    @Test
    public void testGetUniqueClicksByMatchingAttributes() {
        String[] clickDummy3 = {"2015-01-01 12:00:00", "1", "1200"};
        List<String[]> moreData = new ArrayList<>();
        moreData.add(clickDummy3);
        model.insertData(LogType.CLICK, moreData);

        Map<String, Number> testMap = model.getTotalUniquesForAttribute(AttributeType.GENDER);

        Assert.assertEquals(1, testMap.size());
        //One user clicks twice so this should give us a different unique value
        Assert.assertEquals(2, testMap.get("Male").intValue());
    }

    @Test
    public void testGetUniqueClicksByDifferentAttributes() {
        String[] clickDummy3 = {"2015-01-01 12:00:00", "1", "1200"};
        List<String[]> moreData = new ArrayList<>();
        moreData.add(clickDummy3);
        model.insertData(LogType.CLICK, moreData);

        Map<String, Number> testMap = model.getTotalUniquesForAttribute(AttributeType.CONTEXT);

        Assert.assertEquals(2, testMap.size());

        Assert.assertEquals(1, testMap.get("Blog").intValue());
        Assert.assertEquals(1, testMap.get("Social Media").intValue());
    }

    @Test
    public void testGetConversionsByMatchingAttribute() {
        Map<String, Number> testMap = model.getTotalConversionsForAttribute(AttributeType.CONTEXT);

        Assert.assertEquals(1, testMap.size());

        Assert.assertEquals(2, testMap.get("Social Media").intValue());
        Assert.assertNull(testMap.get("Blog"));
    }

    @Test
    public void testGetConversionsByDifferentAttributes() {
        Map<String, Number> testMap = model.getTotalConversionsForAttribute(AttributeType.AGE);

        Assert.assertEquals(2, testMap.size());
        Assert.assertEquals(1, testMap.get(">55").intValue());
        Assert.assertEquals(1, testMap.get("35-44").intValue());
        Assert.assertNull(testMap.get("<25"));
    }
}
