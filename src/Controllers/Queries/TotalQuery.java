package Controllers.Queries;

import Views.MetricType;

import java.time.Instant;

/**
 * Created by marro on 26/04/2017.
 */
public class TotalQuery extends Query {

    public TotalQuery(TotalQueryBuilder b) {
        super(b);
    }

    public TotalQuery deriveQuery(MetricType metric) {
        TotalQueryBuilder newBuilder = new TotalQueryBuilder(metric);
        return newBuilder.startDate(this.getStartDate())
                .endDate(this.getEndDate())
                .filters(this.getFilters())
                .build();
    }
}
