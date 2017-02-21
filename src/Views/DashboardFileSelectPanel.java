package Views;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * DashboardFileSelectPanel is a class containing the elements needed to choose a file from the given directory. Once
 * a file has been chosen, the JFileChooser is replaced with a preview of the file to ensure the correct one was
 * chosen.
 */
public class DashboardFileSelectPanel extends JPanel {

    private File homeDir;
    private JFileChooser chooser;

    public DashboardFileSelectPanel(File homeDir) {
        this.homeDir = homeDir;
    }

    public void init() {
        this.setMaximumSize(new Dimension(700, 900));
        this.setLayout(new BorderLayout());

        //Create title label and panel for it to go on
        JLabel title = new JLabel("Choose a file");
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
        JButton choose = new JButton("Import CSV File...");

        buttonPanel.add(Box.createRigidArea(new Dimension(100, 100)));
        buttonPanel.add(choose);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(submit);
        buttonPanel.add(Box.createHorizontalStrut(100));
        this.add(buttonPanel, BorderLayout.SOUTH);

        //Create preview JTextPane
        JTextPane preview = new JTextPane();
        this.add(preview, BorderLayout.CENTER);

        //Instantiate JFileChooser which will appear when buttons are clicked
        chooser = new JFileChooser(homeDir);
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Files", "csv");
        chooser.setFileFilter(csvFilter);

    }

}
