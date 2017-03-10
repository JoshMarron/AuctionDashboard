package Views.ViewPresets;

import java.awt.*;

/**
 * Created by marro on 10/03/2017.
 */
public enum ColorSettings {

    BG_COLOR (new Color(82, 85, 91)),
    LOADING_COLOR (new Color(181, 184, 188)),
    TEXT_COLOR (new Color(225, 230, 239)),
    BUTTON_COLOR (new Color(68, 71, 76)),
    BUTTON_CLICKED_COLOR (BUTTON_COLOR.getColor().brighter().brighter()),
    BUTTON_HOVER_COLOR (BUTTON_COLOR.getColor().brighter());

    private Color color;

    private ColorSettings(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }
}
