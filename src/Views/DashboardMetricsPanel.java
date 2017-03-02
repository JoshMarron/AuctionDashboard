package Views;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * DashboardMetricsPanel is the GUI element responsible for displaying a list of metrics computed by the application.
 * This will only be a necessary component for Increment 1.
 */
public class DashboardMetricsPanel extends JPanel {

    private Map<MetricType, MetricBoxPanel> metricBoxes;

    public DashboardMetricsPanel() {
        metricBoxes = new HashMap<>();
    }

    /**
     * Initialise the MetricsPanel
     */
    public void init() {
        this.setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(800, 900));
        this.setBackground(DashboardMainFrame.BG_COLOR);

        //Set up the title and the panel used to centre it
        JLabel title = new JLabel("Some Key Metrics");
        title.setFont(DashboardMainFrame.GLOB_FONT);
        title.setFont(new Font(title.getFont().getName(), title.getFont().getStyle(), 20));
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(Box.createRigidArea(new Dimension(0, 70)));
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        //Set up the grid which displays the metrics
        JPanel metrics = new JPanel();
        metrics.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        metrics.setLayout(new GridLayout(3, 1));

        MetricBoxPanel totalImpressionsPanel = new MetricBoxPanel(MetricType.TOTAL_IMPRESSIONS);
        MetricBoxPanel CTRPanel = new MetricBoxPanel(MetricType.CTR);
        MetricBoxPanel totalCostPanel = new MetricBoxPanel(MetricType.TOTAL_COST);

        metricBoxes.put(MetricType.TOTAL_IMPRESSIONS, totalImpressionsPanel);
        metricBoxes.put(MetricType.CTR, CTRPanel);
        metricBoxes.put(MetricType.TOTAL_COST, totalCostPanel);

        metrics.add(totalImpressionsPanel);
        metrics.add(CTRPanel);
        metrics.add(totalCostPanel);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(metrics, BorderLayout.CENTER);
        this.add(Box.createRigidArea(new Dimension(0, 100)), BorderLayout.SOUTH);
    }

    /**
     * Put a metric in the list of metrics
     * @param type The type of the metric from the MetricType enum
     * @param data The value of the metric
     */
    public void putMetricInPanel(MetricType type, Number data) {
        metricBoxes.get(type).setData(data);
    }
}
