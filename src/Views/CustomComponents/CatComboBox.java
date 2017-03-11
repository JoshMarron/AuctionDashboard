package Views.CustomComponents;

import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

/**
 * Created by marro on 10/03/2017.
 */
public class CatComboBox<T> extends JComboBox<T> {

    public CatComboBox() {
        initComboBox();
    }

    private void initComboBox() {

        this.setFocusable(false);
        this.setBackground(ColorSettings.TEXT_AREA_BG_COLOR.getColor());
        this.setForeground(ColorSettings.TEXT_AREA_TEXT_COLOR.getColor());
        this.setFont(FontSettings.TEXT_AREA_FONT.getFont());


    }

}
