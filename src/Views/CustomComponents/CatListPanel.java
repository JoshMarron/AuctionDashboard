package Views.CustomComponents;

import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * Created by marro on 12/03/2017.
 */
public class CatListPanel extends CatPanel {

    public CatListPanel() {
        init();
    }

    private void init() {
        this.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()));
    }

}
