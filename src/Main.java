import Controllers.DashboardMainFrameController;
import DataStructures.ClickLog;
import DataStructures.CsvInterfaces.IClickLog;
import DataStructures.CsvPaserFactory;
import Views.DashboardMainFrame;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CsvPaserFactory factory = new CsvPaserFactory();
        try {
            String path = "/home/rhys/SegProj/AuctionDashboard/click_log.csv";
            factory.open(ClickLog.class, path);
            IClickLog c = (IClickLog) factory.next();

            System.out.println(c);
            factory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DashboardMainFrame frame = new DashboardMainFrame(new File(System.getProperty("user.home")));
        DashboardMainFrameController controller = new DashboardMainFrameController(frame);
        //TODO resolve the circular dependency with the listener pattern
        frame.setController(controller);

        SwingUtilities.invokeLater(frame::init);
    }
}
