package Views;

import Views.CustomComponents.CatPanel;

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
        CatPanel contentPane = new CatPanel();
        this.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());


    }

    public int showDialog() {
        this.returnVal = DashboardSettingsDialog.CANCEL_OPTION; //Set the default each time so we do not accidentally approve
        this.setVisible(true); //Execution hangs here while dialog box is open
        return returnVal;
    }
}
