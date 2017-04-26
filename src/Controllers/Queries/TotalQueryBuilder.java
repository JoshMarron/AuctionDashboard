package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.util.List;
import java.util.Map;

/**
 * Created by marro on 26/04/2017.
 */
public class TotalQueryBuilder extends QueryBuilder {

    public TotalQueryBuilder(MetricType metric) {
        super(metric);
    }

    public TotalQueryBuilder filters(Map<AttributeType, List<String>> filters) {
        this.setFilters(filters);
        return this;
    }

    @Override
    public Query build() {
        return new TotalQuery(this);
    }
}
