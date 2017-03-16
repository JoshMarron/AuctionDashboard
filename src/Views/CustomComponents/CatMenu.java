package Views.CustomComponents;

import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;

/**
 *
 */
public class CatMenu extends JMenu {

    public CatMenu(String menuName) {
        super(menuName);
        this.init();
    }

    private void init() {
        this.setForeground(ColorSettings.TEXT_COLOR.getColor());
        this.setFont(FontSettings.GLOB_FONT.getFont());
    }
}
