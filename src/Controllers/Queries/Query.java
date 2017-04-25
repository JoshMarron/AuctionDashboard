package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.util.List;
import java.util.Map;

/**
 * Abstract class to represent data shared by all queries
 */
public abstract class Query {

    private MetricType metric;

    public Query(QueryBuilder b) {
        this.metric = b.getMetric();
    }

    public MetricType getMetric() {
        return this.metric;
    }

}
