package Views.MainFramePanels;

import Model.DBEnums.DateEnum;
import Views.CustomComponents.CatPanel;
import Views.DashboardMainFrame;
import Views.MetricType;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ChartType;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * MainFrameChartDisplayPanel displays the selected type of chart and populates it
 */
public class MainFrameChartDisplayPanel extends CatPanel {

    private DashboardMainFrame parent;
    Map<ChartType, MainFrameMainTimeChartPanel> timeChartMap;
    Map<ChartType, MainFrameMainAttributeChartPanel> attributeChartMap;
    private ChartType currentChart;
    private CardLayout cards;

    public MainFrameChartDisplayPanel(DashboardMainFrame parent) {
        this.parent = parent;
        this.currentChart = ChartType.LINE;
        timeChartMap = new HashMap<>();
        attributeChartMap = new HashMap<>();
        this.init();
    }

    private void init() {
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        cards = new CardLayout();

        this.setLayout(cards);

        MainFrameMainLineChartPanel linePanel = new MainFrameMainLineChartPanel();
        MainFrameMainBarChartPanel barPanel = new MainFrameMainBarChartPanel();
        MainFrameMainPieChartPanel piePanel = new MainFrameMainPieChartPanel();

        timeChartMap.put(ChartType.LINE, linePanel);
        attributeChartMap.put(ChartType.BAR, barPanel);
        attributeChartMap.put(ChartType.PIE, piePanel);

        this.add(linePanel, ChartType.LINE.getName());
        this.add(barPanel, ChartType.BAR.getName());
        this.add(piePanel, ChartType.PIE.getName());

        cards.show(this, ChartType.LINE.getName());
    }

    public void displayTimeChart(ChartType chartType, MetricType type, DateEnum granularity, Map<Instant, Number> data) {
        cards.show(this, chartType.getName());
        this.currentChart = chartType;
        MainFrameMainTimeChartPanel chart = timeChartMap.get(chartType);
        chart.displayChart(type, granularity, data);
    }

    public void displayAttributeChart(ChartType chartType, MetricType type, AttributeType attr, Map<String, Number> data) {
        cards.show(this, chartType.getName());
        this.currentChart = chartType;
        MainFrameMainAttributeChartPanel chart = attributeChartMap.get(chartType);
        chart.displayChart(type, attr, data);
    }

    public ChartType getCurrentChart() {
        return this.currentChart;
    }
}
