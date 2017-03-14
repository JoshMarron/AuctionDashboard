package Views;

import Controllers.DashboardMainFrameController;

import Model.DBEnums.DateEnum;
import Model.DBEnums.LogType;
import Views.CustomComponents.CatFileChooser;
import Views.CustomComponents.CatMenuBar;
import Views.CustomComponents.CatPanel;
import Views.MainFramePanels.MainFrameMainLineChartPanel;
import Views.MainFramePanels.MainFrameMenu;
import Views.MainFramePanels.MainFrameMetricList;
import Views.MainFramePanels.MainFramePieChartPanel;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;

/**
 * DashboardMainFrame is the main frame visible during the running of the application, which contains all of the
 * GUI elements.
 */
public class DashboardMainFrame extends JFrame {

    public static final Color BG_COLOR = new Color(146, 171, 185);
    public static final Font GLOB_FONT = new Font("Tahoma", Font.BOLD, 14);

    private File homeDir;
    private DashboardMainFrameController controller;
    private boolean loading;
    private ImageIcon icon;
    private MainFrameMetricList metricList;
    private MainFrameMainLineChartPanel chartPanel;

    public DashboardMainFrame(File homeDir) {
        this.homeDir = homeDir;
        loading = false;
    }

    public void init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(new Dimension(1400, 800));
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setTitle("CatAnalysis");

        CatPanel mainContentPane = new CatPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2 = (Graphics2D) g;

                if (loading) {
                    g2.setColor(ColorSettings.LOADING_COLOR.getColor());
                    g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                }
            }
        };

        this.setJMenuBar(new MainFrameMenu(this));

        mainContentPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        metricList = new MainFrameMetricList(this);
        chartPanel = new MainFrameMainLineChartPanel();
        MainFramePieChartPanel piePanel = new MainFramePieChartPanel(AttributeType.AGE);
        this.setContentPane(mainContentPane);
        mainContentPane.setLayout(new BorderLayout());

        CatPanel mainPanel = new CatPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.add(metricList);
        mainPanel.add(chartPanel);

        mainContentPane.add(mainPanel, BorderLayout.CENTER);

    }

    public void setController(DashboardMainFrameController controller) {
        this.controller = controller;
    }

    public void submitFiles(Map<LogType, File> files) {
    }

    public void displayMetrics(Map<MetricType, Number> data) {

        data.forEach((type, value) -> metricList.putMetricInBox(type, value));
    }

    public void requestChart(MetricType type) {
        controller.requestChart(type);
    }

    public void displayChart(MetricType type, DateEnum granularity, Map<Instant, Number> data) {
        chartPanel.displayChart(type, granularity, data);
    }

    public void saveFileAs() {
        JFileChooser saveChooser = new JFileChooser();
        int saveChooserVal = saveChooser.showSaveDialog(this);
        if (saveChooserVal == JFileChooser.APPROVE_OPTION) {
            try {
                controller.saveProject(saveChooser.getSelectedFile());
            } catch (IOException e) {
                /*JOptionPane.showMessageDialog(this,
                        "The file could not be saved at the selected location!",
                        "Saving error!",
                        JOptionPane.ERROR_MESSAGE);*/
                e.printStackTrace();
                return;
            }
        }
        JOptionPane.showMessageDialog(this,
                "File successfully saved!",
                "Saved",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
