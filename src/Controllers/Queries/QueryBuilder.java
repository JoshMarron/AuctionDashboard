package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joshua on 05/04/2017.
 */
public abstract class QueryBuilder {

    private MetricType metric;

    public QueryBuilder(MetricType metric) {
        this.metric = metric;
    }

    public abstract Query build();

    public MetricType getMetric() {
        return this.metric;
    }
}
