package Views.CustomComponents;

import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;

/**
 * CatCheckBox is a JCheckBox with stylings to match that of our application
 */
public class CatCheckBox extends JCheckBox {

    public CatCheckBox(String text) {
        super(text);
        this.init();
    }

    private void init() {
        this.setBackground(ColorSettings.BG_COLOR);
        this.setFont(FontSettings.GLOB_FONT.getFont());
        this.setForeground(ColorSettings.TEXT_COLOR);
    }
}
