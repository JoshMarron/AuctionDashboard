package Views;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * DashboardMainFrame is the main frame visible during the running of the application, which contains all of the
 * GUI elements.
 */
public class DashboardMainFrame extends JFrame {

    private File homeDir;

    public DashboardMainFrame(File homeDir) {
        this.homeDir = homeDir;
    }

    public void init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1200, 900);
        this.setName("Ad Auction Dashboard");

        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        DashboardFileSelectPanel fileSelect = new DashboardFileSelectPanel(homeDir);
        fileSelect.init();

        DashboardMetricsPanel metrics = new DashboardMetricsPanel();
        metrics.init();

        this.add(Box.createRigidArea(new Dimension(50, 0)));
        this.add(fileSelect);
        this.add(Box.createRigidArea(new Dimension(50, 0)));
        this.add(metrics);
        this.add(Box.createRigidArea(new Dimension(50, 0)));

        this.setVisible(true);
    }

}
