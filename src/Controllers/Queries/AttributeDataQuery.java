package Controllers.Queries;

import Model.DBEnums.attributes.Attribute;
import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.time.Instant;

/**
 * A query object for a query on an attribute
 */
public class AttributeDataQuery extends Query{

    private AttributeType attribute;

    public AttributeDataQuery(AttributeQueryBuilder b) {
        super(b);
        this.attribute = b.getAttribute();
    }

    public AttributeType getAttribute() {
        return attribute;
    }

    public AttributeDataQuery deriveQuery(MetricType metric, AttributeType attribute) {
        AttributeQueryBuilder newBuilder = new AttributeQueryBuilder(metric, attribute);
        return newBuilder.startDate(this.getStartDate())
                .endDate(this.getEndDate())
                .filters(this.getFilters())
                .build();
    }
}
