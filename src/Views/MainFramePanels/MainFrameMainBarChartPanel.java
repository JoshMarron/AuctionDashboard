package Views.MainFramePanels;

import DataStructures.CsvInterfaces.Income;
import Model.DBEnums.attributes.Attribute;
import Views.CustomComponents.CatPanel;
import Views.MetricType;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ColorSettings;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * MainFrameMainBarChartPanel displays a bar chart when given the correct data
 */
public class MainFrameMainBarChartPanel extends MainFrameMainAttributeChartPanel {

    private JFXPanel chartPanel;

    public MainFrameMainBarChartPanel() {
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());

        chartPanel = new JFXPanel();
        chartPanel.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR));

        this.add(chartPanel, BorderLayout.CENTER);
    }

    public void displayChart(MetricType type, AttributeType attribute, Map<String, Number> data) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel(attribute.toString());
        yAxis.setLabel(type.toString());

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(type.toString() + " by " + attribute.toString());
        barChart.setLegendVisible(false);

        XYChart.Series series = new XYChart.Series();

        List<String> sortedKeys = AttributeType.sortAttributeValues(attribute, new ArrayList<>(data.keySet()));
        sortedKeys.forEach((key) -> {
            series.getData().add(new XYChart.Data(key, data.get(key).doubleValue()));
        });
        barChart.getData().add(series);
        Scene scene = new Scene(barChart);
        scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());

        chartPanel.setScene(scene);
    }
}
