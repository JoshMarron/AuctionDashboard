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
import java.util.Map;
import java.util.Optional;

/**
 * MainFrameMainPieChartPanel displays a small pie chart of attributes as total shares
 */
public class MainFrameMainPieChartPanel extends MainFrameMainAttributeChartPanel {

    private JFXPanel chartPanel;
    private ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
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
            pieData = FXCollections.observableArrayList();
            chart.setTitle(metric.toString() + " by " + attr.toString());

            data.forEach((name, number) -> {
                pieData.add(new PieChart.Data(name, number.doubleValue()));
            });

            chart.setData(pieData);

            if (chartPanel.getScene() == null) {

                Scene scene = new Scene(chart);
                scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());
                chartPanel.setScene(scene);
            }

        });
    }
}
