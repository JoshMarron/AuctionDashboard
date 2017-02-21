import Views.DashboardMainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DashboardMainFrame frame = new DashboardMainFrame();
        SwingUtilities.invokeLater(frame::init);
    }
}
