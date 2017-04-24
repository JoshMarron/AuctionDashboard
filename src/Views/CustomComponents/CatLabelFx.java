package Views.CustomComponents;

import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created by joepr on 24/04/2017.
 */
public class CatLabelFx extends Label {
    public CatLabelFx(String message){
        super(message);
        initLab();
    }

    private void initLab(){
        this.setTextFill(Color.rgb(ColorSettings.TEXT_COLOR.getRed(), ColorSettings.TEXT_COLOR.getGreen(), ColorSettings.TEXT_COLOR.getBlue()));
        this.setFont(Font.font(FontSettings.GLOB_FONT.getFont().getFamily()));
    }

}
