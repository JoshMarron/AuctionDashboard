package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joshua on 05/04/2017.
 */
public abstract class QueryBuilder {

    private MetricType metric;
    private Map<AttributeType, String> filters;

    public QueryBuilder(MetricType metric) {
        this.metric = metric;
        this.filters = new HashMap<>();
    }

    public abstract Query build();

    public MetricType getMetric() {
        return this.metric;
    }

    public Map<AttributeType, String> getFilters() {
        return this.filters;
    }

    public QueryBuilder filters(Map<AttributeType, String> filters) {
        this.filters = filters;
        return this;
    }
}
