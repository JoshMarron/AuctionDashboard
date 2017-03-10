package Views.Deprecated;

import Model.DBEnums.LogType;
import Views.DashboardMainFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * DashboardFilePreviewPanel allows the user to select a file with a file chooser, see its name, and
 * view the first lines of the file to check they chose the right one
 */
@Deprecated
public class DashboardFilePreviewPanel extends JPanel {

    private File homeDir;
    private LogType logType;            //Impression, click or server
    private JFileChooser chooser;
    private boolean isLogSelected;      //True if a file has been selected
    private JLabel logFileName;
    private JTextArea preview;
    private File selectedFile;

    public DashboardFilePreviewPanel(LogType logType, File homeDir) {
        this.logType = logType;
        this.isLogSelected = false;
        this.homeDir = homeDir;
        this.init();
    }

    /**
     * Set up the FilePreviewPanel
     */
    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(DashboardMainFrame.BG_COLOR);

        //Set up the panel which contains the button and the final name
        JPanel fileChoosePanel = new JPanel();
        fileChoosePanel.setLayout(new BoxLayout(fileChoosePanel, BoxLayout.X_AXIS));
        fileChoosePanel.setOpaque(false);
        fileChoosePanel.setBackground(DashboardMainFrame.BG_COLOR);
        JLabel logName = new JLabel(this.logType.prettyPrint() + ":");
        logName.setFont(DashboardMainFrame.GLOB_FONT);

        logFileName = new JLabel("None chosen");
        logFileName.setFont(DashboardMainFrame.GLOB_FONT);

        JButton logChooseButton = new JButton("Choose " + this.logType.prettyPrint() + "...");
        logChooseButton.addActionListener(new ChooseButtonListener());
        logChooseButton.setFont(DashboardMainFrame.GLOB_FONT);

        fileChoosePanel.add(logName);
        fileChoosePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        fileChoosePanel.add(logFileName);
        fileChoosePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        fileChoosePanel.add(logChooseButton);

        //Create the panel which holds the preview in a text area
        JPanel previewTextPanel = new JPanel();
        previewTextPanel.setLayout(new BorderLayout());
        preview = new JTextArea();
        preview.setEditable(false);

        previewTextPanel.add(preview, BorderLayout.CENTER);

        //Instantiate JFileChooser which will appear when buttons are clicked
        chooser = new JFileChooser(homeDir);
        chooser.setDialogTitle("Choose " + this.logType.prettyPrint() + "...");
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Files", "csv");
        chooser.setFileFilter(csvFilter);

        this.add(fileChoosePanel);
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(previewTextPanel);

    }

    /**
     * Listener which is attached to the choose button and opens up the file chooser.
     * Performs a sanity check on the extension of the file to check the user definitely chooses a CSV file
     */
    class ChooseButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int chooserVal = chooser.showOpenDialog(DashboardFilePreviewPanel.this);

            // If a file is chosen, set the preview, the chosen file, and the selected flag
            if (chooserVal == JFileChooser.APPROVE_OPTION) {
                isLogSelected = true;

                selectedFile = chooser.getSelectedFile();
                String extension = selectedFile.getName().split("\\.")[1];

                //Check the extension of the file is .csv
                if (!extension.equalsIgnoreCase("csv")) {
                    JOptionPane.showMessageDialog(DashboardFilePreviewPanel.this,
                            "File must be of type CSV",
                            "Wrong Filetype",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                logFileName.setText(selectedFile.getName());
                DashboardFilePreviewPanel.this.setPreview(selectedFile);

                repaint();
            }
        }
    }

    public boolean isLogSelected() {
        return isLogSelected;
    }

    public File getSelectedFile() {
        return this.selectedFile;
    }

    private void setPreview(File selected) {

        try (Stream<String> fileStream = Files.lines(Paths.get(selected.getPath()))) {
            fileStream.limit(10).forEach((str) -> preview.append(str + '\n'));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LogType getLogType() {
        return this.logType;
    }
}
