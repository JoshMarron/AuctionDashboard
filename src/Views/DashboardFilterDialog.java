package Views;

import Views.CustomComponents.CatPanel;
import Views.ViewPresets.AttributeType;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The frame which pops up when the "add filters" button is clicked, allows the user to select the filters they wish to
 * apply to their data.
 */
public class DashboardFilterDialog extends JDialog {

    private Map<AttributeType, List<String>> filters;

    public DashboardFilterDialog() {
        this.init();
    }

    private void init() {
        CatPanel contentPane = new CatPanel();
        this.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

    }

    public Map<AttributeType, List<String>> getFilters() {
        return this.filters;
    }
}
