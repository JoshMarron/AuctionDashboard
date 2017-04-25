package Views.DialogPanels;

import Views.CustomComponents.CatLabel;
import Views.CustomComponents.CatLabelFx;
import Views.CustomComponents.CatPanel;
import Views.ViewPresets.ColorSettings;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
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
        Scene scene = new Scene(root, Color.rgb(ColorSettings.BG_COLOR.getRed(), ColorSettings.BG_COLOR.getGreen(), ColorSettings.BG_COLOR.getBlue()));

        CatLabelFx startLabel = new CatLabelFx("Start Date: ");
        CatLabelFx endLabel = new CatLabelFx("End Date: ");

        DatePicker startPicker = new DatePicker();
        DatePicker endPicker = new DatePicker();

        HBox dateBox = new HBox();
        dateBox.getChildren().add(startLabel);
        dateBox.getChildren().add(startPicker);
        dateBox.getChildren().add(endLabel);
        dateBox.getChildren().add(endPicker);

        root.getChildren().add(dateBox);

        dateJFXPanel.setScene(scene);
    }
}
