package Views.DialogPanels;

import Views.CustomComponents.CatPanel;
import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Created by Joshua on 07/04/2017.
 */
public class DialogBounceDefinitionPanel extends CatPanel {

    public DialogBounceDefinitionPanel() {
        this.init();
    }

    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()),
                "Bounce Definition", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                FontSettings.GLOB_FONT.getFont(), ColorSettings.TEXT_COLOR.getColor()));


    }
}
