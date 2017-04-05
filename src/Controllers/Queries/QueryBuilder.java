package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.util.Map;

/**
 * Created by Joshua on 05/04/2017.
 */
public interface QueryBuilder {

    public Query build();
    public MetricType getMetric();
    public Map<AttributeType, String> getFilters();
}
