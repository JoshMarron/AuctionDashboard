package Views.DialogPanels;

import Views.CustomComponents.CatCheckBox;
import Views.CustomComponents.CatComboBox;
import Views.CustomComponents.CatLabel;
import Views.CustomComponents.CatPanel;
import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Created by Joe on 11/04/2017.
 */
public class DialogColourCustomisationPanel extends CatPanel{

    private CatCheckBox useColourTheme;
    private CatCheckBox useCustomColours;
    private CatComboBox themeSelector;

    public DialogColourCustomisationPanel(){this.init();}

    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR),
                "Colour Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                FontSettings.GLOB_FONT.getFont(), ColorSettings.TEXT_COLOR));

        CatLabel colourThemeLabel = new CatLabel("Use selected theme");
        CatLabel colourCustomLabel = new CatLabel ("Use custom colours");

        useColourTheme = new CatCheckBox("Use");
        useCustomColours = new CatCheckBox("Use");
        themeSelector = new CatComboBox();
        themeSelector.setSelectedItem("Default (Dark)");

        this.add(Box.createHorizontalGlue());
        this.add(colourThemeLabel);
        this.add(useColourTheme);
        this.add(themeSelector);

        this.add(Box.createHorizontalGlue());
        this.add(colourCustomLabel);
        this.add(useCustomColours);
        this.add(Box.createHorizontalGlue());
    }

}
