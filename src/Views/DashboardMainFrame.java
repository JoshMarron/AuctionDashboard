package Views;

import Controllers.DashboardMainFrameController;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

/**
 * DashboardMainFrame is the main frame visible during the running of the application, which contains all of the
 * GUI elements.
 */
public class DashboardMainFrame extends JFrame {

    private File homeDir;
    private DashboardMainFrameController controller;
    private DashboardMetricsPanel metricsPanel;

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

        DashboardFileSelectPanel fileSelect = new DashboardFileSelectPanel(homeDir, this);
        fileSelect.init();

        metricsPanel = new DashboardMetricsPanel();
        metricsPanel.init();

        this.add(Box.createRigidArea(new Dimension(50, 0)));
        this.add(fileSelect);
        this.add(Box.createRigidArea(new Dimension(50, 0)));
        this.add(metricsPanel);
        this.add(Box.createRigidArea(new Dimension(50, 0)));

        this.setVisible(true);
    }

    public void setController(DashboardMainFrameController controller) {
        this.controller = controller;
    }

    public void submitFiles(List<File> files) {
        this.controller.processFiles(files);
    }


    public void displayMetrics(Map<String, Double> data) {
        data.forEach((name, value) -> metricsPanel.putMetricInTextList(name, value));
    }
}
