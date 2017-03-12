package Views.ViewPresets;

import java.awt.*;

/**
 * Created by marro on 10/03/2017.
 */
public enum ColorSettings {

    BG_COLOR (new Color(82, 85, 91)),
    LOADING_COLOR (new Color(181, 184, 188, 100)),
    TEXT_COLOR (new Color(225, 230, 239)),
    BUTTON_COLOR (new Color(68, 71, 76)),
    BUTTON_CLICKED_COLOR (BUTTON_COLOR.getColor().brighter().brighter()),
    BUTTON_HOVER_COLOR (BUTTON_COLOR.getColor().brighter()),
    TEXT_AREA_BG_COLOR (new Color(153, 155, 158)),
    TEXT_AREA_TEXT_COLOR (new Color(52, 52, 53)),
    PANEL_BORDER_COLOR (new Color(164, 166, 168));

    private Color color;

    private ColorSettings(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }
}
