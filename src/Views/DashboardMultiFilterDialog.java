package Views;

import Views.CustomComponents.CatButton;
import Views.CustomComponents.CatPanel;
import Views.CustomComponents.CatTitlePanel;
import Views.DialogPanels.DialogDateFilterPanel;
import Views.DialogPanels.DialogFilterPanel;
import Views.ViewPresets.AttributeType;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.util.*;
import java.util.List;

/**
 * Created by marro on 26/04/2017.
 */
public class DashboardMultiFilterDialog extends DashboardFilterDialog {

    private List<DialogFilterPanel> filterPanels2;
    private DialogDateFilterPanel dateFilterPanel;

    public DashboardMultiFilterDialog(Window parent, Map<AttributeType, List<String>> possibleVals) {
        super(parent, possibleVals);
        this.filterPanels2 = new ArrayList<>();
        this.init(possibleVals);
    }

    private void init(Map<AttributeType, List<String>> possibleVals) {
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);
        this.setResizable(true);

        CatPanel contentPane = new CatPanel();
        this.setContentPane(contentPane);

        contentPane.setLayout(new BorderLayout());
        dateFilterPanel = new DialogDateFilterPanel();
        contentPane.add(dateFilterPanel, BorderLayout.NORTH);

        CatPanel mainPanel = new CatPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        CatPanel filterPanel1 = new CatPanel();
        filterPanel1.setLayout(new BoxLayout(filterPanel1, BoxLayout.Y_AXIS));

        CatTitlePanel filterPanel1Title = new CatTitlePanel("Filter Set 1");
        filterPanel1.add(filterPanel1Title);

        CatPanel filterPanel2 = new CatPanel();
        filterPanel2.setLayout(new BoxLayout(filterPanel2, BoxLayout.Y_AXIS));

        CatTitlePanel filterPanel2Title = new CatTitlePanel("Filter Set 2");
        filterPanel2.add(filterPanel2Title);

        possibleVals.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(AttributeType::toString))).forEach((entry) -> {
            DialogFilterPanel filterPanel = new DialogFilterPanel(entry.getKey(), entry.getValue());
            filterPanel1.add(filterPanel);
            this.getFilterPanels().add(filterPanel);
            filterPanel1.add(Box.createHorizontalGlue());

            DialogFilterPanel filterPanelTheSecond = new DialogFilterPanel(entry.getKey(), entry.getValue());
            filterPanel2.add(filterPanelTheSecond);
            filterPanels2.add(filterPanelTheSecond);
            filterPanel2.add(Box.createHorizontalGlue());
        });

        CatButton confirmButton = new CatButton("Apply Filters");
        confirmButton.addActionListener((e) -> {
            if (this.getStartDate().isAfter(this.getEndDate())) {
                JOptionPane.showMessageDialog(this, "The start date must be before the end date!",
                        "Date Selection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.setReturnVal(DashboardFilterDialog.APPROVE_OPTION);
            this.setVisible(false);
        });

        mainPanel.add(filterPanel1);
        mainPanel.add(filterPanel2);

        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(confirmButton, BorderLayout.SOUTH);

    }

    public Map<AttributeType, List<String>> getSecondFilters() {
        Map<AttributeType, List<String>> filters = new HashMap<>();

        for (DialogFilterPanel filterPanel: filterPanels2) {
            if (!filterPanel.getSelected().isEmpty()) {
                filters.put(filterPanel.getAttribute(), filterPanel.getSelected());
            }
        }

        return filters;
    }

    public Instant getStartDate() {
        return this.dateFilterPanel.getStartDate();
    }

    public Instant getEndDate() {
        return this.dateFilterPanel.getEndDate();
    }
}
