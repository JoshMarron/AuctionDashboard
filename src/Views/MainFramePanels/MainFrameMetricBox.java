package Views.MainFramePanels;

import Views.CustomComponents.CatLabel;
import Views.CustomComponents.CatListPanel;
import Views.MetricType;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * MainFrameMetricBox contains a single metric to be displayed in the list of metrics in the side bar.
 */
public class MainFrameMetricBox extends CatListPanel {

    private MetricType type;
    private CatLabel metricDataLabel;

    public MainFrameMetricBox(MetricType type) {
        this.type = type;
        this.init();
    }

    private void init() {

        CatLabel nameLabel = new CatLabel(type.toString());
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        metricDataLabel = new CatLabel(type.getErrorMessage());
        metricDataLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(nameLabel);
        this.add(Box.createGlue());
        this.add(metricDataLabel);
        this.add(Box.createGlue());
    }

    public void updateData(Number data) {
        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.CEILING);
        metricDataLabel.setFont(FontSettings.GLOB_FONT.getFont().deriveFont(20F));
        metricDataLabel.setText(df.format(data));
    }

}
