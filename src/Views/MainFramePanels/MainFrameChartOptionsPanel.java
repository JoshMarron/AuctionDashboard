package Views.MainFramePanels;

import Model.DBEnums.DateEnum;
import Views.CustomComponents.CatButton;
import Views.CustomComponents.CatComboBox;
import Views.CustomComponents.CatLabel;
import Views.CustomComponents.CatPanel;
import Views.DashboardFilterDialog;
import Views.DashboardMainFrame;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ChartType;
import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.List;

/**
 * MainFrameChartOptionsPanel is a frame which allows the user to change the currently displayed chart to
 * a different type, and choose which attribute to view an attribute chart by
 */
public class MainFrameChartOptionsPanel extends CatPanel {

    private DashboardMainFrame parent;
    private Map<AttributeType, List<String>> possibleVals;
    private DashboardFilterDialog filterDialog = null;

    public MainFrameChartOptionsPanel(DashboardMainFrame parent) {
        this.parent = parent;
        this.possibleVals = possibleVals;
        this.init();
    }

    private void init() {
        this.setLayout(new GridBagLayout());

        //Panel with a single large button on it, needs to stand out
        CatPanel filterButtonPanel = new CatPanel();
        filterButtonPanel.setLayout(new BorderLayout());
        filterButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        CatButton filterButton = new CatButton("Add filters...");
        filterButton.setFont(FontSettings.GLOB_FONT.getFont().deriveFont(20F));
        filterButton.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR));
        filterButton.addActionListener((e) -> {
            int returnVal = filterDialog.showFilterDialog();
            System.out.println("closed");
            System.out.println(returnVal);
            if (returnVal == DashboardFilterDialog.APPROVE_OPTION) {
                System.out.println(filterDialog.getFilters());
                System.out.println(filterDialog.getStartDate());
                System.out.println(filterDialog.getEndDate());
                parent.setFilters(filterDialog.getFilters());
                parent.setStartDate(filterDialog.getStartDate());
                parent.setEndDate(filterDialog.getEndDate());
            }
        });
        filterButtonPanel.add(filterButton, BorderLayout.CENTER);

        //This panel contains the options which can be changed from the main panel
        CatPanel embeddedOptions = new CatPanel();
        embeddedOptions.setBorder(BorderFactory.createEmptyBorder());
        embeddedOptions.setLayout(new BoxLayout(embeddedOptions, BoxLayout.Y_AXIS));

        CatPanel timeChartOptionsPanel = new CatPanel();
        timeChartOptionsPanel.setLayout(new BoxLayout(timeChartOptionsPanel, BoxLayout.X_AXIS));
        timeChartOptionsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR), timeChartOptionsPanel.getBorder()));

        CatLabel timeChartLabel = new CatLabel("Metrics over time");
        CatLabel timeChartTypeLabel = new CatLabel("Chart Type: ");
        timeChartTypeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        CatComboBox<ChartType> timeChartPicker = new CatComboBox<>();
        timeChartPicker.addItem(ChartType.LINE);
        CatLabel timeChartGranularityLabel = new CatLabel("Granularity: ");
        timeChartGranularityLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        CatComboBox<DateEnum> timeChartGranularityPicker = new CatComboBox<>();
        for (DateEnum dateEnum: DateEnum.values()) {
            timeChartGranularityPicker.addItem(dateEnum);
        }
        CatButton timeChartGoButton = new CatButton("Go!");
        timeChartGoButton.addActionListener((e) -> this.requestNewTimeChart((ChartType) timeChartPicker.getSelectedItem(),
                (DateEnum) timeChartGranularityPicker.getSelectedItem()));

        timeChartOptionsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        timeChartOptionsPanel.add(timeChartLabel);
        timeChartOptionsPanel.add(Box.createHorizontalGlue());
        timeChartOptionsPanel.add(timeChartTypeLabel);
        timeChartOptionsPanel.add(timeChartPicker);
        timeChartOptionsPanel.add(Box.createHorizontalGlue());
        timeChartOptionsPanel.add(timeChartGranularityLabel);
        timeChartOptionsPanel.add(timeChartGranularityPicker);
        timeChartOptionsPanel.add(Box.createGlue());
        timeChartOptionsPanel.add(timeChartGoButton);
        timeChartOptionsPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        CatPanel attributeChartOptionsPanel = new CatPanel();
        attributeChartOptionsPanel.setLayout(new BoxLayout(attributeChartOptionsPanel, BoxLayout.X_AXIS));
        attributeChartOptionsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR), attributeChartOptionsPanel.getBorder()));

        CatLabel attributeChartLabel = new CatLabel("Metrics by Attribute");
        CatLabel attributeChartTypeLabel = new CatLabel("Chart Type: ");
        attributeChartLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        CatLabel attributeChoiceLabel = new CatLabel("Attribute: ");
        attributeChoiceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        CatComboBox<ChartType> attributeChartPicker = new CatComboBox<>();
        attributeChartPicker.addItem(ChartType.BAR);
        attributeChartPicker.addItem(ChartType.PIE);

        CatComboBox<AttributeType> attributeTypePicker = new CatComboBox<>();
        Arrays.stream(AttributeType.values()).forEach(attributeTypePicker::addItem);

        CatButton attributeChartGoButton = new CatButton("Go!");
        attributeChartGoButton.addActionListener((e) -> {
            ChartType selectedChart = (ChartType) attributeChartPicker.getSelectedItem();
            AttributeType selectedAttribute = (AttributeType) attributeTypePicker.getSelectedItem();

            this.requestNewAttributeChart(selectedChart, selectedAttribute);
        });

        attributeChartOptionsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        attributeChartOptionsPanel.add(attributeChartLabel);
        attributeChartOptionsPanel.add(Box.createHorizontalGlue());
        attributeChartOptionsPanel.add(attributeChartTypeLabel);
        attributeChartOptionsPanel.add(attributeChartPicker);
        attributeChartOptionsPanel.add(Box.createHorizontalGlue());
        attributeChartOptionsPanel.add(attributeChoiceLabel);
        attributeChartOptionsPanel.add(attributeTypePicker);
        attributeChartOptionsPanel.add(Box.createHorizontalGlue());
        attributeChartOptionsPanel.add(attributeChartGoButton);
        attributeChartOptionsPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        embeddedOptions.add(timeChartOptionsPanel);
        embeddedOptions.add(Box.createGlue());
        embeddedOptions.add(attributeChartOptionsPanel);

        GridBagConstraints embeddedCon = new GridBagConstraints();
        embeddedCon.weighty = 1;
        embeddedCon.weightx = 10;
        embeddedCon.gridx = 0;
        embeddedCon.gridy = 0;
        embeddedCon.fill = GridBagConstraints.BOTH;

        GridBagConstraints filterCon = new GridBagConstraints();
        filterCon.weighty = 1;
        filterCon.weightx = 6;
        filterCon.gridx = 1;
        filterCon.gridy = 0;
        filterCon.fill = GridBagConstraints.BOTH;

        this.add(embeddedOptions, embeddedCon);
        this.add(filterButtonPanel, filterCon);
    }

    private void requestNewTimeChart(ChartType chartType, DateEnum granularity) {
        parent.requestTimeChartTypeChange(chartType, granularity);
    }

    private void requestNewAttributeChart(ChartType chartType, AttributeType attr) {
        System.out.println("Display " + chartType + " " + attr);
        parent.requestAttributeChartTypeChange(chartType, attr);
    }

    public void setUpFilterOptions(Map<AttributeType, List<String>> possibleVals) {
        this.possibleVals = possibleVals;
        this.filterDialog = new DashboardFilterDialog(parent, possibleVals);
    }
}
