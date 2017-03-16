package Views;

import Controllers.DashboardMainFrameController;

import Model.DBEnums.DateEnum;
import Model.DBEnums.LogType;
import Views.CustomComponents.CatFrame;
import Views.CustomComponents.CatPanel;
import Views.MainFramePanels.*;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ChartType;
import javafx.scene.chart.Chart;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;

/**
 * DashboardMainFrame is the main frame visible during the running of the application, which contains all of the
 * GUI elements.
 */
public class DashboardMainFrame extends CatFrame {

    private MainFrameChartDisplayPanel chartPanel;
    private DashboardMainFrameController controller;
    private MainFrameMetricList metricList;
    private ChartType requestedChart;

    public DashboardMainFrame(File homeDir) {
        this.homedir = homeDir;
        this.requestedChart = ChartType.LINE;
        loading = false;
    }

    public void init() {
        super.init();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(new Dimension(1400, 800));
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setTitle("CatAnalysis");

        CatPanel mainContentPane = (CatPanel) this.getContentPane();

        this.setJMenuBar(new MainFrameMenu(this));

        mainContentPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        metricList = new MainFrameMetricList(this);
        this.setContentPane(mainContentPane);

        chartPanel = new MainFrameChartDisplayPanel(this);

        mainContentPane.setLayout(new BorderLayout());
        CatPanel mainPanel = new CatPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.add(metricList);
        mainPanel.add(chartPanel);

        this.initGlassPane();
        mainContentPane.add(mainPanel, BorderLayout.CENTER);
    }

    public void setController(DashboardMainFrameController controller) {
        this.controller = controller;
    }

    public void submitFiles(Map<LogType, File> files) {
    }

    public void displayMetrics(Map<MetricType, Number> data) {

        data.forEach((type, value) -> metricList.putMetricInBox(type, value));
    }

    public void requestChart(MetricType type) {
        controller.requestChart(type);
    }

    public void displayTimeChart(MetricType type, DateEnum granularity, Map<Instant, Number> data) {
        chartPanel.displayTimeChart(requestedChart, type, granularity, data);
    }

    public void requestAttributeChart(MetricType type, AttributeType attr) {
        controller.requestAttributeChart(type, attr);
    }

    public void displayAttributeChart(MetricType type, AttributeType attr, Map<String, Number> data) {
        chartPanel.displayAttributeChart(requestedChart, type, attr, data);
    }

    public void saveFileAs() {
        JFileChooser saveChooser = new JFileChooser();
        int saveChooserVal = saveChooser.showSaveDialog(this);
        if (saveChooserVal == JFileChooser.APPROVE_OPTION) {
            try {
                controller.saveProject(saveChooser.getSelectedFile());
            } catch (IOException e) {
                /*JOptionPane.showMessageDialog(this,
                        "The file could not be saved at the selected location!",
                        "Saving error!",
                        JOptionPane.ERROR_MESSAGE);*/
                e.printStackTrace();
                return;
            }
        }
        JOptionPane.showMessageDialog(this,
                "File successfully saved!",
                "Saved",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
