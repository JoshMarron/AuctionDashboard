package Views.CustomComponents;

import Views.DashboardMainFrame;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import java.awt.*;

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

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setFocusPainted(false);
        this.setBackground(ColorSettings.BUTTON_COLOR.getColor());
        this.setFont(DashboardMainFrame.GLOB_FONT);
        this.setForeground(ColorSettings.TEXT_COLOR.getColor());
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if (this.getModel().isSelected()) {
            g2.setColor(ColorSettings.BUTTON_CLICKED_COLOR.getColor());
        }
        if (this.getModel().isRollover()) {
            g2.setColor(ColorSettings.BUTTON_HOVER_COLOR.getColor());
        }
        else {
            g2.setColor(ColorSettings.BUTTON_COLOR.getColor());
        }
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        super.paintComponent(g);
    }

}
