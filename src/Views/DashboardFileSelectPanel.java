package Views;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DashboardFileSelectPanel is a class containing the elements needed to choose a file from the given directory. Once
 * a file has been chosen, the JFileChooser is replaced with a preview of the file to ensure the correct one was
 * chosen.
 */
public class DashboardFileSelectPanel extends JPanel {

    private DashboardMainFrame parent;
    private File homeDir;
    private ArrayList<DashboardFilePreviewPanel> previewPanels;

    public DashboardFileSelectPanel(File homeDir, DashboardMainFrame parent) {
        this.homeDir = homeDir;
        this.parent = parent;
        this.previewPanels = new ArrayList<>();
    }

    public void init() {
        this.setMaximumSize(new Dimension(600, 900));
        this.setBackground(DashboardMainFrame.BG_COLOR);
        this.setLayout(new BorderLayout());

        //Create title label and panel for it to go on
        JLabel title = new JLabel("Choose your log files");
        title.setFont(DashboardMainFrame.GLOB_FONT);
        title.setFont(new Font(title.getFont().getName(), title.getFont().getStyle(), 20));

        title.setHorizontalAlignment(SwingConstants.CENTER);
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
        JButton confirm = new JButton("Confirm");
        confirm.setFont(DashboardMainFrame.GLOB_FONT);
        confirm.addActionListener(new ConfirmListener());

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
        DashboardFilePreviewPanel serverChoose = new DashboardFilePreviewPanel(LogType.SERVER, homeDir);

        previewPanels.add(impressionChoose);
        previewPanels.add(clickChoose);
        previewPanels.add(serverChoose);

        chooseAndPreviewPanel.add(impressionChoose);
        chooseAndPreviewPanel.add(clickChoose);
        chooseAndPreviewPanel.add(serverChoose);

        this.add(chooseAndPreviewPanel, BorderLayout.CENTER);

    }

    class ConfirmListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Stream<DashboardFilePreviewPanel> panels = previewPanels.stream().filter(DashboardFilePreviewPanel::isLogSelected);
            List<File> files = panels.map(DashboardFilePreviewPanel::getSelectedFile).collect(Collectors.toList());

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
