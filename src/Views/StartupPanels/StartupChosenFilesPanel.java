package Views.StartupPanels;

import Model.DBEnums.LogType;
import Views.CustomComponents.CatPanel;
import Views.CustomComponents.CatTitlePanel;
import Views.DashboardStartupFrame;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * StartupChosenFilesPanel displays the files that the user has chosen to load into the application - clicking them
 * should allow the user to change the file
 */
public class StartupChosenFilesPanel extends CatPanel {

    private Map<LogType, StartupFileViewPanel> chosenFilePanelMap;
    private DashboardStartupFrame parentFrame;

    public StartupChosenFilesPanel(DashboardStartupFrame parentFrame) {
        this.parentFrame = parentFrame;
        chosenFilePanelMap = new HashMap<>();
        this.initChosenFilesPanel();

    }

    private void initChosenFilesPanel() {

        Border inside = BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR);
        this.setBorder(BorderFactory.createCompoundBorder(inside, BorderFactory.createEmptyBorder(0, 20, 20, 20)));

        this.setLayout(new BorderLayout());

        CatTitlePanel title = new CatTitlePanel("Your chosen files");

        JPanel centrePanel = new JPanel();
        centrePanel.setBackground(ColorSettings.BG_COLOR);
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));

        StartupFileViewPanel impressionPanel = new StartupFileViewPanel(LogType.IMPRESSION, this);
        StartupFileViewPanel clickPanel = new StartupFileViewPanel(LogType.CLICK, this);
        StartupFileViewPanel serverPanel = new StartupFileViewPanel(LogType.SERVER_LOG, this);
        chosenFilePanelMap.put(LogType.IMPRESSION, impressionPanel);
        chosenFilePanelMap.put(LogType.CLICK, clickPanel);
        chosenFilePanelMap.put(LogType.SERVER_LOG, serverPanel);

        centrePanel.add(Box.createVerticalGlue());
        centrePanel.add(impressionPanel);
        centrePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centrePanel.add(clickPanel);
        centrePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centrePanel.add(serverPanel);
        centrePanel.add(Box.createVerticalGlue());

        this.add(title, BorderLayout.NORTH);
        this.add(centrePanel, BorderLayout.CENTER);

    }

    public void setLogPanelName(LogType log, File file) {
        chosenFilePanelMap.get(log).setFile(file);
    }

    public void reselectFile(LogType type, File file) {
        this.parentFrame.reselectFile(type, file);
    }

}
