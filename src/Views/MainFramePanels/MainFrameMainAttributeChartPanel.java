package Views.MainFramePanels;

import Views.CustomComponents.CatPanel;
import Views.MetricType;
import Views.ViewPresets.AttributeType;

import java.util.Map;

/**
 * MainFrameMainAttributeChartPanel is an abstract superclass for charts which display a metric per attribute
 */
abstract public class MainFrameMainAttributeChartPanel extends CatPanel {

    abstract public void displayChart(MetricType type, AttributeType attr, Map<String, Number> data);
    abstract public void displayDoubleChart(MetricType type, AttributeType attr, Map<String, Number> data1, Map<String, Number> data2);
}
