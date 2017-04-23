package Views.StartupPanels;

import Model.DBEnums.LogType;
import Views.CustomComponents.CatLabel;
import Views.CustomComponents.CatPanel;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * StartupFileViewPanel displays the files that have been chosen for each log type
 */
public class StartupFileViewPanel extends CatPanel {

    private LogType logtype;
    private CatLabel filename;
    private StartupChosenFilesPanel parent;
    private File file;

    public StartupFileViewPanel(LogType logtype, StartupChosenFilesPanel parent) {
        this.logtype = logtype;
        this.parent = parent;
        this.initPanel();
    }

    private void initPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR));
        this.setMinimumSize(new Dimension(300, 0));

        filename = new CatLabel("No " + logtype.prettyPrint() + " currently chosen.");
        filename.setHorizontalAlignment(SwingConstants.CENTER);
        filename.setVerticalAlignment(SwingConstants.CENTER);
        CatLabel title = new CatLabel(logtype.prettyPrint());

        CatPanel titlePanel = new CatPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(Box.createRigidArea(new Dimension(10, 10)));
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        this.addMouseListener(new PanelMouseListener());
        this.add(filename, BorderLayout.CENTER);
        this.add(titlePanel, BorderLayout.NORTH);
    }

    public void setFile(File file) {
        this.file = file;
        filename.setText(file.getName());
    }

    private void setReselection() {
        this.parent.reselectFile(this.logtype, this.file);
    }

    private boolean isFileSelected() {
        return file != null;
    }

    class PanelMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (StartupFileViewPanel.this.isFileSelected()) {
                StartupFileViewPanel.this.setReselection();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (StartupFileViewPanel.this.isFileSelected()) {
                StartupFileViewPanel.this.setBackground(ColorSettings.BUTTON_HOVER_COLOR);
                StartupFileViewPanel.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                repaint();
            }
            else {
                StartupFileViewPanel.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            StartupFileViewPanel.this.setBackground(ColorSettings.BG_COLOR);
            repaint();
        }
    }
}
