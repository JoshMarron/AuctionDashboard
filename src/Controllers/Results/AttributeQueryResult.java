package Controllers.Results;

import Views.MetricType;

import java.util.Map;

/**
 * Created by marro on 24/04/2017.
 */
public class AttributeQueryResult extends QueryResult {

    private Map<String, Number> data;

    public AttributeQueryResult(MetricType metric, Map<String, Number> data) {
        super(metric);
        this.data = data;
    }

    public Map<String, Number> getData() {
        return data;
    }
}
