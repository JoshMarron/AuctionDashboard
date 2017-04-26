package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Abstract class to represent data shared by all queries
 */
public abstract class Query {

    private MetricType metric;
    private Map<AttributeType, List<String>> filters;
    private Instant startDate;
    private Instant endDate;

    public Query(QueryBuilder b) {
        this.metric = b.getMetric();
        this.filters = b.getFilters();
        this.startDate = b.getStartDate();
        this.endDate = b.getEndDate();
    }

    public MetricType getMetric() {
        return this.metric;
    }

    public Map<AttributeType, List<String>> getFilters() {
        return this.filters;
    }

    @Override
    public int hashCode() {
        return metric.hashCode() + filters.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Query)) {
            return false;
        }

        return this.filters.equals(((Query) o).getFilters()) && this.metric.equals(((Query) o).getMetric());
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }
}
