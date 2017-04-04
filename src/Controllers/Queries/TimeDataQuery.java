package Controllers.Queries;

import Model.DBEnums.DateEnum;
import Views.MetricType;

import java.time.Instant;

/**
 * Class which contains a a query for a series of data in terms of date.
 */
public class TimeDataQuery extends Query {

    private DateEnum granularity;
    private Instant startDate;
    private Instant endDate;

    public TimeDataQuery(MetricType metric, DateEnum granularity, Instant startDate, Instant endDate) {
        super(metric);
        this.granularity = granularity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateEnum getGranularity() {
        return granularity;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }
}
