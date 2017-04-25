package Controllers.Queries;

import Controllers.ProjectSettings;
import Model.DBEnums.DateEnum;
import Model.DatabaseManager;
import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Creates query objects for the cache to handle
 */
public class TimeQueryBuilder extends QueryBuilder {

    private DateEnum granularity;
    private Instant startDate;
    private Instant endDate;

    public TimeQueryBuilder(MetricType metric) {
        super(metric);
        //Set the default values of optional parameters
        this.granularity = DateEnum.DAYS;
        this.startDate = ProjectSettings.MIN_DATE;
        this.endDate = ProjectSettings.MAX_DATE;
    }

    public TimeDataQuery build() {
        System.out.println("builder: " + this.getFilters());
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

    public DateEnum getGranularity() {
        return granularity;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public TimeQueryBuilder filters(Map<AttributeType, List<String>> filters) {
        this.setFilters(filters);
        return this;
    }

}
