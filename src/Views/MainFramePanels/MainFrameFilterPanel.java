package Views.MainFramePanels;

import Views.CustomComponents.CatPanel;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MainFrameFilterPanel, given a list of values for an attribute, displays all the values as checkboxes
 */
public class MainFrameFilterPanel extends CatPanel {

    private List<String> attributeValues;
    private AttributeType attr;
    private List<JCheckBox> checkboxes;

    public MainFrameFilterPanel(AttributeType attr, List<String> attributeValues) {
        this.attributeValues = attributeValues;
        this.attr = attr;
        this.checkboxes = new ArrayList<>();
        this.init();
    }

    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()),
                attr.toString()));

        this.add(Box.createGlue());

        for (String val: attributeValues) {
            //TODO create CatCheckBox for customisation
            JCheckBox checkBox = new JCheckBox(val);
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
                System.out.println(box.getText());
                selected.add(box.getText());
            }
        }

        return selected;
    }

    //when any checkbox is selected, should colour the panel
}
