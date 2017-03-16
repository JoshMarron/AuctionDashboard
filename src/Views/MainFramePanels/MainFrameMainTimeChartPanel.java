package Views.MainFramePanels;

import Model.DBEnums.DateEnum;
import Views.CustomComponents.CatPanel;
import Views.MetricType;

import java.time.Instant;
import java.util.Map;

/**
 * MainFrameMainTimeChartPanel is an abstract superclass for all the charts that can be displayed on the main panel
 */
abstract class MainFrameMainTimeChartPanel extends CatPanel {

    abstract public void displayChart(MetricType type, DateEnum granularity, Map<Instant, Number> data);
}
