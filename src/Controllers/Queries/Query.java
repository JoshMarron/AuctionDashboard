package Controllers.Queries;

import Views.MetricType;

/**
 * Created by Joshua on 04/04/2017.
 */
public abstract class Query {

    MetricType metric;

    public Query(MetricType metric) {
        this.metric = metric;
    }

    public MetricType getMetric() {
        return this.metric;
    }

    public void setMetric(MetricType metric) {
        this.metric = metric;
    }
}
