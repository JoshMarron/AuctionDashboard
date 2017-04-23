package Views.ViewPresets;

import java.awt.*;

/**
 * ColorSettings is an enum of the colour scheme used throughout the application
 */
public class ColorSettings {

    public static Color BG_COLOR = new Color(82, 85, 91);
    public static Color LOADING_COLOR = new Color(181, 184, 188, 100);
    public static Color TEXT_COLOR = new Color(255,230,239);
    public static Color BUTTON_COLOR = new Color(68, 71, 76);
    public static Color BUTTON_CLICKED_COLOR = BUTTON_COLOR.brighter().brighter();
    public static Color BUTTON_HOVER_COLOR = BUTTON_COLOR.brighter();
    public static Color TEXT_AREA_BG_COLOR = new Color(153,155,158);
    public static Color TEXT_AREA_TEXT_COLOR = new Color(52, 52,53);
    public static Color PANEL_BORDER_COLOR = new Color(174,166,168);
}
