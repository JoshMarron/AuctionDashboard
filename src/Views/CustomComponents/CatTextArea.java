package Views.CustomComponents;

import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Created by marro on 10/03/2017.
 */
public class CatTextArea extends JTextArea {

    int maxWidth;
    int maxHeight;

    public CatTextArea(int maxWidth, int maxHeight) {
        super();
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        initTextArea();
    }

    private void initTextArea() {
        this.setBackground(ColorSettings.TEXT_AREA_BG_COLOR.getColor());
        this.setForeground(ColorSettings.TEXT_AREA_TEXT_COLOR.getColor());
        this.setFont(FontSettings.TEXT_AREA_FONT.getFont());

    }

}
