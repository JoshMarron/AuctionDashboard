package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marro on 25/04/2017.
 */
public class TestMain {

    public static void main(String[] args) {
        Map<AttributeType, List<String>> map = new HashMap<>();
        map.put(AttributeType.CONTEXT, Arrays.asList("Social Media", "Farts"));
        AttributeDataQuery query1 = new AttributeQueryBuilder(MetricType.CPA, AttributeType.GENDER).filters(map).build();

        Map<AttributeType, List<String>> map2 = new HashMap<>();
        map2.put(AttributeType.CONTEXT, Arrays.asList("Social Media"));
        Query query2 = new AttributeQueryBuilder(MetricType.CPA, AttributeType.GENDER).filters(map2).build();

        System.out.println(query1.equals(query2));

        System.out.println(query1.hashCode());
        System.out.println(query2.hashCode());

        Query query3 = new TimeQueryBuilder(MetricType.CPA).filters(map).build();

        Map<Query, String> queryMap = new HashMap<>();
        queryMap.put(query1, "bloop");
        queryMap.put(query2, "badoop");
        queryMap.put(query3, "badoop poop");

        System.out.println(queryMap);
    }
}
