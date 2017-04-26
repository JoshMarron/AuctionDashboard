package Views.MainFramePanels;

import Model.DBEnums.DateEnum;
import Views.CustomComponents.CatPanel;
import Views.DashboardMainFrame;
import Views.MetricType;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ChartType;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * MainFrameChartDisplayPanel displays the selected type of chart and populates it
 */
public class MainFrameChartDisplayPanel extends CatPanel {

    private DashboardMainFrame parent;
    private Map<ChartType, MainFrameMainTimeChartPanel> timeChartMap;
    private Map<ChartType, MainFrameMainAttributeChartPanel> attributeChartMap;
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

    public Map<ChartType, MainFrameMainTimeChartPanel> getTimeChartMap(){
        return timeChartMap;
    }

    public Map<ChartType, MainFrameMainAttributeChartPanel> getAttributeChartMap(){
        return attributeChartMap;
    }

    public void saveTimeChart(Map<ChartType, MainFrameMainTimeChartPanel> chartMap){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try{
                    MainFrameMainLineChartPanel line = (MainFrameMainLineChartPanel)chartMap.get(ChartType.LINE);
                    WritableImage image = line.getChart().snapshot(new SnapshotParameters(), null);
                    saveExportedChart(image);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveAttributeChart(Map<ChartType, MainFrameMainAttributeChartPanel> chartMap){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try{

                    ChartType type = null;
                    MainFrameMainPieChartPanel pieChart = null;
                    MainFrameMainBarChartPanel barChart = null;
                    if(parent.getRequestedChart() == ChartType.BAR){
                        barChart = (MainFrameMainBarChartPanel)chartMap.get(ChartType.BAR);
                        type = ChartType.BAR;
                    } else if(parent.getRequestedChart() == ChartType.PIE){
                        pieChart = (MainFrameMainPieChartPanel)chartMap.get(ChartType.PIE);
                        type = ChartType.PIE;
                    }





                    WritableImage image = null;
                    if(type == ChartType.PIE) {image = pieChart.getChart().snapshot(new SnapshotParameters(), null);}
                    if(type == ChartType.BAR) {image = barChart.getChart().snapshot(new SnapshotParameters(), null);}
                    saveExportedChart(image);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void saveExportedChart(WritableImage image){
        File file = null;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export chart...");
        chooser.setFileFilter(new FileNameExtensionFilter("PNG File", "png"));
        int userSelection = chooser.showSaveDialog(parent);

        if(userSelection == JFileChooser.APPROVE_OPTION){
            file = chooser.getSelectedFile();

            File fileToSave = new File(file.toString() + ".png");
            parent.saveExport(image, fileToSave);
        }
    }
}
