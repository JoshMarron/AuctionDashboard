package Tests;

import Controllers.ProjectSettings;
import Controllers.Queries.AttributeDataQuery;
import Controllers.Queries.AttributeQueryBuilder;
import Controllers.Results.AttributeQueryResult;
import Model.DBEnums.LogType;
import Model.DBEnums.attributes.Attribute;
import Model.DatabaseManager;
import Views.MetricType;
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

        model.initTables();

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
    public void testGetImpressionsByDifferentAttributes() {
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.TOTAL_IMPRESSIONS, AttributeType.AGE).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        //We expect 4 entries because all 4 users have different ages
        Assert.assertEquals(result.getData().size(), 4);

        //Test some of the actual values
        Assert.assertEquals(1, result.getData().get("<25").intValue());
        Assert.assertEquals(1, result.getData().get("25-34").intValue());
    }

    @Test
    public void testGetImpressionsByMatchingAttributes() {
        Map<String, Number> testMap = model.getTotalImpressionsForAttribute(AttributeType.GENDER);
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.TOTAL_IMPRESSIONS, AttributeType.GENDER).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        //We expect to see two genders
        Assert.assertEquals(result.getData().size(), 2);

        Assert.assertEquals(2, result.getData().get("Male").intValue());
        Assert.assertEquals(2, result.getData().get("Female").intValue());
    }

    @Test
    public void testGetClicksByDifferentAttributes() {
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.TOTAL_CLICKS, AttributeType.AGE).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        //There are only 2 clicks and both users have different ages
        Assert.assertEquals(2, result.getData().size());

        Assert.assertEquals(1, result.getData().get("25-34").intValue());
        Assert.assertEquals(1, result.getData().get("35-44").intValue());
    }

    @Test
    public void testGetClicksByMatchingAttributes() {
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.TOTAL_CLICKS, AttributeType.GENDER).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        //Both users who clicked have the same gender
        Assert.assertEquals(1, result.getData().size());
        Assert.assertEquals(2, result.getData().get("Male").intValue());
        //No female users clicked so we should get a null result
        Assert.assertNull(result.getData().get("Female"));
    }

    @Test
    public void testGetUniqueClicksByMatchingAttributes() {
        String[] clickDummy3 = {"2015-01-01 12:00:00", "1", "1200"};
        List<String[]> moreData = new ArrayList<>();
        moreData.add(clickDummy3);
        model.insertData(LogType.CLICK, moreData);

        Map<String, Number> testMap = model.getTotalUniquesForAttribute(AttributeType.GENDER);
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.TOTAL_UNIQUES, AttributeType.GENDER).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(1, result.getData().size());
        //One user clicks twice so this should give us a different unique value
        Assert.assertEquals(2, result.getData().get("Male").intValue());
    }

    @Test
    public void testGetUniqueClicksByDifferentAttributes() {
        String[] clickDummy3 = {"2015-01-01 12:00:00", "1", "1200"};
        List<String[]> moreData = new ArrayList<>();
        moreData.add(clickDummy3);
        model.insertData(LogType.CLICK, moreData);

        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.TOTAL_UNIQUES, AttributeType.CONTEXT).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(2, result.getData().size());

        Assert.assertEquals(1, result.getData().get("Blog").intValue());
        Assert.assertEquals(1, result.getData().get("Social Media").intValue());
    }

    @Test
    public void testGetConversionsByMatchingAttribute() {
        Map<String, Number> testMap = model.getTotalConversionsForAttribute(AttributeType.CONTEXT);
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.TOTAL_CONVERSIONS, AttributeType.CONTEXT).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(1, result.getData().size());

        Assert.assertEquals(2, result.getData().get("Social Media").intValue());
        Assert.assertNull(testMap.get("Blog"));
    }

    @Test
    public void testGetConversionsByDifferentAttributes() {
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.TOTAL_CONVERSIONS, AttributeType.AGE).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(2, result.getData().size());
        Assert.assertEquals(1, result.getData().get(">55").intValue());
        Assert.assertEquals(1, result.getData().get("35-44").intValue());
        Assert.assertNull(result.getData().get("<25"));
    }

    @Test
    public void testGetBouncesByMatchingAttribute() {
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.TOTAL_BOUNCES, AttributeType.INCOME).build();

        ProjectSettings.setBouncePages(1);
        ProjectSettings.setBounceSeconds(525600);

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(1, result.getData().size());
        Assert.assertEquals(2, result.getData().get("High").intValue());
        Assert.assertNull(result.getData().get("Low"));
    }

    @Test
    public void testGetBouncesByDifferentAttributes() {
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.TOTAL_BOUNCES, AttributeType.AGE).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(2, result.getData().size());
        Assert.assertEquals(1, result.getData().get("<25").intValue());
        Assert.assertEquals(1, result.getData().get("25-34").intValue());
    }

    @Test
    public void testGetTotalCostForAttribute() {
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.TOTAL_COST, AttributeType.GENDER).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);
        System.out.println(result.getData());
        Assert.assertEquals(2, result.getData().size());

        //Sum up both click and impression cost across genders
        Assert.assertEquals(4400, result.getData().get("Male").intValue());
        Assert.assertEquals(1200, result.getData().get("Female").intValue());

        query = new AttributeQueryBuilder(MetricType.TOTAL_COST, AttributeType.AGE).build();

        result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(4, result.getData().size());

        Assert.assertEquals(1400, result.getData().get("25-34").intValue());
        Assert.assertEquals(3000, result.getData().get("35-44").intValue());
        Assert.assertEquals(800, result.getData().get(">55").intValue());
    }

    @Test
    public void testGetCTRForAttribute() {
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.CTR, AttributeType.GENDER).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(2, result.getData().size());

        //2 clicks divided by 2 impressions
        Assert.assertEquals(1.0, result.getData().get("Male").doubleValue(), 0.00001);
        Assert.assertEquals(0.0, result.getData().get("Female").doubleValue(), 0.00001);

        query = new AttributeQueryBuilder(MetricType.CTR, AttributeType.AGE).build();

        result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(4, result.getData().size());

        Assert.assertEquals(1.0, result.getData().get("25-34").doubleValue(), 0.00001);
        Assert.assertEquals(0.0, result.getData().get("<25").doubleValue(), 0.00001);
    }

    @Test
    public void testGetCPAForAttribute() {
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.CPA, AttributeType.CONTEXT).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);


        Assert.assertEquals(3, result.getData().size());

        Assert.assertEquals(1900.0, result.getData().get("Social Media").doubleValue(), 0.00001);
        Assert.assertEquals(0.0, result.getData().get("Blog").doubleValue(), 0.00001);

        query = new AttributeQueryBuilder(MetricType.CPA, AttributeType.INCOME).build();

        result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(3, result.getData().size());
        Assert.assertEquals(3000.0, result.getData().get("Low").doubleValue(), 0.00001);
        Assert.assertEquals(0.0, result.getData().get("High").doubleValue(), 0.00001);
    }

    @Test
    public void testGetCPCForAttribute() {
        Map<String, Number> testMap = model.getCPCForAttribute(AttributeType.GENDER);
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.CPC, AttributeType.GENDER).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(2, result.getData().size());

        Assert.assertEquals(2200.0, result.getData().get("Male").doubleValue(), 0.00001);
        Assert.assertEquals(0.0, result.getData().get("Female").doubleValue(), 0.00001);

        query = new AttributeQueryBuilder(MetricType.CPC, AttributeType.CONTEXT).build();

        result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(3, result.getData().size());

        Assert.assertEquals(1400.0, result.getData().get("Blog").doubleValue(), 0.00001);
        Assert.assertEquals(3800.0, result.getData().get("Social Media").doubleValue(), 0.00001);
        Assert.assertEquals(0.0, result.getData().get("Fashion").doubleValue(), 0.00001);
    }

    @Test
    public void testGetCPMForAttribute() {
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.CPM, AttributeType.GENDER).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(2, result.getData().size());
        Assert.assertEquals(2200000.0, result.getData().get("Male").doubleValue(), 0.00001);
        Assert.assertEquals(600000.0, result.getData().get("Female").doubleValue(), 0.00001);

        query = new AttributeQueryBuilder(MetricType.CPM, AttributeType.INCOME).build();

        result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(3, result.getData().size());
        Assert.assertEquals(900000.0, result.getData().get("High").doubleValue(), 0.00001);
        Assert.assertEquals(3000000.0, result.getData().get("Low").doubleValue(), 0.00001);
    }

    @Test
    public void testGetBounceRateForAttribute() {
        AttributeDataQuery query = new AttributeQueryBuilder(MetricType.BOUNCE_RATE, AttributeType.GENDER).build();

        AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(2, result.getData().size());
        Assert.assertEquals(0.5, result.getData().get("Male").doubleValue(), 0.00001);
        Assert.assertEquals(1.0, result.getData().get("Female").doubleValue(), 0.00001);

        query = new AttributeQueryBuilder(MetricType.BOUNCE_RATE, AttributeType.INCOME).build();

        result = (AttributeQueryResult) model.resolveQuery(query);

        Assert.assertEquals(2, result.getData().size());
        Assert.assertEquals(2.0, result.getData().get("High").doubleValue(), 0.00001);
        Assert.assertEquals(0.0, result.getData().get("Low").doubleValue(), 0.00001);
    }
}
