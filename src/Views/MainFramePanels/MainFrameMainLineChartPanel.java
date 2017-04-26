package Views.MainFramePanels;

import Model.DBEnums.DateEnum;
import Views.CustomComponents.CatPanel;
import Views.MetricType;
import Views.ViewPresets.ColorSettings;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.*;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Map;

/**
 * MainFrameMainLineChartPanel contains the main chart, whether it be a bar or line chart
 */
public class MainFrameMainLineChartPanel extends MainFrameMainTimeChartPanel {

    private JFXPanel chartPanel;
    private LineChart<String, Number> chart;

    public MainFrameMainLineChartPanel() {
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());

        chartPanel = new JFXPanel();
        chartPanel.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR));

        this.add(chartPanel, BorderLayout.CENTER);
    }

    public void displayChart(MetricType type, DateEnum granularity, Map<Instant, Number> data) {
        Platform.runLater(() -> {
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();

            xAxis.setLabel("Date");
            yAxis.setLabel(type.toString());

            chart = new LineChart<>(xAxis, yAxis);
            chart.setTitle(type.toString() + " over time");
            XYChart.Series series = new XYChart.Series();
            series.setName(type.toString());

            if (granularity.equals(DateEnum.HOURS)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                        .withLocale(Locale.UK)
                        .withZone(ZoneId.systemDefault());
                data.keySet().stream().sorted().forEach((time) -> series.getData().add(new XYChart.Data(formatter.format(time), data.get(time))));
            }
            else {
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                        .withLocale(Locale.UK)
                        .withZone(ZoneId.systemDefault());
                data.keySet().stream().sorted().forEach((time) -> series.getData().add(new XYChart.Data(formatter.format(time), data.get(time))));
            }


            Scene scene = new Scene(chart);
            scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());
            chart.getData().addAll(series);

            chartPanel.setScene(scene);

        });
    }

    public LineChart<String, Number> getChart(){
        return chart;
    }
}
