package Model;

import Controllers.Queries.AttributeDataQuery;
import Controllers.Queries.AttributeQueryBuilder;
import Controllers.Queries.TimeDataQuery;
import Controllers.Queries.TimeQueryBuilder;
import Controllers.Results.AttributeQueryResult;
import Controllers.Results.TimeQueryResult;
import Model.DBEnums.DateEnum;
import Model.DBEnums.attributes.AgeAttribute;
import Model.DBEnums.attributes.Attribute;
import Model.DBEnums.attributes.IncomeAttribute;
import Views.MetricType;
import Views.ViewPresets.AttributeType;
import org.w3c.dom.Attr;

import javax.xml.crypto.Data;
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
        try {
            model.loadDB("db/Project.cat");
        } catch (SQLException | CorruptTableException e) {
            e.printStackTrace();
        }
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

		AttributeDataQuery attQ = new AttributeQueryBuilder(MetricType.TOTAL_UNIQUES, AttributeType.GENDER).filters(map).build();
		AttributeQueryResult result = (AttributeQueryResult) model.resolveQuery(attQ);
		System.out.println(result.getData());
	}
}
