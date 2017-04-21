package Views.DialogPanels;

import Views.CustomComponents.CatCheckBox;
import Views.CustomComponents.CatPanel;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.util.ArrayList;
import java.util.List;

/**
 * DialogFilterPanel, given a list of values for an attribute, displays all the values as checkboxes
 */
public class DialogFilterPanel extends CatPanel {

    private List<String> attributeValues;
    private AttributeType attr;
    private List<JCheckBox> checkboxes;

    public DialogFilterPanel(AttributeType attr, List<String> attributeValues) {
        this.attributeValues = attributeValues;
        this.attr = attr;
        this.checkboxes = new ArrayList<>();
        this.init();
    }

    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        Border innerBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR),
                attr.toString(), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, FontSettings.GLOB_FONT.getFont(),
                ColorSettings.TEXT_COLOR);
        Border outerBorder = BorderFactory.createEmptyBorder(10, 0, 10, 0);
        this.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        this.add(Box.createGlue());

        for (String val: AttributeType.sortAttributeValues(attr, attributeValues)) {
            //TODO create CatCheckBox for customisation
            CatCheckBox checkBox = new CatCheckBox(val);
            checkboxes.add(checkBox);
            this.add(checkBox);
            this.add(Box.createHorizontalGlue());
        }

    }

    public AttributeType getAttribute() {
        return this.attr;
    }

    public List<String> getSelected() {
        List<String> selected = new ArrayList<>();

        for (JCheckBox box: checkboxes) {
            if (box.isSelected()) {
                selected.add(box.getText());
            }
        }

        return selected;
    }

    //when any checkbox is selected, should colour the panel
}
