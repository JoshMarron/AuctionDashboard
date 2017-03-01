package Views;

import Controllers.DashboardMainFrameController;

import Model.LogType;
import javax.swing.*;
//import org.apache.batik.swing.JSVGCanvas;
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
        this.setName("Ad Auction Dashboard");

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

        JPanel contentPane = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2 = (Graphics2D) g;

                if (loading) {
                    g2.setColor(new Color(181, 184, 188, 160));
                    g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                }
            }
        };
        this.setContentPane(contentPane);
        contentPane.setBackground(BG_COLOR);

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

        this.initGlassPane();

        this.setVisible(true);
    }

    public void initGlassPane() {
        JPanel loadingPanel = (JPanel) this.getGlassPane();
        loadingPanel.setLayout(new BorderLayout());

        /*icon = new ImageIcon("img/animal.gif");
        icon.setImage(icon.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
        JLabel loadingLabel = new JLabel(icon);*/

        //JSVGCanvas svgCanvas = new JSVGCanvas();
        //svgCanvas.setURI("img/pie.svg");
        //svgCanvas.setBackground(new Color(0, 0, 0, 0));

        JLabel textLoadingLabel = new JLabel("Loading...");
        textLoadingLabel.setFont(new Font(GLOB_FONT.getName(), GLOB_FONT.getStyle(), 40));
        textLoadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLoadingLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel textLoadingPanel = new JPanel();
        textLoadingPanel.setLayout(new BoxLayout(textLoadingPanel, BoxLayout.X_AXIS));
        textLoadingPanel.add(Box.createRigidArea(new Dimension(0, 100)));
        textLoadingPanel.add(Box.createHorizontalGlue());
        textLoadingPanel.add(textLoadingLabel);
        textLoadingPanel.add(Box.createHorizontalGlue());
        textLoadingPanel.setOpaque(false);

        //loadingPanel.add(svgCanvas, BorderLayout.CENTER);
        loadingPanel.add(textLoadingPanel, BorderLayout.SOUTH);
    }

    public void setController(DashboardMainFrameController controller) {
        this.controller = controller;
    }

    public void submitFiles(Map<LogType, File> files) {
        this.controller.processFiles(files);
    }

    public void displayMetrics(Map<MetricType, Number> data) {
        data.forEach((type, value) -> metricsPanel.putMetricInPanel(type, value));
    }

    public void displayLoading() {
        this.loading = true;
        this.getGlassPane().setVisible(true);
        this.getContentPane().setEnabled(false);
        repaint();
    }

    public void finishedLoading() {
        this.loading = false;
        this.getGlassPane().setVisible(false);
        this.setEnabled(true);
        repaint();
    }
}
