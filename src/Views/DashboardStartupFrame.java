package Views;

import Views.CustomComponents.CatPanel;
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

        this.setSize(new Dimension(800, 600));
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CatPanel contentPane = new CatPanel();
        contentPane.setLayout(new BorderLayout());
        this.setContentPane(contentPane);

        StartupFileImportPanel importPanel = new StartupFileImportPanel(homedir);
        contentPane.add(importPanel, BorderLayout.CENTER);

        StartupChosenFilesPanel viewPanel = new StartupChosenFilesPanel();
        contentPane.add(viewPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }
}
