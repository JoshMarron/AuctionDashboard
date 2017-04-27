package Views.MainFramePanels;

import Controllers.ProjectSettings;
import Views.CustomComponents.CatMenu;
import Views.CustomComponents.CatMenuBar;
import Views.DashboardMainFrame;
import Views.DashboardSettingsDialog;
import Views.ViewPresets.ChartType;

import javax.swing.*;

/**
 * Created by marro on 14/03/2017.
 */
public class MainFrameMenu extends CatMenuBar {

    private DashboardMainFrame frame;
    private DashboardSettingsDialog settingsDialog;

    public MainFrameMenu(DashboardMainFrame frame) {
        this.frame = frame;
        this.settingsDialog = new DashboardSettingsDialog(frame);
        this.init();
    }

    private void init() {
        CatMenu fileMenu = new CatMenu("File");
        JMenuItem saveAsMenuItem = new JMenuItem("Save as...");
        saveAsMenuItem.addActionListener((e) -> frame.saveFileAs());

        JMenuItem closeProjectItem = new JMenuItem("Close Project");
        closeProjectItem.addActionListener((e) -> frame.closeProject());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener((e) -> System.exit(0));

        JMenuItem settingsItem = new JMenuItem("Settings...");
        settingsItem.addActionListener((e) -> {
            int settingsVal = settingsDialog.showDialog();
            if (settingsVal == DashboardSettingsDialog.APPROVE_OPTION) {
                frame.restart();
            }
        });

        JMenuItem addSecondCampaignItem = new JMenuItem("Add second campaign...");
        addSecondCampaignItem.addActionListener((e) -> {
            frame.addSecondCampaign();
        });

        JMenuItem multiFilterModeItem = new JMenuItem("Start multi filter mode");
        multiFilterModeItem.addActionListener((e) -> {
            JOptionPane.showMessageDialog(frame, "Multi filter mode started, choose the filters with the Add Filter button", "Multi Filter Mode", JOptionPane.INFORMATION_MESSAGE);
            frame.startMultiFilter();
        });

        fileMenu.add(addSecondCampaignItem);
        fileMenu.add(multiFilterModeItem);
        JMenuItem exportItem = new JMenuItem("Export Chart");
        exportItem.addActionListener((e)-> {
            if(frame.getRequestedChart() == ChartType.LINE){
                frame.getChartPanel().saveTimeChart(frame.getChartPanel().getTimeChartMap());
            } else {
                frame.getChartPanel().saveAttributeChart(frame.getChartPanel().getAttributeChartMap());
            }
        });

        fileMenu.add(saveAsMenuItem);
        fileMenu.add(exportItem);
        fileMenu.add(settingsItem);
        fileMenu.add(closeProjectItem);
        fileMenu.add(exitItem);

        this.add(fileMenu);
    }


}
