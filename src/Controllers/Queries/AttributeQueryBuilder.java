package Controllers.Queries;

import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.util.Map;

/**
 * Created by Joshua on 05/04/2017.
 */
public class AttributeQueryBuilder extends QueryBuilder {

    private AttributeType attr;

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
}
