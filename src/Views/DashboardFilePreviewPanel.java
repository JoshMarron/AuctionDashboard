package Views;

/**
 * DashboardFilePreviewPanel allows the user to select a file with a file chooser, see its name, and
 * view the first lines of the file to check they chose the right one
 */
public class DashboardFilePreviewPanel {

    private LogType logType; //Impression, click or server
    private boolean selected; //True if a file has been selected

    public DashboardFilePreviewPanel(LogType logType) {
        this.logType = logType;
        this.init();
    }

    public void init() {

    }


}
