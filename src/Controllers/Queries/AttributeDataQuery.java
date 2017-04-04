package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

/**
 * A query object for a query on an attribute
 */
public class AttributeDataQuery extends Query{

    private AttributeType attribute;

    public AttributeDataQuery(MetricType metric, AttributeType attribute) {
        super(metric);
        this.attribute = attribute;
    }

    public MetricType getMetric() {
        return metric;
    }

    public AttributeType getAttribute() {
        return attribute;
    }
}
