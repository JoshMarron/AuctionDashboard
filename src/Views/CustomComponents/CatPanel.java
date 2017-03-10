package Views.CustomComponents;

import Views.ViewPresets.ColorSettings;

import javax.swing.*;

/**
 * Created by marro on 10/03/2017.
 */
public class CatPanel extends JPanel {

    public CatPanel() {
        super();
        initCatPanel();
    }

    private void initCatPanel() {
        this.setBackground(ColorSettings.BG_COLOR.getColor());
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
}
