package Views;

import Controllers.DashboardMainFrameController;

import Model.DBEnums.LogType;
import Views.Deprecated.DashboardMetricsPanel;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.File;
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
    private DashboardMetricsPanel metricsPanel;
    private boolean loading;
    private ImageIcon icon;

    public DashboardMainFrame(File homeDir) {
        this.homeDir = homeDir;
        loading = false;
    }

    public void init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1200, 900);
        this.setTitle("Catalysis");

        //Try to use anti-aliasing on the font
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        //Set the System Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error getting System Look and Feel, reverting to Java default",
                    "Graphical Error",
                    JOptionPane.ERROR_MESSAGE
                    );
        }

        UIManager.put("ComboBox.background", new ColorUIResource(ColorSettings.TEXT_AREA_BG_COLOR.getColor()));
        UIManager.put("ComboBox.foreground", new ColorUIResource(ColorSettings.TEXT_AREA_TEXT_COLOR.getColor()));
        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(ColorSettings.TEXT_AREA_BG_COLOR.getColor().brighter()));
        UIManager.put("ComboBox.selectionForeground", new ColorUIResource(ColorSettings.TEXT_AREA_TEXT_COLOR.getColor()));

        JPanel contentPane = new JPanel() {
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

        this.setContentPane(contentPane);
        contentPane.setBackground(ColorSettings.BG_COLOR.getColor());

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        DashboardFileSelectPanel fileSelect = new DashboardFileSelectPanel(homeDir, this);
        fileSelect.init();

        metricsPanel = new DashboardMetricsPanel();
        metricsPanel.init();

        this.add(Box.createRigidArea(new Dimension(50, 0)));
        this.add(fileSelect);
        this.add(Box.createRigidArea(new Dimension(50, 0)));
        this.add(metricsPanel);
        this.add(Box.createRigidArea(new Dimension(50, 0)));
    }

    public void setController(DashboardMainFrameController controller) {
        this.controller = controller;
    }

    public void submitFiles(Map<LogType, File> files) {
    }

    public void displayMetrics(Map<MetricType, Number> data) {
        data.forEach((type, value) -> metricsPanel.putMetricInPanel(type, value));
    }

}
