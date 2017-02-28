import Controllers.DashboardMainFrameController;
import Model.DatabaseModel;
import Views.DashboardMainFrame;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        DashboardMainFrame frame = new DashboardMainFrame(new File(System.getProperty("user.home")));
        DatabaseModel model = new DatabaseModel();
        DashboardMainFrameController controller = new DashboardMainFrameController(frame, model);
        //TODO resolve the circular dependency with the listener pattern
        frame.setController(controller);

        SwingUtilities.invokeLater(frame::init);
    }
}
