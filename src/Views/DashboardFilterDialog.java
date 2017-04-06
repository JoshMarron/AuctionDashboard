package Views;

import Views.CustomComponents.CatButton;
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

    public final static int APPROVE_OPTION = 1;
    public final static int CANCEL_OPTION = 2;
    private List<MainFrameFilterPanel> filterPanels;
    private int returnVal;

    public DashboardFilterDialog(Window parent, Map<AttributeType, List<String>> possibleVals) {
        super(parent, "Choose Filters", ModalityType.APPLICATION_MODAL);
        this.filterPanels = new ArrayList<>();
        this.init(possibleVals);
    }

    private void init(Map<AttributeType, List<String>> possibleVals) {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);

        CatPanel contentPane = new CatPanel();
        this.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        CatPanel filterListPanel = new CatPanel();
        filterListPanel.setLayout(new BoxLayout(filterListPanel, BoxLayout.Y_AXIS));
        filterListPanel.add(Box.createGlue());

        possibleVals.forEach((attr, vals) -> {
            MainFrameFilterPanel filterPanel = new MainFrameFilterPanel(attr, vals);
            filterPanels.add(filterPanel);
            filterListPanel.add(filterPanel);
            filterListPanel.add(Box.createHorizontalGlue());
        });

        CatButton confirmButton = new CatButton("Apply Filters");
        confirmButton.addActionListener((e) -> {
            this.returnVal = DashboardFilterDialog.APPROVE_OPTION;
            this.setVisible(false);
        });

        this.add(filterListPanel, BorderLayout.CENTER);
        this.add(confirmButton, BorderLayout.SOUTH);
    }

    public int showFilterDialog() {
        this.returnVal = DashboardFilterDialog.CANCEL_OPTION; //Need to reset this or it might always approve
        this.setVisible(true);
        return this.returnVal;
    }

    public Map<AttributeType, List<String>> getFilters() {
        Map<AttributeType, List<String>> filters = new HashMap<>();

        for (MainFrameFilterPanel filterPanel: filterPanels) {
            if (!filterPanel.getSelected().isEmpty()) {
                filters.put(filterPanel.getAttribute(), filterPanel.getSelected());
            }
        }

        return filters;
    }
}
