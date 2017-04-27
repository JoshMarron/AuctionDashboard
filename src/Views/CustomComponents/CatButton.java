package Views.CustomComponents;

import Views.DashboardMainFrame;
import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;
import javafx.scene.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Cursor;

/**
 * CatButton is a JButton which doesn't look like complete arse.
 */
public class CatButton extends JButton {

    public CatButton(String text) {
        super(text);
        this.initButton();
    }

    private void initButton() {
        this.setContentAreaFilled(false);

        Border outside = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border inside = BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR);
        this.setBorder(BorderFactory.createCompoundBorder(inside, outside));
        this.setFocusPainted(false);
        this.setBackground(ColorSettings.BUTTON_COLOR);
        this.setFont(FontSettings.GLOB_FONT.getFont());
        this.setForeground(ColorSettings.TEXT_COLOR);

        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if (this.getModel().isSelected()) {
            g2.setColor(ColorSettings.BUTTON_CLICKED_COLOR);
        }
        if (this.getModel().isRollover()) {
            g2.setColor(ColorSettings.BUTTON_HOVER_COLOR);
        }
        else {
            g2.setColor(ColorSettings.BUTTON_COLOR);
        }
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        super.paintComponent(g);
    }

}
