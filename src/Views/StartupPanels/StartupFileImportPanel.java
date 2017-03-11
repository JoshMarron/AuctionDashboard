package Views.StartupPanels;

import Model.DBEnums.LogType;
import Views.CustomComponents.*;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * StartupFileImportPanel allows the user to select a file with a file chooser, view a small preview and see which
 * log it corresponds to
 */
public class StartupFileImportPanel extends CatPanel {

    private File homedir;
    private File selectedFile;
    private CatFileChooser chooser;
    private CatTextArea previewText;
    private CatComboBox<LogType> logTypeComboBox;

    public StartupFileImportPanel(File homedir) {
        this.homedir = homedir;
        this.initFileImportPanel();
    }

    private void initFileImportPanel() {

        this.setLayout(new BorderLayout());
        this.chooser = new CatFileChooser(homedir);

        CatPanel buttonPanel = new CatPanel();
        CatButton popChooserButton = new CatButton("Choose log...");
        popChooserButton.addActionListener(new ChooseButtonListener());
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(popChooserButton);
        buttonPanel.add(Box.createHorizontalGlue());


        CatLabel fileTypeLabel = new CatLabel("File Type");
        logTypeComboBox = new CatComboBox<>();
        buttonPanel.add(fileTypeLabel);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(logTypeComboBox);
        buttonPanel.add(Box.createHorizontalGlue());
        this.add(buttonPanel, BorderLayout.NORTH);

        previewText = new CatTextArea(200, 200);
        this.add(previewText, BorderLayout.CENTER);

        CatPanel submitPanel = new CatPanel();
        submitPanel.setLayout(new BoxLayout(submitPanel, BoxLayout.X_AXIS));
        CatButton submitButton = new CatButton("Submit");

        submitPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        submitPanel.add(Box.createHorizontalGlue());
        submitPanel.add(submitButton);
        submitPanel.add(Box.createHorizontalGlue());

        this.add(submitPanel, BorderLayout.SOUTH);

    }

    /**
     * Listener which is attached to the choose button and opens up the file chooser.
     * Performs a sanity check on the extension of the file to check the user definitely chooses a CSV file
     */
    class ChooseButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int chooserVal = chooser.showOpenDialog(StartupFileImportPanel.this);

            // If a file is chosen, set the preview, the chosen file, and the selected flag
            if (chooserVal == JFileChooser.APPROVE_OPTION) {


                selectedFile = chooser.getSelectedFile();
                String extension = selectedFile.getName().split("\\.")[1];

                //Check the extension of the file is .csv
                if (!extension.equalsIgnoreCase("csv")) {
                    JOptionPane.showMessageDialog(StartupFileImportPanel.this,
                            "File must be of type CSV",
                            "Wrong Filetype",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LogType chosenLog = validateCSV(selectedFile);
                if (chosenLog != null) {
                    StartupFileImportPanel.this.setPreview(selectedFile);
                    logTypeComboBox.removeAllItems();
                    logTypeComboBox.addItem(chosenLog);
                }
                repaint();
            }
        }
    }

    private void setPreview(File file) {

        previewText.setText("");

        try (Stream<String> fileStream = Files.lines(Paths.get(file.getPath()))) {
            fileStream.limit(10).forEach((str) -> previewText.append(str + '\n'));
        } catch (IOException e) {
            this.handleFileError(file);
        }
    }

    private LogType validateCSV(File file) {
        String firstLine = "";

        try (Stream<String> fileStream = Files.lines(Paths.get(file.getPath()))) {
            firstLine = fileStream.limit(1).reduce("", (a, str) -> str);
        } catch (IOException e) {
            this.handleFileError(file);
        }

        if (firstLine.contains("Impression")) {
            return LogType.IMPRESSION;
        } else if (firstLine.contains("Click")) {
            return LogType.CLICK;
        } else if (firstLine.contains("Conversion")) {
            return LogType.SERVER_LOG;
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid csv file! Must be one of Impression Log, Click Log or Server Log",
                    "Invalid file chosen!",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

    private void handleFileError(File file) {
        JOptionPane.showMessageDialog(this, "File " + file.getName() + " could not be opened!",
                "Error opening file", JOptionPane.ERROR_MESSAGE);
    }
}
