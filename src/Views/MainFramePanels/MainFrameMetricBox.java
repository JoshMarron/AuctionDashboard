package Views.MainFramePanels;

import Views.CustomComponents.CatLabel;
import Views.CustomComponents.CatListPanel;
import Views.MetricType;
import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * MainFrameMetricBox contains a single metric to be displayed in the list of metrics in the side bar.
 */
public class MainFrameMetricBox extends CatListPanel {

    private MetricType type;
    private CatLabel metricDataLabel;
    MainFrameMetricList parent;
    private boolean dataAvailable;

    public MainFrameMetricBox(MetricType type, MainFrameMetricList parent) {
        this.type = type;
        this.parent = parent;
        this.init();
    }

    private void init() {

        CatLabel nameLabel = new CatLabel(type.toString());
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        metricDataLabel = new CatLabel(type.getErrorMessage());
        metricDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        metricDataLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(nameLabel);
        this.add(Box.createGlue());
        this.add(metricDataLabel);
        this.add(Box.createGlue());

        this.addMouseListener(new MetricBoxAdapter());
    }

    public void updateData(Number data) {
        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.CEILING);
        metricDataLabel.setFont(FontSettings.GLOB_FONT.getFont().deriveFont(20F));
        metricDataLabel.setText(df.format(data));
        this.dataAvailable = true;
        repaint();
    }

    public void reset() {
        metricDataLabel.setFont(FontSettings.GLOB_FONT.getFont());
        metricDataLabel.setText(this.type.getErrorMessage());
        this.dataAvailable = false;
    }

    class MetricBoxAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (dataAvailable) {
                parent.requestChart(type);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (dataAvailable) {
                MainFrameMetricBox.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                MainFrameMetricBox.this.setBackground(ColorSettings.BUTTON_HOVER_COLOR);
                repaint();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            MainFrameMetricBox.this.setBackground(ColorSettings.BG_COLOR);
            repaint();
        }
    }

}
