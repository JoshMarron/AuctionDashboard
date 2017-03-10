package Views.CustomComponents;

import Views.DashboardMainFrame;
import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;

/**
 * CatLabel is a JLabel with configurable colors, use this
 */
public class CatLabel extends JLabel {

    public CatLabel(String text) {
        super(text);
        initLab();
    }

    private void initLab() {
        this.setForeground(ColorSettings.TEXT_COLOR.getColor());
        this.setFont(FontSettings.GLOB_FONT.getFont());
    }

}
