package Views;

import javax.swing.*;
import java.awt.*;

/**
 * Created by marro on 21/02/2017.
 */
public class DashboardMetricsPanel extends JPanel {

    public DashboardMetricsPanel() {

    }

    public void init() {
        this.setLayout(new BorderLayout());

        JLabel title = new JLabel("Some Key Metrics");
        JList<String> metrics = new JList<>();

        this.add(title, BorderLayout.NORTH);
        this.add(metrics, BorderLayout.CENTER);
    }
}
