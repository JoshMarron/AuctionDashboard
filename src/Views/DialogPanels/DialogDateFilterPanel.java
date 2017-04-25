package Views.DialogPanels;

import Views.CustomComponents.CatPanel;
import Views.ViewPresets.ColorSettings;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

/**
 * Created by marro on 24/04/2017.
 */
public class DialogDateFilterPanel extends CatPanel {

    private JFXPanel dateJFXPanel;

    public DialogDateFilterPanel() {
        this.init();
    }

    private void init() {
        this.dateJFXPanel = new JFXPanel();

        Group root = new Group();
//        Scene scene = new Scene(root, Color.rgb(ColorSettings.BG_COLOR))
    }
}
