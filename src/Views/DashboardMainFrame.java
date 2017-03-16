package Views;

import Controllers.DashboardMainFrameController;

import Model.DBEnums.DateEnum;
import Model.DBEnums.LogType;
import Views.CustomComponents.CatFrame;
import Views.CustomComponents.CatPanel;
import Views.MainFramePanels.*;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ChartType;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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
    private MetricType currentMetric;
    private AttributeType currentAttribute;
    private MainFrameChartOptionsPanel optionsPanel;

    public DashboardMainFrame(File homeDir) {
        this.homedir = homeDir;
        this.requestedChart = ChartType.LINE;
        this.currentAttribute = AttributeType.CONTEXT;
        this.currentMetric = MetricType.TOTAL_IMPRESSIONS;
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

        this.setContentPane(mainContentPane);
        mainContentPane.setLayout(new BorderLayout());

        metricList = new MainFrameMetricList(this);
        chartPanel = new MainFrameChartDisplayPanel(this);
        optionsPanel = new MainFrameChartOptionsPanel(this);

        CatPanel chartDisplay = new CatPanel();
        chartDisplay.setBorder(BorderFactory.createEmptyBorder());
        chartDisplay.setLayout(new BorderLayout());

        chartDisplay.add(chartPanel, BorderLayout.CENTER);
        chartDisplay.add(optionsPanel, BorderLayout.SOUTH);

        CatPanel mainPanel = new CatPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.add(metricList);
        mainPanel.add(chartDisplay);

        this.initGlassPane();
        mainContentPane.add(mainPanel, BorderLayout.CENTER);
    }

    public void setController(DashboardMainFrameController controller) {
        this.controller = controller;
    }

    public void submitFiles(Map<LogType, File> files) {
    }

    public void displayMetrics(Map<MetricType, Number> data) {
        metricList.resetMetricBoxes();
        data.forEach((type, value) -> metricList.putMetricInBox(type, value));
    }

    public void requestMetricChange(MetricType type) {
        this.currentMetric = type;
        requestNewChart();
    }

    public void requestTimeChartTypeChange(ChartType chartType) {
        if (!this.requestedChart.equals(chartType)) {
            this.requestedChart = chartType;
            requestNewChart();
        }
    }

    public void requestAttributeChartTypeChange(ChartType chartType, AttributeType attr) {
        if (!this.requestedChart.equals(chartType) || !this.currentAttribute.equals(attr)) {
            this.requestedChart = chartType;
            this.currentAttribute = attr;
            requestNewChart();
        }
    }

    private void requestNewChart() {
        this.displayLoading();
        if (this.requestedChart.equals(ChartType.LINE)) {
            controller.requestTimeChart(currentMetric);
        } else {
            controller.requestAttributeChart(currentMetric, currentAttribute);
        }
    }

    public void displayChart(MetricType type, DateEnum granularity, Map<Instant, Number> data) {
        chartPanel.displayTimeChart(requestedChart, type, granularity, data);
        this.finishedLoading();
    }

    public void displayChart(MetricType type, AttributeType attr, Map<String, Number> data) {
        chartPanel.displayAttributeChart(requestedChart, type, attr, data);
        this.finishedLoading();
    }

    public void saveFileAs() {
        JFileChooser saveChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CAT CatAnalysis Files", "cat");
        saveChooser.setFileFilter(filter);
        int saveChooserVal = saveChooser.showSaveDialog(this);
        if (saveChooserVal == JFileChooser.APPROVE_OPTION) {
            try {
                File saved = saveChooser.getSelectedFile();
                String savedFileName = saved.getName();
                if (!savedFileName.contains(".")) {
                    savedFileName = savedFileName.split("\\.")[0] + ".cat";
                } else {
                    savedFileName = savedFileName + ".cat";
                }
                controller.saveProject(new File(savedFileName));
                JOptionPane.showMessageDialog(this,
                        "File successfully saved!",
                        "Saved",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "The file could not be saved at the selected location!",
                        "Saving error!",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

    }

    public void closeProject() {
        controller.closeProject();
    }

    public File getHomeDir() {
        return this.homedir;
    }
}
