import Controllers.DashboardMainFrameController;
import DataStructures.CsvInterfaces.Factory;
import DataStructures.CsvInterfaces.IServerLog;
import DataStructures.CsvPaserFactory;
import DataStructures.ServerLog;
import Views.DashboardMainFrame;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Factory factory = new CsvPaserFactory();
        try {
            String path = "server_log.csv";
            factory.open(ServerLog.class, path);
            ServerLog c;
            while ((c = (ServerLog) factory.next()) != null){
                  System.out.println(c.getEntryDate());
            }
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
