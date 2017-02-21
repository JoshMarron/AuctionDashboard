import Views.DashboardMainFrame;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        DashboardMainFrame frame = new DashboardMainFrame(new File(System.getProperty("user.home")));
        SwingUtilities.invokeLater(frame::init);
    }
}
