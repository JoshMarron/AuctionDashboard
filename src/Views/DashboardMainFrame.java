package Views;

import Controllers.DashboardMainFrameController;

import Controllers.ProjectSettings;
import Controllers.Queries.*;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
/**
 * DashboardMainFrame is the main frame visible during the running of the application, which contains all of the
 * GUI elements.
 */
public class DashboardMainFrame extends CatFrame {

    private MainFrameChartDisplayPanel chartPanel;
    private DashboardMainFrameController controller;
    private MainFrameMetricList metricList;
    private ChartType requestedChart;
    private DateEnum granularity;
    private MetricType currentMetric;
    private AttributeType currentAttribute;
    private MainFrameChartOptionsPanel optionsPanel;
    private Map<AttributeType, List<String>> filters;
    private Instant startDate;
    private Instant endDate;

    public DashboardMainFrame(File homeDir) {
        this.homedir = homeDir;
        this.requestedChart = ChartType.LINE;
        this.granularity = DateEnum.DAYS;
        this.currentAttribute = AttributeType.CONTEXT;
        this.currentMetric = MetricType.TOTAL_IMPRESSIONS;
        this.filters = new HashMap<>();
        this.startDate = ProjectSettings.MIN_DATE;
        this.endDate = ProjectSettings.MAX_DATE;
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

    public void setUpFilterOptions(Map<AttributeType, List<String>> possibleVals) {
        optionsPanel.setUpFilterOptions(possibleVals);
    }

    public void requestMetricChange(MetricType type) {
        this.currentMetric = type;
        requestNewChart();
    }

    public void requestTimeChartTypeChange(ChartType chartType, DateEnum granularity) {
        if (!this.requestedChart.equals(chartType) || !this.granularity.equals(granularity)) {
            this.requestedChart = chartType;
            this.granularity = granularity;
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

    public void requestFilterRefresh(Instant startDate, Instant endDate, Map<AttributeType, List<String>> filters) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.filters = filters;
        requestNewChart();
    }

    private void requestNewChart() {
        this.displayLoading();
        if (this.requestedChart.equals(ChartType.LINE)) {
            TimeDataQuery query = new TimeQueryBuilder(currentMetric)
                                        .filters(filters)
                                        .granularity(granularity)
                                        .startDate(startDate)
                                        .endDate(endDate)
                                        .build();
            controller.requestTimeChart(query);
        } else {
            AttributeDataQuery query = new AttributeQueryBuilder(currentMetric, currentAttribute)
                                            .filters(filters)
                                            .startDate(startDate)
                                            .endDate(endDate)
                                            .build();

            controller.requestAttributeChart(query);
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

    public void displayDoubleChart(MetricType type, DateEnum granularity, Map<Instant, Number> data1, Map<Instant, Number> data2) {
        chartPanel.displayDoubleTimeChart(requestedChart, type, granularity, data1, data2);
        this.finishedLoading();
    }

    public void displayDoubleChart(MetricType type, AttributeType attr, Map<String, Number> data1, Map<String, Number> data2) {
        chartPanel.displayDoubleAttributeChart(requestedChart, type, attr, data1, data2);
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
                String savedFileName = saved.getAbsolutePath();
                if (!savedFileName.contains(".")) {
                    savedFileName = savedFileName.split("\\.")[0] + ".cat";
                } else {
                    savedFileName = savedFileName + ".cat";
                }
                //Path savedPath = Paths.get(saved.getAbsolutePath());
                //savedPath.resolveSibling(savedFileName);
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
            }
        }

    }

    // This should refresh all the metrics + the current chart
    public void refresh() {
        TotalQuery query = new TotalQueryBuilder(MetricType.TOTAL_IMPRESSIONS).startDate(startDate)
                                                    .endDate(endDate)
                                                    .filters(filters)
                                                    .build();
        System.out.println(ProjectSettings.getBouncePages());
        controller.refreshKeyMetrics(query);
        this.requestNewChart();

    }

    public void addSecondCampaign() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a project file...");
        FileNameExtensionFilter catFilter = new FileNameExtensionFilter("CAT Files", "cat");
        chooser.setFileFilter(catFilter);

        int chooserVal = chooser.showOpenDialog(this);
        if (chooserVal == JFileChooser.APPROVE_OPTION) {
            File chosenCampaign = chooser.getSelectedFile();
            controller.addSecondCampaign(chosenCampaign);
        }
    }

    public void closeProject() {
        this.requestedChart = ChartType.LINE;
        controller.closeProject();
    }

    public File getHomeDir() {
        return this.homedir;
    }

    public void setFilters(Map<AttributeType, List<String>> filters) {
        this.filters = filters;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }
}
