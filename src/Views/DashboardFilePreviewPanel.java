package Views;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
    private JTextPane preview;

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
        preview = new JTextPane();
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

            // TODO make this method set the preview in the JTextPane
            if (chooserVal == JFileChooser.APPROVE_OPTION) {
                isLogSelected = true;
                String fileWithoutPath = chooser.getSelectedFile().getName();
                logFileName.setText(fileWithoutPath);
                repaint();
            }
        }
    }

    public boolean isLogSelected() {
        return isLogSelected;
    }
}
