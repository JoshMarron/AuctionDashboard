package Views;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * DashboardFileSelectPanel is a class containing the elements needed to choose a file from the given directory. Once
 * a file has been chosen, the JFileChooser is replaced with a preview of the file to ensure the correct one was
 * chosen.
 */
public class DashboardFileSelectPanel extends JPanel {

    private File homeDir;

    public DashboardFileSelectPanel(File homeDir) {
        this.homeDir = homeDir;
    }

    public void init() {
        this.setMaximumSize(new Dimension(500, 900));
        this.setLayout(new BorderLayout());

        //Create title label and panel for it to go on
        JLabel title = new JLabel("Choose your log files");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(Box.createRigidArea(new Dimension(0, 70)));
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());
        this.add(titlePanel, BorderLayout.NORTH);

        //Create buttons and panel for them to go on
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton submit = new JButton("Confirm");

        buttonPanel.add(Box.createRigidArea(new Dimension(100, 100)));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(submit);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(Box.createHorizontalStrut(100));
        this.add(buttonPanel, BorderLayout.SOUTH);

        //Create the panel for the three preview and choose panels
        JPanel chooseAndPreviewPanel = new JPanel();
        chooseAndPreviewPanel.setLayout(new GridLayout(3, 1));
        DashboardFilePreviewPanel impressionChoose = new DashboardFilePreviewPanel(LogType.IMPRESSION, homeDir);
        DashboardFilePreviewPanel clickChoose = new DashboardFilePreviewPanel(LogType.CLICK, homeDir);
        DashboardFilePreviewPanel serverChoose = new DashboardFilePreviewPanel(LogType.SERVER, homeDir);

        chooseAndPreviewPanel.add(impressionChoose);
        chooseAndPreviewPanel.add(clickChoose);
        chooseAndPreviewPanel.add(serverChoose);

        /*chooseAndPreviewPanel.add(impressionChoose);
        chooseAndPreviewPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        chooseAndPreviewPanel.add(clickChoose);
        chooseAndPreviewPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        chooseAndPreviewPanel.add(serverChoose);*/

        this.add(chooseAndPreviewPanel, BorderLayout.CENTER);

    }

}
