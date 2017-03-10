package Views.StartupPanels;

import Views.CustomComponents.CatPanel;

import java.io.File;

/**
 * StartupFileImportPanel allows the user to select a file with a file chooser, view a small preview and see which
 * log it corresponds to
 */
public class StartupFileImportPanel extends CatPanel {

    private File homedir;

    public StartupFileImportPanel(File homedir) {
        this.homedir = homedir;
    }

    private void initFileImportPanel() {

    }
}
