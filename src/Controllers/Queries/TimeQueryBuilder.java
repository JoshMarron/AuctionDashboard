package Controllers.Queries;

import Model.DBEnums.DateEnum;
import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.time.Instant;
import java.util.Map;

/**
 * Creates query objects for the cache to handle
 */
public class TimeQueryBuilder implements QueryBuilder {

    private MetricType metric;
    private DateEnum granularity;
    private Instant startDate;
    private Instant endDate;
    private Map<AttributeType, String> filters;

    public TimeQueryBuilder(MetricType metric) {
        this.metric = metric;
        //Set the default values of optional parameters
        this.granularity = DateEnum.DAYS;
        this.startDate = Instant.MIN;
        this.endDate = Instant.MAX;
        this.filters = null;
    }

    public TimeDataQuery build() {
        return new TimeDataQuery(this);
    }

    public TimeQueryBuilder granularity(DateEnum granularity) {
        this.granularity = granularity;
        return this;
    }

    public TimeQueryBuilder startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public TimeQueryBuilder endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public TimeQueryBuilder filters(Map<AttributeType, String> filters) {
        this.filters = filters;
        return this;
    }

    public MetricType getMetric() {
        return metric;
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

    public Map<AttributeType, String> getFilters() {
        return filters;
    }
}
