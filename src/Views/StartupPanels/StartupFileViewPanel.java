package Views.StartupPanels;

import Model.DBEnums.LogType;
import Views.CustomComponents.CatLabel;
import Views.CustomComponents.CatPanel;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * StartupFileViewPanel displays the files that have been chosen for each log type
 */
public class StartupFileViewPanel extends CatPanel {

    private LogType logtype;
    private CatLabel filename;

    public StartupFileViewPanel(LogType logtype) {
        this.logtype = logtype;
        this.initPanel();
    }

    private void initPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()));
        this.setMinimumSize(new Dimension(500, 0));

        filename = new CatLabel("No " + logtype.prettyPrint() + " currently chosen.");
        filename.setHorizontalAlignment(SwingConstants.CENTER);
        filename.setVerticalAlignment(SwingConstants.CENTER);
        CatLabel title = new CatLabel(logtype.prettyPrint());

        CatPanel titlePanel = new CatPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(Box.createRigidArea(new Dimension(10, 10)));
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        this.add(filename, BorderLayout.CENTER);
        this.add(titlePanel, BorderLayout.NORTH);
    }

    public void setFilename(File file) {
        filename.setText(file.getName());
    }
}
