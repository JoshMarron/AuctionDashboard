package Views.MainFramePanels;

import Views.CustomComponents.*;
import Views.DashboardMainFrame;
import Views.ViewPresets.AttributeType;
import Views.ViewPresets.ChartType;
import Views.ViewPresets.ColorSettings;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Created by marro on 16/03/2017.
 */
public class MainFrameChartOptionsPanel extends CatPanel {

    private DashboardMainFrame parent;

    public MainFrameChartOptionsPanel(DashboardMainFrame parent) {
        this.parent = parent;
        this.init();
    }

    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        CatPanel timeChartOptionsPanel = new CatPanel();
        timeChartOptionsPanel.setLayout(new BoxLayout(timeChartOptionsPanel, BoxLayout.X_AXIS));
        timeChartOptionsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR.getColor()), timeChartOptionsPanel.getBorder()));

        CatLabel timeChartLabel = new CatLabel("Metrics over time");
        CatLabel timeChartTypeLabel = new CatLabel("Chart Type: ");
        timeChartTypeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        CatComboBox<ChartType> timeChartPicker = new CatComboBox<>();
        timeChartPicker.addItem(ChartType.LINE);
        CatButton timeChartGoButton = new CatButton("Go!");

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

        this.add(timeChartOptionsPanel);
        this.add(Box.createGlue());
        this.add(attributeChartOptionsPanel);
    }

    public void requestNewTimeChart(ChartType chartType) {

    }
}
