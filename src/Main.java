import Controllers.DashboardMainFrameController;
import Model.DatabaseManager;
import Views.DashboardMainFrame;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

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
    }
}
