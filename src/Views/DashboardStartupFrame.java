package Views;

import Views.CustomComponents.CatPanel;
import Views.StartupPanels.RecentProjectsViewPanel;
import Views.StartupPanels.StartUpFileViewPanel;
import Views.StartupPanels.StartupChosenFilesPanel;
import Views.StartupPanels.StartupFileImportPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * DashboardStartupFrame is the first frame the user sees, containing the elements needed to load in files
 */
public class DashboardStartupFrame extends JFrame {

    private File homedir;

    public DashboardStartupFrame(File homedir) {
        this.homedir = homedir;
    }

    public void initStartup() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error getting System Look and Feel, reverting to Java default",
                    "Graphical Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        this.setSize(new Dimension(1000, 600));
        //this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CatPanel contentPane = new CatPanel();
        contentPane.setLayout(new BorderLayout());
        this.setContentPane(contentPane);

        CatPanel centrePanel = new CatPanel();
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.X_AXIS));

        StartupFileImportPanel importPanel = new StartupFileImportPanel(homedir);
        StartupChosenFilesPanel viewPanel = new StartupChosenFilesPanel();
        RecentProjectsViewPanel recentProjects = new RecentProjectsViewPanel(null);

        centrePanel.add(Box.createHorizontalGlue());
        centrePanel.add(recentProjects);
        centrePanel.add(Box.createHorizontalGlue());
        centrePanel.add(importPanel);
        centrePanel.add(viewPanel);
        centrePanel.add(Box.createHorizontalGlue());

        contentPane.add(centrePanel, BorderLayout.CENTER);
        this.setVisible(true);
    }
}
