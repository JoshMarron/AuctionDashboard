package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Created by Joshua on 05/04/2017.
 */
public class AttributeQueryBuilder extends QueryBuilder {

    private AttributeType attr;
    private Instant startDate;
    private Instant endDate;

    public AttributeQueryBuilder(MetricType metric, AttributeType attr) {
        super(metric);
        this.attr = attr;
    }

    @Override
    public AttributeDataQuery build() {
        return new AttributeDataQuery(this);
    }

    public AttributeType getAttribute() {
        return this.attr;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public AttributeQueryBuilder startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public AttributeQueryBuilder endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public AttributeQueryBuilder filters(Map<AttributeType, List<String>> filters) {
        this.setFilters(filters);
        return this;
    }
}
