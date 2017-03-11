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

        this.setSize(new Dimension(1250, 600));
        //this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CatPanel contentPane = new CatPanel();
        contentPane.setLayout(new BorderLayout());
        this.setContentPane(contentPane);

        CatPanel centrePanel = new CatPanel();
        centrePanel.setLayout(new GridBagLayout());

        StartupFileImportPanel importPanel = new StartupFileImportPanel(homedir);
        GridBagConstraints importPanelConstraints = new GridBagConstraints();
        importPanelConstraints.gridx = 1;
        importPanelConstraints.weightx = 10;
        importPanelConstraints.weighty = 1;
        importPanelConstraints.fill = GridBagConstraints.BOTH;

        StartupChosenFilesPanel viewPanel = new StartupChosenFilesPanel();
        GridBagConstraints viewPanelConstraints = new GridBagConstraints();
        viewPanelConstraints.gridx = 2;
        viewPanelConstraints.weightx = 1;
        viewPanelConstraints.weighty = 1;
        viewPanelConstraints.fill = GridBagConstraints.BOTH;

        RecentProjectsViewPanel recentProjects = new RecentProjectsViewPanel(null);
        GridBagConstraints recentProjectsConstraints = new GridBagConstraints();
        recentProjectsConstraints.gridx = 0;
        recentProjectsConstraints.weightx = 1;
        recentProjectsConstraints.weighty = 1;
        recentProjectsConstraints.fill = GridBagConstraints.BOTH;

        centrePanel.add(recentProjects, recentProjectsConstraints);
        centrePanel.add(importPanel, importPanelConstraints);
        centrePanel.add(viewPanel, viewPanelConstraints);

        contentPane.add(centrePanel, BorderLayout.CENTER);
        this.setVisible(true);
    }
}
