package Controllers.Results;

import Views.MetricType;

/**
 * Created by marro on 24/04/2017.
 */
public abstract class QueryResult {

    private MetricType metric;

    public QueryResult(MetricType metric) {
        this.metric = metric;
    }

    public MetricType getMetric() {
        return this.metric;
    }
}
