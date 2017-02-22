package Views;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
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
public class DashboardFilePreviewPanel extends JPanel {

    private File homeDir;
    private LogType logType; //Impression, click or server
    private JFileChooser chooser;
    private boolean isLogSelected; //True if a file has been selected
    private JLabel logFileName;
    private JTextArea preview;
    private File selectedFile;

    public DashboardFilePreviewPanel(LogType logType, File homeDir) {
        this.logType = logType;
        this.isLogSelected = false;
        this.homeDir = homeDir;
        this.init();
    }

    public void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel fileChoosePanel = new JPanel();
        fileChoosePanel.setLayout(new BoxLayout(fileChoosePanel, BoxLayout.X_AXIS));
        JLabel logName = new JLabel(this.logType.toString() + ":");
        logFileName = new JLabel("None chosen");
        JButton logChooseButton = new JButton("Choose " + this.logType.toString() + "...");
        logChooseButton.addActionListener(new chooseButtonListener());

        fileChoosePanel.add(logName);
        fileChoosePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        fileChoosePanel.add(logFileName);
        fileChoosePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        fileChoosePanel.add(logChooseButton);

        JPanel previewTextPanel = new JPanel();
        previewTextPanel.setLayout(new BorderLayout());
        preview = new JTextArea();
        preview.setEditable(false);

        previewTextPanel.add(preview, BorderLayout.CENTER);

        //Instantiate JFileChooser which will appear when buttons are clicked
        chooser = new JFileChooser(homeDir);
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Files", "csv");
        chooser.setFileFilter(csvFilter);

        this.add(fileChoosePanel);
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(previewTextPanel);

    }

    class chooseButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int chooserVal = chooser.showOpenDialog(DashboardFilePreviewPanel.this);

            // If a file is chosen, set the preview, the chosen file, and the selected flag
            if (chooserVal == JFileChooser.APPROVE_OPTION) {
                isLogSelected = true;

                selectedFile = chooser.getSelectedFile();
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
            fileStream.limit(10).forEach((str) -> {preview.append(str + '\n');});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
