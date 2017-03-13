package Views.MainFramePanels;

import Views.CustomComponents.CatPanel;
import Views.CustomComponents.CatTitlePanel;
import Views.DashboardMainFrame;
import Views.MetricType;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * MainFrameMetricList displays a list of metric names and values
 * These should be clickable, allowing a user to click them to view the associated graphs
 */
public class MainFrameMetricList extends CatPanel {

    private DashboardMainFrame parentFrame;
    private Map<MetricType, MainFrameMetricBox> metricBoxMap;

    public MainFrameMetricList(DashboardMainFrame parentFrame) {
        this.parentFrame = parentFrame;
        metricBoxMap = new HashMap<>();
        initMetricList();
    }

    private void initMetricList() {
        this.setLayout(new BorderLayout());
        Border outside = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        Border inside = BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor());
        this.setBorder(BorderFactory.createCompoundBorder(outside, inside));
        CatTitlePanel metricListTitle = new CatTitlePanel("Campaign Metrics");
        metricListTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        this.add(metricListTitle, BorderLayout.NORTH);

        CatPanel metricList = new CatPanel();
        metricList.setBorder(BorderFactory.createEmptyBorder());
        metricList.setLayout(new BoxLayout(metricList, BoxLayout.Y_AXIS));

        for (MetricType metric: MetricType.values()) {
            MainFrameMetricBox metricBox = new MainFrameMetricBox(metric, this);
            metricBoxMap.put(metric, metricBox);
            metricList.add(metricBox);
        }

        this.add(metricList, BorderLayout.CENTER);
    }

    public void putMetricInBox(MetricType type, Number data) {
        metricBoxMap.get(type).updateData(data);
    }

    public void requestChart(MetricType type) {
        parentFrame.requestChart(type);
    }
}
