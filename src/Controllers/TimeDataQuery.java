package Controllers;

import Model.DBEnums.DateEnum;
import Views.MetricType;

import java.time.Instant;

/**
 * Created by Joshua on 04/04/2017.
 */
public class TimeDataQuery extends Query {

    private DateEnum granularity;
    private Instant startDate;
    private Instant endDate;

    public TimeDataQuery(MetricType metric, DateEnum granularity, Instant startDate, Instant endDate) {
        super(metric);
    }
}
