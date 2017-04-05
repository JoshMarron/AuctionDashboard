package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

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
}
