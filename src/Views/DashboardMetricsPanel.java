package Views;

import javax.swing.*;
import java.awt.*;

/**
 * DashboardMetricsPanel is the GUI element responsible for displaying a list of metrics computed by the application.
 * This will only be a necessary component for Increment 1.
 */
public class DashboardMetricsPanel extends JPanel {

    public DashboardMetricsPanel() {

    }

    public void init() {
        this.setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(600, 900));
        JLabel title = new JLabel("Some Key Metrics");
        JList<String> metrics = new JList<>();

        this.add(title, BorderLayout.NORTH);
        this.add(metrics, BorderLayout.CENTER);
    }
}
