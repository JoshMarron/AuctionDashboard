package Views;

import Views.CustomComponents.CatPanel;
import Views.DialogPanels.DialogBounceDefinitionPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Joshua on 07/04/2017.
 */
public class DashboardSettingsDialog extends JDialog {

    public static final int APPROVE_OPTION = 1;
    public static final int CANCEL_OPTION = 2;

    private int returnVal;

    public DashboardSettingsDialog(Window parent) {
        super(parent, "Change Settings", ModalityType.APPLICATION_MODAL);
        this.init();
    }

    private void init() {
        this.setSize(800, 160);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setResizable(false);

        CatPanel contentPane = new CatPanel();
        this.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        DialogBounceDefinitionPanel bouncePanel = new DialogBounceDefinitionPanel();
        contentPane.add(bouncePanel, BorderLayout.CENTER);
    }

    public int showDialog() {
        this.returnVal = DashboardSettingsDialog.CANCEL_OPTION; //Set the default each time so we do not accidentally approve
        this.setVisible(true); //Execution hangs here while dialog box is open
        return returnVal;
    }
}
