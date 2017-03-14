package Views.MainFramePanels;

import Views.CustomComponents.CatPanel;
import Views.MetricType;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ColorSettings;
import javafx.embed.swing.JFXPanel;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * MainFrameMainBarChartPanel displays a bar chart when given the correct data
 */
public class MainFrameMainBarChartPanel extends CatPanel {

    private JFXPanel chartPanel;

    public MainFrameMainBarChartPanel() {
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());

        chartPanel = new JFXPanel();
        chartPanel.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()));

        this.add(chartPanel, BorderLayout.CENTER);
    }

    public void displayChart(MetricType type, AttributeType attribute, Map<AttributeType, Number> data) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel(attribute.toString());
        yAxis.setLabel(type.toString());

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(type.toString() + " by " + attribute.toString());


    }
}
