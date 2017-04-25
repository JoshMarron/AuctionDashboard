package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.time.Instant;

/**
 * A query object for a query on an attribute
 */
public class AttributeDataQuery extends Query{

    private AttributeType attribute;
    private Instant startDate;
    private Instant endDate;

    public AttributeDataQuery(AttributeQueryBuilder b) {
        super(b);
        this.attribute = b.getAttribute();
        this.startDate = b.getStartDate();
        this.endDate = b.getEndDate();
    }

    public AttributeType getAttribute() {
        return attribute;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }
}
