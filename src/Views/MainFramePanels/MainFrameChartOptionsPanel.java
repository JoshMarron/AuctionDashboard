package Views.MainFramePanels;

import Views.CustomComponents.CatButton;
import Views.CustomComponents.CatComboBox;
import Views.CustomComponents.CatLabel;
import Views.CustomComponents.CatPanel;
import Views.DashboardMainFrame;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ChartType;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * MainFrameChartOptionsPanel is a frame which allows the user to change the currently displayed chart to
 * a different type, and choose which attribute to view an attribute chart by
 */
public class MainFrameChartOptionsPanel extends CatPanel {

    private DashboardMainFrame parent;

    public MainFrameChartOptionsPanel(DashboardMainFrame parent) {
        this.parent = parent;
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());

        //Panel with a single large button on it, needs to stand out
        CatPanel filterButtonPanel = new CatPanel();
        filterButtonPanel.setBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()));
        filterButtonPanel.setLayout(new BorderLayout());
        CatButton filterButton = new CatButton("Add filters...");
        filterButtonPanel.add(filterButton, BorderLayout.CENTER);

        //This panel contains the options which can be changed from the main panel
        CatPanel embeddedOptions = new CatPanel();
        embeddedOptions.setBorder(BorderFactory.createEmptyBorder());
        embeddedOptions.setLayout(new BoxLayout(embeddedOptions, BoxLayout.Y_AXIS));

        CatPanel timeChartOptionsPanel = new CatPanel();
        timeChartOptionsPanel.setLayout(new BoxLayout(timeChartOptionsPanel, BoxLayout.X_AXIS));
        timeChartOptionsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()), timeChartOptionsPanel.getBorder()));

        CatLabel timeChartLabel = new CatLabel("Metrics over time");
        CatLabel timeChartTypeLabel = new CatLabel("Chart Type: ");
        timeChartTypeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        CatComboBox<ChartType> timeChartPicker = new CatComboBox<>();
        timeChartPicker.addItem(ChartType.LINE);
        CatButton timeChartGoButton = new CatButton("Go!");
        timeChartGoButton.addActionListener((e) -> this.requestNewTimeChart((ChartType) timeChartPicker.getSelectedItem()));

        timeChartOptionsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        timeChartOptionsPanel.add(timeChartLabel);
        timeChartOptionsPanel.add(Box.createHorizontalGlue());
        timeChartOptionsPanel.add(timeChartTypeLabel);
        timeChartOptionsPanel.add(timeChartPicker);
        timeChartOptionsPanel.add(Box.createHorizontalGlue());
        timeChartOptionsPanel.add(timeChartGoButton);
        timeChartOptionsPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        CatPanel attributeChartOptionsPanel = new CatPanel();
        attributeChartOptionsPanel.setLayout(new BoxLayout(attributeChartOptionsPanel, BoxLayout.X_AXIS));
        attributeChartOptionsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()), attributeChartOptionsPanel.getBorder()));

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

        this.add(embeddedOptions, BorderLayout.CENTER);
        this.add(filterButtonPanel, BorderLayout.EAST);
    }

    private void requestNewTimeChart(ChartType chartType) {
        parent.requestTimeChartTypeChange(chartType);
    }

    private void requestNewAttributeChart(ChartType chartType, AttributeType attr) {
        System.out.println("Display " + chartType + " " + attr);
        parent.requestAttributeChartTypeChange(chartType, attr);
    }
}
