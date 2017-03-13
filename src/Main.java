import Controllers.DashboardMainFrameController;
import Controllers.DashboardStartupController;
import Model.DatabaseManager;
import Views.DashboardMainFrame;
import Views.DashboardStartupFrame;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        DatabaseManager model = new DatabaseManager();
        model.init();
        model.initTables();

        //DashboardStartupFrame startupFrame = new DashboardStartupFrame(new File(System.getProperty("user.home")));
        DashboardMainFrame mainFrame = new DashboardMainFrame(new File(System.getProperty("user.home")));
        DashboardMainFrameController mainController = new DashboardMainFrameController(mainFrame, model);
        //DashboardStartupController startupController = new DashboardStartupController(startupFrame, model, mainController);
        //startupFrame.setController(startupController);
        mainFrame.setController(mainController);
        //SwingUtilities.invokeLater(startupFrame::initStartup);
        SwingUtilities.invokeLater(mainFrame::init);
        mainFrame.setVisible(true);
    }
}
