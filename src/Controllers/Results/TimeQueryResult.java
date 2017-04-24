package Controllers.Results;

import Views.MetricType;

import java.time.Instant;
import java.util.Map;

/**
 * Created by marro on 24/04/2017.
 */
public class TimeQueryResult extends QueryResult {

    private Map<Instant, Number> data;

    public TimeQueryResult(MetricType metric, Map<Instant, Number> data) {
        super(metric);
        this.data = data;
    }

    public Map<Instant, Number> getData() {
        return data;
    }

}
