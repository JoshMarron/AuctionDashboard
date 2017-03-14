package Views.MainFramePanels;

import Views.CustomComponents.CatMenu;
import Views.CustomComponents.CatMenuBar;
import Views.DashboardMainFrame;

import javax.swing.*;

/**
 * Created by marro on 14/03/2017.
 */
public class MainFrameMenu extends CatMenuBar {

    private DashboardMainFrame frame;

    public MainFrameMenu(DashboardMainFrame frame) {
        this.frame = frame;
        this.init();
    }

    private void init() {
        CatMenu fileMenu = new CatMenu("File");
        JMenuItem saveAsMenuItem = new JMenuItem("Save as...");
        saveAsMenuItem.addActionListener((e) -> frame.saveFileAs());
        JMenuItem saveMenuItem = new JMenuItem("Save");

        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);

        this.add(fileMenu);
    }


}
