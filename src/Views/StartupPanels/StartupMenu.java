package Views.StartupPanels;

import Views.CustomComponents.CatMenu;
import Views.CustomComponents.CatMenuBar;
import Views.DashboardStartupFrame;

import javax.swing.*;

/**
 * Created by marro on 15/03/2017.
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
        menu.add(open);

        this.add(menu);
    }

}
