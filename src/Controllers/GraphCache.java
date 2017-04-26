package Controllers;

import Controllers.Queries.*;
import Controllers.Results.QueryResult;
import Controllers.Results.TimeQueryResult;
import Model.DBEnums.DateEnum;
import Model.DatabaseManager;
import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GraphCache is an object which contains a multithreaded map of data - this can be queried before making a lengthy
 * SQL query
 */
public class GraphCache {

    private Map<Query, QueryResult> cacheMap;
    private DatabaseManager model;

    public GraphCache(DatabaseManager model) {
        cacheMap = new HashMap<>();
        this.model = model;
    }

    /**
     * This MUST be run on a new thread or it'll completely consume the resources
     */
    public void startCaching() {
        for (MetricType metric: MetricType.values()) {
            for (DateEnum gran: DateEnum.values()) {
                TimeQueryBuilder builder = new TimeQueryBuilder(metric);
                TimeDataQuery query = builder.granularity(gran).build();

                System.out.println(query);
                QueryResult result = model.resolveQuery(query);
                System.out.println("Resolved");
                cacheMap.put(query, result);
                System.out.println(cacheMap);
            }
        }

        /*for (AttributeType attr: AttributeType.values()) {
            for (MetricType metric: MetricType.values()) {
                AttributeQueryBuilder builder = new AttributeQueryBuilder(metric, attr);
                AttributeDataQuery query = builder.build();
                // TODO resolve
            }
        }*/
    }

    public void addToCache(Query query, QueryResult result) {
        this.cacheMap.put(query, result);
    }

    public boolean isInCache(Query query) {
        return cacheMap.containsKey(query);
    }

    public QueryResult hitCache(Query query) {
        return cacheMap.get(query);
    }
}
