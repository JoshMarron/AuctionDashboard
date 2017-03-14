package Views.CustomComponents;

import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import java.awt.*;

/**
 * CatMenuBar is simply a themed JMenuBar for this application
 */
public class CatMenuBar extends JMenuBar {

    private Color bgColor;

    public CatMenuBar() {
        bgColor = ColorSettings.BG_COLOR.getColor();
        this.init();
    }

    private void init() {
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorSettings.PANEL_BORDER_COLOR.getColor()));

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(bgColor);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}
