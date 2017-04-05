package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.util.Map;

/**
 * Created by Joshua on 04/04/2017.
 */
public abstract class Query {

    private MetricType metric;
    private Map<AttributeType, String> filters;

    public Query(QueryBuilder b) {
        this.metric = metric;
    }

    public MetricType getMetric() {
        return this.metric;
    }
}
