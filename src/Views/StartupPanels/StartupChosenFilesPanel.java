package Views.StartupPanels;

import Model.DBEnums.LogType;
import Views.CustomComponents.CatPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by marro on 11/03/2017.
 */
public class StartupChosenFilesPanel extends CatPanel {

    Map<LogType, File> chosenFileMap;

    public StartupChosenFilesPanel() {
        chosenFileMap = new HashMap<>();
        this.initChosenFilesPanel();
    }

    private void initChosenFilesPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        StartUpFileViewPanel impressionPanel = new StartUpFileViewPanel(LogType.IMPRESSION);
        StartUpFileViewPanel clickPanel = new StartUpFileViewPanel(LogType.CLICK);
        StartUpFileViewPanel serverPanel = new StartUpFileViewPanel(LogType.SERVER_LOG);

        this.add(Box.createVerticalGlue());
        this.add(impressionPanel);
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(clickPanel);
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(serverPanel);
        this.add(Box.createVerticalGlue());

    }

}
