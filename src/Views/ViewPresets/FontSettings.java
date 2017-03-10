package Views.ViewPresets;

import java.awt.*;

/**
 * Collects all the font objects for the application, configure them here
 */
public enum FontSettings {

    GLOB_FONT (new Font("Tahoma", Font.BOLD, 14));

    private Font font;

    private FontSettings(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return this.font;
    }
}
