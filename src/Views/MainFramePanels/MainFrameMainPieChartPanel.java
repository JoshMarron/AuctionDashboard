package Views.MainFramePanels;

import Model.DBEnums.DateEnum;
import Views.CustomComponents.CatPanel;
import Views.MetricType;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ColorSettings;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * MainFrameMainPieChartPanel displays a small pie chart of attributes as total shares
 */
public class MainFrameMainPieChartPanel extends MainFrameMainAttributeChartPanel {

    private JFXPanel chartPanel;

    public MainFrameMainPieChartPanel() {
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());

        chartPanel = new JFXPanel();
        chartPanel.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()));

        this.add(chartPanel, BorderLayout.CENTER);
    }

    @Override
    public void displayChart(MetricType metric, AttributeType attr, Map<String, Number> data) {

        Platform.runLater(() -> {
            PieChart chart = new PieChart();
            chart.setTitle(metric.toString() + " by " + attr.toString());
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

            data.forEach((name, number) -> {
                pieData.add(new PieChart.Data(name, number.doubleValue()));
            });

            Scene scene = new Scene(chart);
            chart.setData(pieData);
            scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());
            chartPanel.setScene(scene);

        });
    }
}
