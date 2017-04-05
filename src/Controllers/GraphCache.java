package Controllers;

import Controllers.Queries.AttributeDataQuery;
import Controllers.Queries.TimeQueryBuilder;
import Controllers.Queries.TimeDataQuery;
import Controllers.Queries.Query;
import Model.DBEnums.DateEnum;
import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GraphCache is an object which contains a multithreaded map of data - this can be queried before making a lengthy
 * SQL query
 */
public class GraphCache {

    private ConcurrentHashMap<Query, Map<Instant, Number>> timeCache;
    private ConcurrentHashMap<Query, Map<String, Number>> attributeCache;

    public GraphCache() {
        timeCache = new ConcurrentHashMap<>();
        attributeCache = new ConcurrentHashMap<>();
    }

    /**
     * This MUST be run on a new thread or it'll completely consume the resources
     */
    public void startCaching() {
        for (MetricType metric: MetricType.values()) {
            for (DateEnum gran: DateEnum.values()) {
                TimeQueryBuilder builder = new TimeQueryBuilder(metric);
                TimeDataQuery query = builder.granularity(gran).build();
            }
        }

        for (AttributeType attr: AttributeType.values()) {
            for (MetricType metric: MetricType.values()) {
                AttributeDataQuery query = TimeQueryBuilder.createAttributeDataQuery(metric, attr);
                //TODO process
            }
        }
    }

    public void addToCache(TimeDataQuery query, Map<Instant, Number> data) {
        this.timeCache.put(query, data);
    }

    public void addToCache(AttributeDataQuery query, Map<String, Number> data) {
        this.attributeCache.put(query, data);
    }

    public boolean isInCache(Query query) {
        return (attributeCache.containsKey(query) || timeCache.containsKey(query));
    }
}
