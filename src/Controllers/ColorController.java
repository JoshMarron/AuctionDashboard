package Controllers;

import Views.DialogPanels.DialogColourCustomisationPanel;
import Views.ViewPresets.ColorSettings;

import java.awt.*;
import java.util.*;
import javax.swing.*;


/**
 * Created by joepr on 24/04/2017.
 */
public class ColorController {
    public static void applyColorChanges(java.util.ArrayList<Color> colors){
        ColorSettings.BG_COLOR = colors.get(0);
        ColorSettings.TEXT_COLOR = colors.get(1);
        ColorSettings.BUTTON_COLOR = colors.get(2);
        ColorSettings.TEXT_AREA_BG_COLOR = colors.get(3);
        ColorSettings.TEXT_AREA_TEXT_COLOR = colors.get(4);
    }
}
