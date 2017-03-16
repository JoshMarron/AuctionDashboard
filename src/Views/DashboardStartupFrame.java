package Views;

import Controllers.DashboardStartupController;
import Model.DBEnums.LogType;
import Views.CustomComponents.*;
import Views.StartupPanels.RecentProjectsViewPanel;
import Views.StartupPanels.StartupChosenFilesPanel;
import Views.StartupPanels.StartupFileImportPanel;
import Views.StartupPanels.StartupMenu;
import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * DashboardStartupFrame is the first frame the user sees, containing the elements needed to load in files
 */
public class DashboardStartupFrame extends CatFrame {

    private Map<LogType, File> fileMap;
    private StartupChosenFilesPanel viewPanel;
    private DashboardStartupController controller;
    private StartupFileImportPanel importPanel;

    public DashboardStartupFrame(File homedir) {
        this.homedir = homedir;
        fileMap = new HashMap<>();
    }

    public void initStartup() {
        super.init();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error getting System Look and Feel, reverting to Java default",
                    "Graphical Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        //Try to use anti-aliasing on the font
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        //Try to make ComboBox look less awful
        UIManager.put("ComboBox.background", new ColorUIResource(ColorSettings.TEXT_AREA_BG_COLOR.getColor()));
        UIManager.put("ComboBox.foreground", new ColorUIResource(ColorSettings.TEXT_AREA_TEXT_COLOR.getColor()));
        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(ColorSettings.TEXT_AREA_BG_COLOR.getColor().brighter()));
        UIManager.put("ComboBox.selectionForeground", new ColorUIResource(ColorSettings.TEXT_AREA_TEXT_COLOR.getColor()));

        this.setTitle("CatAnalysis");
        this.setSize(new Dimension(1250, 700));
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CatPanel contentPane = (CatPanel) this.getContentPane();

        CatPanel centrePanel = new CatPanel();
        centrePanel.setLayout(new GridBagLayout());

        importPanel = new StartupFileImportPanel(homedir, this);
        GridBagConstraints importPanelConstraints = new GridBagConstraints();
        importPanelConstraints.gridx = 1;
        importPanelConstraints.weightx = 100F;
        importPanelConstraints.weighty = 1;
        importPanelConstraints.fill = GridBagConstraints.BOTH;

        viewPanel = new StartupChosenFilesPanel(this);
        GridBagConstraints viewPanelConstraints = new GridBagConstraints();
        viewPanelConstraints.gridx = 2;
        viewPanelConstraints.weightx = 1;
        viewPanelConstraints.weighty = 1;
        viewPanelConstraints.fill = GridBagConstraints.BOTH;

        List<File> fileList = new ArrayList<>();
        try {
            fileList = controller.getRecentProjects();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "Recent projects file is missing or corrupted.",
                    "Recent projects error!",
                    JOptionPane.ERROR_MESSAGE);
        }

        RecentProjectsViewPanel recentProjects = new RecentProjectsViewPanel(fileList, this);
        GridBagConstraints recentProjectsConstraints = new GridBagConstraints();
        recentProjectsConstraints.gridx = 0;
        recentProjectsConstraints.weightx = 5;
        recentProjectsConstraints.weighty = 1;
        recentProjectsConstraints.fill = GridBagConstraints.BOTH;

        centrePanel.add(recentProjects, recentProjectsConstraints);
        centrePanel.add(importPanel, importPanelConstraints);
        centrePanel.add(viewPanel, viewPanelConstraints);

        CatPanel titlePanel = new CatPanel();
        CatLabel titleLabel = new CatLabel("CatAnalysis Advertising Dashboard");
        titleLabel.setFont(titleLabel.getFont().deriveFont(60F));
        titlePanel.add(titleLabel);

        StartupMenu menu = new StartupMenu(this);
        this.setJMenuBar(menu);

        contentPane.add(centrePanel, BorderLayout.CENTER);
        contentPane.add(titlePanel, BorderLayout.NORTH);
        this.initGlassPane();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void fileChosen(LogType logType, File file) {
        this.fileMap.put(logType, file);
        this.viewPanel.setLogPanelName(logType, file);
    }

    public void submitFiles() {
        if (this.fileMap.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "You must submit at least one log file!",
                    "Empty submission error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            String result = JOptionPane.showInputDialog(this,
                    "Choose a name for your project",
                    "Choose a name!",
                    JOptionPane.QUESTION_MESSAGE);
            if (result != null) {
                this.controller.processFiles(this.fileMap, result);
            }
        }
    }


    public void reselectFile(LogType type, File file) {
        this.importPanel.setUpReselection(type, file);
    }

    public void setController(DashboardStartupController controller) {
        this.controller = controller;
    }

    public void chooseRecentProject(File recentFile) {
        int chooseVal = JOptionPane.showConfirmDialog(this, "Load " + recentFile.getName() + "?");
        if (chooseVal == JOptionPane.OK_OPTION) {
            this.controller.loadOldProject(recentFile);
        }
    }

    public void selectProjectFromFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a project file...");
        FileNameExtensionFilter catFilter = new FileNameExtensionFilter("CAT Files", "cat");
        chooser.setFileFilter(catFilter);

        int chooserVal = chooser.showOpenDialog(this);

        if (chooserVal == JFileChooser.APPROVE_OPTION) {
            File filename = chooser.getSelectedFile();
            this.chooseRecentProject(filename);
        }
    }

    public void exit() {
        System.exit(0);
    }
}
