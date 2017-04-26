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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.text.Text;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * MainFrameMainPieChartPanel displays a small pie chart of attributes as total shares
 */
public class MainFrameMainPieChartPanel extends MainFrameMainAttributeChartPanel {

    private JFXPanel chartPanel;
    private JFXPanel secondChartPanel;
    private ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
    private ObservableList<PieChart.Data> pieData2 = FXCollections.observableArrayList();
    private DecimalFormat df = new DecimalFormat("#.##");
    private final PieChart chart = new PieChart(pieData) {
        @Override
        protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight) {
            if (getLabelsVisible()) {
                getData().forEach(d -> {
                    Optional<Node> opTextNode = chart.lookupAll(".chart-pie-label").stream().filter(n -> n instanceof Text && ((Text) n).getText().contains(d.getName())).findAny();
                    if (opTextNode.isPresent()) {
                        ((Text) opTextNode.get()).setText(d.getName() + ": " + df.format(d.getPieValue()));
                    }
                });
            }
            super.layoutChartChildren(top, left, contentWidth, contentHeight);
        }
    };

    private final PieChart secondPie = new PieChart(pieData) {
        @Override
        protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight) {
            if (getLabelsVisible()) {
                getData().forEach(d -> {
                    Optional<Node> opTextNode = chart.lookupAll(".chart-pie-label").stream().filter(n -> n instanceof Text && ((Text) n).getText().contains(d.getName())).findAny();
                    if (opTextNode.isPresent()) {
                        ((Text) opTextNode.get()).setText(d.getName() + ": " + df.format(d.getPieValue()));
                    }
                });
            }
            super.layoutChartChildren(top, left, contentWidth, contentHeight);
        }
    };

    public MainFrameMainPieChartPanel() {
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());

        chartPanel = new JFXPanel();
        secondChartPanel = new JFXPanel();
        chartPanel.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR));
        secondChartPanel.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR));

    }

    @Override
    public void displayChart(MetricType metric, AttributeType attr, Map<String, Number> data) {

        Platform.runLater(() -> {
            pieData = FXCollections.observableArrayList();
            chart.setTitle(metric.toString() + " by " + attr.toString());

            List<String> sortedKeys = AttributeType.sortAttributeValues(attr, new ArrayList<>(data.keySet()));

             sortedKeys.forEach((name) -> {
                pieData.add(new PieChart.Data(name, data.get(name).doubleValue()));
            });

            chart.setData(pieData);

            if (chartPanel.getScene() == null) {

                Scene scene = new Scene(chart);
                scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());
                chartPanel.setScene(scene);
            }

        });

        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(chartPanel, BorderLayout.CENTER);
    }

    @Override
    public void displayDoubleChart(MetricType type, AttributeType attr, Map<String, Number> data1, Map<String, Number> data2) {
        Platform.runLater(() -> {
            pieData = FXCollections.observableArrayList();
            chart.setTitle(type.toString() + " by " + attr.toString() + " (Series 1)");

            pieData2 = FXCollections.observableArrayList();
            secondPie.setTitle(type.toString() + " by " + attr.toString() + " (Series 2)");

            List<String> sortedKeys = AttributeType.sortAttributeValues(attr, new ArrayList<>(data1.keySet()));

            sortedKeys.forEach((name) -> {
                pieData.add(new PieChart.Data(name, data1.get(name).doubleValue()));
                pieData2.add(new PieChart.Data(name, data2.get(name).doubleValue()));
            });

            chart.setData(pieData);
            secondPie.setData(pieData2);

            if (chartPanel.getScene() == null) {

                Scene scene = new Scene(chart);
                scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());
                chartPanel.setScene(scene);
            }
            if (secondChartPanel.getScene() == null) {

                Scene scene = new Scene(secondPie);
                scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());
                secondChartPanel.setScene(scene);
            }
        });

        this.removeAll();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(chartPanel);
        this.add(secondChartPanel);
    }


}
