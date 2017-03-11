package Views.CustomComponents;

import SwingUtilsLib.SwingUtils;
import Views.DashboardMainFrame;
import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Created by marro on 10/03/2017.
 */
public class CatFileChooser extends JFileChooser {

    public CatFileChooser(File homedir) {
        super(homedir);
        initChooser();
    }

    private void initChooser() {
        this.setFont(FontSettings.GLOB_FONT.getFont());

        this.setDialogTitle("Choose a log file...");
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Files", "csv");
        this.setFileFilter(csvFilter);
    }
}
