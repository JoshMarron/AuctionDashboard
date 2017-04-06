package Views;

import Views.CustomComponents.CatPanel;
import Views.MainFramePanels.MainFrameFilterPanel;
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

    private List<MainFrameFilterPanel> filterPanels;

    public DashboardFilterDialog(Window parent, Map<AttributeType, List<String>> possibleVals) {
        super(parent, "Choose Filters", ModalityType.APPLICATION_MODAL);
        this.filterPanels = new ArrayList<>();
        this.init(possibleVals);
    }

    private void init(Map<AttributeType, List<String>> possibleVals) {
        CatPanel contentPane = new CatPanel();
        this.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        possibleVals.forEach((attr, vals) -> {

        });

    }

    public Map<AttributeType, List<String>> getFilters() {
        Map<AttributeType, List<String>> filters = new HashMap<>();

        return filters;
    }
}
