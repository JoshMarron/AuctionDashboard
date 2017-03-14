package Views.StartupPanels;

import Views.CustomComponents.CatLabel;
import Views.CustomComponents.CatListPanel;
import Views.CustomComponents.CatPanel;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * RecentProjectInfoPanel displays info for one recent project. It is clickable, allowing the user to load a recent project
 */
public class RecentProjectInfoPanel extends CatListPanel {

    private File file;
    private RecentProjectsViewPanel parent;

    public RecentProjectInfoPanel(File file, RecentProjectsViewPanel parent) {
        this.file = file;
        this.parent = parent;
        this.initInfoPanel();
    }

    private void initInfoPanel() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        long lastModified = file.lastModified();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedLastModified = dateFormat.format(new Date(lastModified));

        CatLabel projectNameLabel = new CatLabel(file.getName().split("\\.")[0]);
        projectNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        CatLabel projectDateLabel = new CatLabel("Last edited: " + formattedLastModified);
        projectDateLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        this.add(Box.createRigidArea(new Dimension(0, 30)));
        this.add(projectNameLabel);
        this.add(projectDateLabel);
        this.add(Box.createHorizontalGlue());
        this.add(Box.createRigidArea(new Dimension(0, 30)));

        this.addMouseListener(new RecentProjectInfoAdapter());
    }

    class RecentProjectInfoAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            RecentProjectInfoPanel.this.parent.chooseRecentProject(file);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            RecentProjectInfoPanel.this.setBackground(ColorSettings.BUTTON_HOVER_COLOR.getColor());
            RecentProjectInfoPanel.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            RecentProjectInfoPanel.this.setBackground(ColorSettings.BG_COLOR.getColor());
            repaint();
        }
    }
}
