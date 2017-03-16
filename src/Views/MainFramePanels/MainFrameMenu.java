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

        JMenuItem closeProjectItem = new JMenuItem("Close Project");
        closeProjectItem.addActionListener((e) -> frame.closeProject());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener((e) -> System.exit(0));

        fileMenu.add(saveAsMenuItem);
        fileMenu.add(closeProjectItem);
        fileMenu.add(exitItem);

        this.add(fileMenu);
    }


}
