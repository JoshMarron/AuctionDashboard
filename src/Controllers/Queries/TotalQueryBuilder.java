package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Created by marro on 26/04/2017.
 */
public class TotalQueryBuilder extends QueryBuilder {

    private Instant startDate;
    private Instant endDate;

    public TotalQueryBuilder(MetricType metric) {
        super(metric);
    }

    public TotalQueryBuilder filters(Map<AttributeType, List<String>> filters) {
        this.setFilters(filters);
        return this;
    }

    public TotalQueryBuilder startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public TotalQueryBuilder endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    @Override
    public Query build() {
        return new TotalQuery(this);
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }
}
