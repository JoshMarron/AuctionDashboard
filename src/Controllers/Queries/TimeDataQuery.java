package Controllers.Queries;

import Model.DBEnums.DateEnum;
import Model.DBEnums.attributes.Attribute;
import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Class which contains a a query for a series of data in terms of date.
 */
public class TimeDataQuery extends Query {

    private DateEnum granularity;

    public TimeDataQuery(TimeQueryBuilder b) {
        super(b);
        this.granularity = b.getGranularity();
    }

    public DateEnum getGranularity() {
        return granularity;
    }

    public TimeDataQuery deriveQuery(MetricType metric) {
        TimeQueryBuilder newBuilder = new TimeQueryBuilder(metric);
        TimeDataQuery newQuery = newBuilder.granularity(this.granularity)
                .startDate(this.getStartDate())
                .endDate(this.getEndDate())
                .filters(this.getFilters())
                .build();

        return newQuery;

    }
}
