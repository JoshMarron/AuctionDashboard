package Model;

import Controllers.Queries.AttributeDataQuery;
import Controllers.Queries.AttributeQueryBuilder;
import Controllers.Queries.TimeDataQuery;
import Controllers.Queries.TimeQueryBuilder;
import Controllers.Results.AttributeQueryResult;
import Controllers.Results.TimeQueryResult;
import Model.DBEnums.DateEnum;
import Model.DBEnums.LogType;
import Model.DBEnums.attributes.AgeAttribute;
import Model.DBEnums.attributes.Attribute;
import Model.DBEnums.attributes.IncomeAttribute;
import Views.MetricType;
import Views.ViewPresets.AttributeType;
import org.w3c.dom.Attr;

import javax.xml.crypto.Data;
import java.io.File;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marro on 15/03/2017.
 */
public class DatabaseTestMain {

    public static void main(String[] args) {
        DatabaseManager model = new DatabaseManager();
		File testDB;
//        try {
//            model.loadDB("db/Project.cat");
//        } catch (SQLException | CorruptTableException e) {
//            e.printStackTrace();
//        }


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

//
//        Map<String, Number> testModel = model.getBounceRateForAttribute(AttributeType.CONTEXT);
//        Map<Instant, Number> testModel2 = model.getBounceRatePer(DateEnum.DAYS);
//
//        testModel.forEach((type, value) -> System.out.println(type + ": " + value));
//        testModel2.forEach((type, value) -> System.out.println(type + ": " + value));
//
//        System.out.println(testModel2.values().stream().reduce((a, b) -> a.doubleValue() + b.doubleValue()));

		HashMap<AttributeType, List<String>> map = new HashMap<>();
		map.put(AttributeType.GENDER, Arrays.asList("Male", "Female"));
		map.put(AttributeType.INCOME, Arrays.asList("Low"));
		map.put(AttributeType.AGE, Arrays.asList("25-34", "35-44"));
		map.put(AttributeType.CONTEXT, Arrays.asList("Social Media"));
		System.out.println("map: " + map);

		//TODO fix weeks working
		TimeDataQuery timeQ = new TimeQueryBuilder(MetricType.TOTAL_BOUNCES).filters(map).granularity(DateEnum.DAYS).build();
//		System.out.println("query: " + timeQ.getFilters());
//
//		System.out.println(model.setBetween(q, "click_date"));
//		System.out.println(model.setFilters(q));
//		System.out.println(model.timeGroup(q, "click_date"));

//		TimeQueryResult result = (TimeQueryResult) model.resolveQuery(timeQ);
//		System.out.println(result.getData());

		AttributeDataQuery attQ = new AttributeQueryBuilder(MetricType.TOTAL_CLICKS, AttributeType.GENDER).filters(map).build();
		AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(attQ);
		System.out.println(result.getData());
	}
}
