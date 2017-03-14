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
import javafx.scene.chart.PieChart;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * MainFramePieChartPanel displays a small pie chart of attributes as total shares
 */
public class MainFramePieChartPanel extends CatPanel {

    private AttributeType type;
    private JFXPanel chartPanel;

    public MainFramePieChartPanel(AttributeType type) {
        this.type = type;
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());

        chartPanel = new JFXPanel();
        chartPanel.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()));

        this.add(chartPanel, BorderLayout.CENTER);
    }

    public void displayChart(MetricType metric, DateEnum granularity, Map<String, Number> data) {

        Platform.runLater(() -> {
            PieChart chart = new PieChart();
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

            data.forEach((name, number) -> {
                //TODO FILL THIS OUT, NEED TO CALCULATE PERCENTAGES - HERE OR CONTROLLER???
            });
        });
    }
}
