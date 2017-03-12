import Controllers.DashboardMainFrameController;
import Controllers.DashboardStartupController;
import Model.DatabaseManager;
import Views.DashboardMainFrame;
import Views.DashboardStartupFrame;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {

        DashboardMainFrame frame = new DashboardMainFrame(new File(System.getProperty("user.home")));
        DatabaseManager model = new DatabaseManager();
        model.init();
        model.initTables();
        DashboardMainFrameController controller = new DashboardMainFrameController(frame, model);
        //TODO resolve the circular dependency with the listener pattern
        frame.setController(controller);
        SwingUtilities.invokeLater(frame::init);
        DashboardStartupFrame startupFrame = new DashboardStartupFrame(new File(System.getProperty("user.home")));
        DashboardStartupController startupController = new DashboardStartupController(startupFrame, model, controller);
        startupFrame.setController(startupController);
        SwingUtilities.invokeLater(startupFrame::initStartup);
    }
}
