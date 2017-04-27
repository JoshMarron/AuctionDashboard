package Views.DialogPanels;

import Views.CustomComponents.*;
import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Created by Joe on 11/04/2017.
 */
public class DialogColourCustomisationPanel extends CatPanel{

    private CatCheckBox useCustomColours;
    private CatComboBox themeSelector;

    private CatPanel colorOptions;
    private CatPanel customColor;

    private ColorPicker bgcolor;
    private ColorPicker textcolor;
    private ColorPicker buttoncolor;
    private ColorPicker textAreaBg;
    private ColorPicker textAreaText;


    public DialogColourCustomisationPanel(){this.init();}

    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ColorSettings.PANEL_BORDER_COLOR),
                "Colour Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                FontSettings.GLOB_FONT.getFont(), ColorSettings.TEXT_COLOR));

        initColorOptionsPanel();
        initCustomColorOptionsPanel();

        this.add(Box.createVerticalGlue());
        this.add(colorOptions);
        this.add(Box.createVerticalGlue());
        this.add(customColor);
        this.add(Box.createVerticalGlue());
    }

    private void initColorOptionsPanel(){
        colorOptions = new CatPanel();
        colorOptions.setLayout(new BoxLayout(colorOptions, BoxLayout.X_AXIS));
        CatLabel colourThemeLabel = new CatLabel("Use selected theme:");
        CatLabel colourCustomLabel = new CatLabel ("Use custom colours:");
        useCustomColours = new CatCheckBox("");
        themeSelector = new CatComboBox();
        themeSelector.setMaximumSize(themeSelector.getPreferredSize());
        colorOptions.add(Box.createHorizontalGlue());
        colorOptions.add(colourThemeLabel);
        colorOptions.add(themeSelector);

        colorOptions.add(Box.createHorizontalGlue());
        colorOptions.add(colourCustomLabel);
        colorOptions.add(useCustomColours);
        colorOptions.add(Box.createHorizontalGlue());
    }

    private void initCustomColorOptionsPanel(){
        customColor = new CatPanel();
        customColor.setLayout(new BoxLayout(customColor, BoxLayout.X_AXIS));

        JFXPanel fxPanel = new JFXPanel();
        Platform.runLater(new Runnable(){
            @Override
            public void run(){
                initFx(fxPanel);
            }
        });

        customColor.add(fxPanel);
    }

    private void initFx(JFXPanel panel){
        Scene scene = createScene();
        panel.setScene(scene);
    }

    private Scene createScene(){
        Group root = new Group();
        Scene scene = new Scene(root, Color.rgb(ColorSettings.BG_COLOR.getRed(), ColorSettings.BG_COLOR.getGreen(), ColorSettings.BG_COLOR.getBlue()));

        VBox vbox = new VBox();
        vbox.setSpacing(10);


        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.BASELINE_CENTER);
        bgcolor = new ColorPicker();
        bgcolor.setValue(Color.rgb(ColorSettings.BG_COLOR.getRed(), ColorSettings.BG_COLOR.getGreen(), ColorSettings.BG_COLOR.getBlue()));
        textcolor = new ColorPicker();
        textcolor.setValue(Color.rgb(ColorSettings.TEXT_COLOR.getRed(), ColorSettings.TEXT_COLOR.getGreen(), ColorSettings.TEXT_COLOR.getBlue()));
        buttoncolor = new ColorPicker();
        buttoncolor.setValue(Color.rgb(ColorSettings.BUTTON_COLOR.getRed(), ColorSettings.BUTTON_COLOR.getGreen(), ColorSettings.BUTTON_COLOR.getBlue()));
        hbox.getChildren().add(new CatLabelFx("Background:"));
        hbox.getChildren().add(bgcolor);
        hbox.getChildren().add(new CatLabelFx("Text:"));
        hbox.getChildren().add(textcolor);
        hbox.getChildren().add(new CatLabelFx("Buttons:"));
        hbox.getChildren().add(buttoncolor);

        HBox overflow = new HBox();
        overflow.setAlignment(Pos.BASELINE_CENTER);
        overflow.setSpacing(10);
        textAreaBg = new ColorPicker();
        textAreaBg.setValue(Color.rgb(ColorSettings.TEXT_AREA_BG_COLOR.getRed(), ColorSettings.TEXT_AREA_BG_COLOR.getGreen(), ColorSettings.TEXT_AREA_BG_COLOR.getBlue()));
        textAreaText = new ColorPicker();
        textAreaText.setValue(Color.rgb(ColorSettings.TEXT_AREA_TEXT_COLOR.getRed(), ColorSettings.TEXT_AREA_TEXT_COLOR.getGreen(), ColorSettings.TEXT_AREA_TEXT_COLOR.getBlue()));
        overflow.getChildren().add(new CatLabelFx("Text Area Background:"));
        overflow.getChildren().add(textAreaBg);
        overflow.getChildren().add(new CatLabelFx("Text Area Text Color:"));
        overflow.getChildren().add(textAreaText);

        vbox.getChildren().add(hbox);
        vbox.getChildren().add(overflow);

        root.getChildren().add(vbox);

        return scene;
    }

    public java.awt.Color getChosenBgColor(){
        return fxPickerToSwingColor(bgcolor);
    }

    public java.awt.Color getChosenTextColor(){
        return fxPickerToSwingColor(textcolor);
    }

    public java.awt.Color getChosenButtonColor(){
        return fxPickerToSwingColor(buttoncolor);
    }

    public java.awt.Color getChosenTextAreaBackgroundColor(){
        return fxPickerToSwingColor(textAreaBg);
    }

    public java.awt.Color getChosenTextAreaTextColor(){
        return fxPickerToSwingColor(textAreaText);
    }

    public static java.awt.Color fxPickerToSwingColor(ColorPicker picker){
        return new java.awt.Color((int)(picker.getValue().getRed()*255), (int)(picker.getValue().getGreen()*255), (int)(picker.getValue().getBlue()*255));
    }

}
