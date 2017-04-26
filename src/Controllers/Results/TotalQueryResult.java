package Controllers.Results;

import Views.MetricType;

import java.time.Instant;

/**
 * Created by marro on 26/04/2017.
 */
public class TotalQueryResult extends QueryResult {

    private Number data;

    public TotalQueryResult(MetricType metric, Number data) {
        super(metric);
        this.data = data;
    }

    public Number getData() {
        return data;
    }
}
