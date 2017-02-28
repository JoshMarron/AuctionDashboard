import Controllers.DashboardMainFrameController;
import Model.DatabaseManager;
import Views.DashboardMainFrame;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {

        DashboardMainFrame frame = new DashboardMainFrame(new File(System.getProperty("user.home")));
        DatabaseManager model = new DatabaseManager();
//        model.init();
        model.initTables();
//	    model.insertData(null, null);
        DashboardMainFrameController controller = new DashboardMainFrameController(frame, model);
        //TODO resolve the circular dependency with the listener pattern
        frame.setController(controller);

        SwingUtilities.invokeLater(frame::init);
    }
}
