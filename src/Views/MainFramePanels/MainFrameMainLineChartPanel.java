package Views.MainFramePanels;

import Views.CustomComponents.CatPanel;
import Views.ViewPresets.ColorSettings;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.*;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.util.Map;

/**
 * MainFrameMainLineChartPanel contains the main chart, whether it be a bar or line chart
 */
public class MainFrameMainLineChartPanel extends CatPanel {

    private Scene thisScene;
    private Map<Instant, Number> dataMap;

    public MainFrameMainLineChartPanel(Map<Instant, Number> dataMap) {
        this.init();
        this.dataMap = dataMap;
    }

    private void init() {
        this.setLayout(new BorderLayout());

        JFXPanel chartPanel = new JFXPanel();
        chartPanel.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()));
        Platform.runLater(() -> {
            thisScene = createLineScene();
            thisScene.getStylesheets().clear();
            thisScene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());
            chartPanel.setScene(thisScene);
        });

        this.add(chartPanel, BorderLayout.CENTER);
    }

    private Scene createLineScene() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        yAxis.setLabel("Number of clicks");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("im a chart");
        XYChart.Series series1 = new XYChart.Series();


        series1.getData().add(new XYChart.Data("Date 1", 2302520));
        series1.getData().add(new XYChart.Data("Date 2", 243436303));

        Scene scene = new Scene(chart);
        chart.getData().addAll(series1);
        return scene;
    }
}
