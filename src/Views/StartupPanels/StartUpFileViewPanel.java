package Views.StartupPanels;

import Model.DBEnums.LogType;
import Views.CustomComponents.CatLabel;
import Views.CustomComponents.CatPanel;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import java.awt.*;

/**
 * Created by marro on 11/03/2017.
 */
public class StartUpFileViewPanel extends CatPanel {

    private LogType logtype;
    private CatLabel filename;

    public StartUpFileViewPanel(LogType logtype) {
        this.logtype = logtype;
        this.initPanel();
    }

    private void initPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()));

        filename = new CatLabel("No " + logtype.prettyPrint() + " currently chosen.");
        filename.setHorizontalAlignment(SwingConstants.CENTER);
        filename.setVerticalAlignment(SwingConstants.CENTER);
        CatLabel title = new CatLabel(logtype.prettyPrint());

        CatPanel titlePanel = new CatPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(Box.createRigidArea(new Dimension(20, 40)));
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        this.add(filename, BorderLayout.CENTER);
        this.add(titlePanel, BorderLayout.NORTH);
    }
}
