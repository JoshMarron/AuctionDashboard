package Views.StartupPanels;

import Views.CustomComponents.CatLabel;
import Views.CustomComponents.CatPanel;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * RecentProjectInfoPanel displays info for one recent project. It is clickable, allowing the user to load a recent project
 */
public class RecentProjectInfoPanel extends CatPanel {

    private File file;

    public RecentProjectInfoPanel(File file) {
        this.file = file;
        this.initInfoPanel();
    }

    private void initInfoPanel() {
        this.setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(1000, 100));
        Border outside = BorderFactory.createEmptyBorder(20, 0, 20, 0);
        Border inside = BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor());
        this.setBorder(BorderFactory.createCompoundBorder(outside, inside));

        long lastModified = file.lastModified();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
        String formattedLastModified = dateFormat.format(lastModified);

        CatPanel projectNamePanel = new CatPanel();
        CatLabel projectNameLabel = new CatLabel(file.getName().split("\\.")[0]);
        CatLabel projectDateLabel = new CatLabel("Last edited: " + formattedLastModified);

        projectNamePanel.setLayout(new BoxLayout(projectNamePanel, BoxLayout.Y_AXIS));
        projectNamePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        projectNamePanel.add(Box.createVerticalGlue());
        projectNamePanel.add(projectNameLabel);
        projectNamePanel.add(projectDateLabel);
        projectNamePanel.add(Box.createVerticalGlue());

        this.add(projectNamePanel, BorderLayout.CENTER);
    }
}
