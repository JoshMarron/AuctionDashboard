package Views.Deprecated;

import Model.DBEnums.LogType;
import Views.CustomComponents.CatButton;
import Views.CustomComponents.CatLabel;
import Views.DashboardMainFrame;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DashboardFileSelectPanel is a class containing the elements needed to choose a file from the given directory. Once
 * a file has been chosen, the JFileChooser is replaced with a preview of the file to ensure the correct one was
 * chosen.
 */
public class DashboardFileSelectPanel extends JPanel {

    private DashboardMainFrame parent;
    private File homeDir;                                               //The directory the file chooser should open at
    private ArrayList<DashboardFilePreviewPanel> previewPanels;         //The 3 preview panels should be collected

    public DashboardFileSelectPanel(File homeDir, DashboardMainFrame parent) {
        this.homeDir = homeDir;
        this.parent = parent;
        this.previewPanels = new ArrayList<>();
    }

    /**
     * Set up the FileSelectPanel
     */
    public void init() {
        this.setMaximumSize(new Dimension(600, 900));
        this.setBackground(ColorSettings.BG_COLOR.getColor());
        this.setLayout(new BorderLayout());

        //Create title label and panel for it to go on
        CatLabel title = new CatLabel("Choose your log files");
        title.setFont(DashboardMainFrame.GLOB_FONT);
        title.setFont(new Font(title.getFont().getName(), title.getFont().getStyle(), 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        //Panel to allow for nicely centering the title
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(Box.createRigidArea(new Dimension(0, 70)));
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());
        this.add(titlePanel, BorderLayout.NORTH);

        //Create buttons and panel for them to go on
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        CatButton confirm = new CatButton("Confirm");
        confirm.setFont(DashboardMainFrame.GLOB_FONT);
        confirm.addActionListener(new ConfirmListener());

        //Panel to allow for nicely centering the button - will be able to add more later
        buttonPanel.add(Box.createRigidArea(new Dimension(100, 100)));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(confirm);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(Box.createHorizontalStrut(100));
        this.add(buttonPanel, BorderLayout.SOUTH);

        //Create the panel for the three preview and choose panels
        JPanel chooseAndPreviewPanel = new JPanel();
        chooseAndPreviewPanel.setLayout(new GridLayout(3, 1));
        DashboardFilePreviewPanel impressionChoose = new DashboardFilePreviewPanel(LogType.IMPRESSION, homeDir);
        DashboardFilePreviewPanel clickChoose = new DashboardFilePreviewPanel(LogType.CLICK, homeDir);
        DashboardFilePreviewPanel serverChoose = new DashboardFilePreviewPanel(LogType.SERVER_LOG, homeDir);

        previewPanels.add(impressionChoose);
        previewPanels.add(clickChoose);
        previewPanels.add(serverChoose);

        chooseAndPreviewPanel.add(impressionChoose);
        chooseAndPreviewPanel.add(clickChoose);
        chooseAndPreviewPanel.add(serverChoose);

        this.add(chooseAndPreviewPanel, BorderLayout.CENTER);

    }

    /**
     * Allows the confirm button to submit the files to the controller.
     */
    class ConfirmListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //Filter the panels list based on whether a file has been selected within it
            //Get the files and put them in a list to pass to the controller
            Stream<DashboardFilePreviewPanel> panels = previewPanels.stream().filter(DashboardFilePreviewPanel::isLogSelected);
            Map<LogType, File> files = panels.collect(Collectors.toMap( DashboardFilePreviewPanel::getLogType,DashboardFilePreviewPanel::getSelectedFile));

            if (!files.isEmpty()) {
                parent.submitFiles(files);
            }
            else {
                JOptionPane.showMessageDialog(DashboardFileSelectPanel.this,
                        "You must add at least one type of log.",
                        "No files chosen", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
