package Views.DialogPanels;

import Views.CustomComponents.CatCheckBox;
import Views.CustomComponents.CatLabel;
import Views.CustomComponents.CatNumberSpinner;
import Views.CustomComponents.CatPanel;
import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Created by Joshua on 07/04/2017.
 */
public class DialogBounceDefinitionPanel extends CatPanel {

    private CatCheckBox bouncePageBox;
    private CatCheckBox bounceTimeBox;
    private CatNumberSpinner bouncePageSpinner;
    private CatNumberSpinner bounceTimeSpinner;

    public DialogBounceDefinitionPanel() {
        this.init();
    }

    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR),
                "Bounce Definition", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                FontSettings.GLOB_FONT.getFont(), ColorSettings.TEXT_COLOR));

        CatLabel bounceTimeLabel = new CatLabel("Visit length (in minutes): ");
        CatLabel bouncePageLabel = new CatLabel("Pages viewed: ");

        bouncePageSpinner = new CatNumberSpinner(1);
        bounceTimeSpinner = new CatNumberSpinner(1);

        bouncePageBox = new CatCheckBox("Use");
        bounceTimeBox = new CatCheckBox("Use");

        bouncePageBox.setSelected(true);

        this.add(Box.createHorizontalGlue());
        this.add(bouncePageLabel);
        this.add(bouncePageSpinner);
        this.add(bouncePageBox);

        this.add(Box.createHorizontalGlue());
        this.add(bounceTimeLabel);
        this.add(bounceTimeSpinner);
        this.add(bounceTimeBox);
        this.add(Box.createHorizontalGlue());
    }

    public int getBouncePages() {
        return bouncePageBox.isSelected() ? bouncePageSpinner.getNumber() : Integer.MAX_VALUE;
    }

    public int getBounceTime() {
        return bounceTimeBox.isSelected() ? bounceTimeSpinner.getNumber() : Integer.MAX_VALUE;
    }
}
