package Controllers.Queries;

import Controllers.ProjectSettings;
import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joshua on 05/04/2017.
 */
public abstract class QueryBuilder {

    private MetricType metric;
    private Map<AttributeType, List<String>> filters;
    private Instant startDate;
    private Instant endDate;

    public QueryBuilder(MetricType metric) {
        this.metric = metric;
        this.filters = new HashMap<>();
        this.startDate = ProjectSettings.MIN_DATE;
        this.endDate = ProjectSettings.MAX_DATE;
    }

    public abstract Query build();

    public MetricType getMetric() {
        return this.metric;
    }

    public void setFilters(Map<AttributeType, List<String>> filters) {
        this.filters = filters;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Map<AttributeType, List<String>> getFilters() {
        return this.filters;
    }
}
