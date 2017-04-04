package Controllers.Queries;

import Model.DBEnums.DateEnum;
import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.time.Instant;

/**
 * Creates query objects for the cache to handlee
 */
public class QueryFactory {

    public static TimeDataQuery createTimeDataQuery(MetricType metric, DateEnum granularity, Instant startDate, Instant endDate) {
        return new TimeDataQuery(metric, granularity, startDate, endDate);
    }

    public static TimeDataQuery createTimeDataQuery(MetricType metric, DateEnum granularity) {
        return new TimeDataQuery(metric, granularity, Instant.MIN, Instant.MAX);
    }

    public static AttributeDataQuery createAttributeDataQuery(MetricType metric, AttributeType attr) {
        return new AttributeDataQuery(metric, attr);
    }
}
