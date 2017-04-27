package Views;

import Controllers.ColorController;
import Controllers.ProjectSettings;
import Views.CustomComponents.CatButton;
import Views.CustomComponents.CatPanel;
import Views.DialogPanels.DialogBounceDefinitionPanel;
import Views.DialogPanels.DialogColourCustomisationPanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * A Dialog which will contain a number of ways to tweak the settings of the application
 */
public class DashboardSettingsDialog extends JDialog {

    public static final int APPROVE_OPTION = 1;
    public static final int CANCEL_OPTION = 2;

    private int returnVal;
    private DialogBounceDefinitionPanel bouncePanel;
    private DialogColourCustomisationPanel colourPanel;

    public DashboardSettingsDialog(Window parent) {
        super(parent, "Change Settings", ModalityType.APPLICATION_MODAL);
        this.init();
    }

    private void init() {
        this.setSize(1200, 800);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setResizable(false);

        CatPanel contentPane = new CatPanel();
        this.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        bouncePanel = new DialogBounceDefinitionPanel();
        colourPanel = new DialogColourCustomisationPanel();

        CatButton applyButton = new CatButton("Apply");
        applyButton.addActionListener((e) -> {
            this.applyColorChanges();
            this.returnVal = DashboardSettingsDialog.APPROVE_OPTION;
            this.setBounceVals();
            this.setVisible(false);
        });
        CatButton cancelButton = new CatButton("Cancel");
        cancelButton.addActionListener((e) -> {
            this.returnVal = DashboardSettingsDialog.CANCEL_OPTION;
            this.setVisible(false);
        });

        CatPanel buttonPanel = new CatPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(applyButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalGlue());


        CatPanel settingsContentPanel = new CatPanel();
        settingsContentPanel.setLayout(new BoxLayout(settingsContentPanel, BoxLayout.Y_AXIS));
        settingsContentPanel.add(Box.createVerticalGlue());
        settingsContentPanel.add(bouncePanel);
        settingsContentPanel.add(Box.createGlue());
        settingsContentPanel.add(colourPanel);
        settingsContentPanel.add(Box.createVerticalGlue());

        contentPane.add(settingsContentPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    public int showDialog() {
        this.returnVal = DashboardSettingsDialog.CANCEL_OPTION; //Set the default each time so we do not accidentally approve
        this.setVisible(true); //Execution hangs here while dialog box is open
        return returnVal;
    }

    private void setBounceVals() {
        ProjectSettings.setBouncePages(bouncePanel.getBouncePages());
        ProjectSettings.setBounceSeconds(bouncePanel.getBounceTime());
    }

    private void applyColorChanges(){
        java.util.ArrayList<Color> colors = new ArrayList<Color>();
        colors.add(colourPanel.getChosenBgColor());
        colors.add(colourPanel.getChosenTextColor());
        colors.add(colourPanel.getChosenButtonColor());
        colors.add(colourPanel.getChosenTextAreaBackgroundColor());
        colors.add(colourPanel.getChosenTextAreaTextColor());
        ColorController.applyColorChanges(colors);
    }
}
