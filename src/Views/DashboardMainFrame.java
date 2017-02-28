package Views;

import Controllers.DashboardMainFrameController;

import Model.LogType;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

/**
 * DashboardMainFrame is the main frame visible during the running of the application, which contains all of the
 * GUI elements.
 */
public class DashboardMainFrame extends JFrame {

    public static final Color BG_COLOR = new Color(184, 200, 209);
    public static final Font GLOB_FONT = new Font("Tahoma", Font.BOLD, 14);

    private File homeDir;
    private DashboardMainFrameController controller;
    private DashboardMetricsPanel metricsPanel;
    private boolean loading;

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
                    System.out.println();
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

        this.setVisible(true);
    }

    public void setController(DashboardMainFrameController controller) {
        this.controller = controller;
    }

    public void submitFiles(Map<LogType, File> files) {
        this.controller.processFiles(files);
    }

    public void displayMetrics(Map<String, Double> data) {
        data.forEach((name, value) -> metricsPanel.putMetricInTextList(name, value));
    }

    public void displayLoading() {
        this.loading = true;
        JPanel loadingPanel = (JPanel) this.getGlassPane();
        loadingPanel.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("C:\\Users\\marro\\IdeaProjects\\AuctionDashboard\\img\\ripple.gif");
        JLabel loadingLabel = new JLabel(icon);

        loadingPanel.add(loadingLabel, BorderLayout.CENTER);
        this.getGlassPane().setVisible(true);
        this.setEnabled(false);
        repaint();
    }

    public void finishedLoading() {
        this.loading = false;
        this.getGlassPane().setVisible(false);
        this.setEnabled(true);
        repaint();
    }
}
