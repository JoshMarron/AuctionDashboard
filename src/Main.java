import Controllers.DashboardMainFrameController;
import Views.DashboardMainFrame;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        DashboardMainFrame frame = new DashboardMainFrame(new File(System.getProperty("user.home")));
        DashboardMainFrameController controller = new DashboardMainFrameController(frame);
        //TODO resolve the circular dependency with the listener pattern
        frame.setController(controller);

        SwingUtilities.invokeLater(frame::init);
    }
}
