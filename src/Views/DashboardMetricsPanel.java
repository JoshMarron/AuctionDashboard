package Views;

import javax.swing.*;
import java.awt.*;

/**
 * DashboardMetricsPanel is the GUI element responsible for displaying a list of metrics computed by the application.
 * This will only be a necessary component for Increment 1.
 */
public class DashboardMetricsPanel extends JPanel {

    private DefaultListModel<String> metricsModel;

    public DashboardMetricsPanel() {

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

        //Set up the model for the JList, this allows us to easily modify the list
        metricsModel = new DefaultListModel<>();
        JList<String> metrics = new JList<>(metricsModel);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(metrics, BorderLayout.CENTER);
        this.add(Box.createRigidArea(new Dimension(0, 100)), BorderLayout.SOUTH);
    }

    /**
     * Put a metric in the list of metrics
     * @param name The name of the metric
     * @param data The value of the metric
     */
    public void putMetricInTextList(String name, Double data) {
        String displayData = name + ": " + data;
        metricsModel.addElement(displayData);
    }
}
