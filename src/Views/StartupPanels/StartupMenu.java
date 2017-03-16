package Views.StartupPanels;

import Views.CustomComponents.CatMenu;
import Views.CustomComponents.CatMenuBar;
import Views.DashboardStartupFrame;

import javax.swing.*;

/**
 * StartupMenu is the MenuBar for the startup screen
 */
public class StartupMenu extends CatMenuBar {

    private DashboardStartupFrame frame;

    public StartupMenu(DashboardStartupFrame frame) {
        this.frame = frame;
        this.init();
    }

    private void init() {

        CatMenu menu = new CatMenu("File");
        JMenuItem open = new JMenuItem("Open...");
        open.addActionListener((e) -> frame.selectProjectFromFolder());

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener((e) -> frame.exit());

        menu.add(open);
        menu.add(exit);

        this.add(menu);
    }

}
