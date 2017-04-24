package Views.CustomComponents;

import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * CatFrame is a JFrame where the glass pane has already been initialised for loading
 */
public class CatFrame extends JFrame {

    protected boolean loading;
    protected File homedir;

    public CatFrame() {

    }

    protected void init() {

        CatPanel contentPane = new CatPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2 = (Graphics2D) g;

                if (loading) {
                    g2.setColor(ColorSettings.LOADING_COLOR);
                    g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                }
            }
        };
        contentPane.setLayout(new BorderLayout());
        this.setContentPane(contentPane);
    }

    public void initGlassPane() {
        JPanel loadingPanel = (JPanel) this.getGlassPane();
        loadingPanel.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("img/animal.gif");
        icon.setImage(icon.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
        JLabel loadingLabel = new JLabel(icon);

        JLabel textLoadingLabel = new JLabel("Loading...");
        textLoadingLabel.setFont(FontSettings.LOADING_FONT.getFont());
        textLoadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLoadingLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel textLoadingPanel = new JPanel();
        textLoadingPanel.setLayout(new BoxLayout(textLoadingPanel, BoxLayout.X_AXIS));
        textLoadingPanel.add(Box.createRigidArea(new Dimension(0, 100)));
        textLoadingPanel.add(Box.createHorizontalGlue());
        textLoadingPanel.add(textLoadingLabel);
        textLoadingPanel.add(Box.createHorizontalGlue());
        textLoadingPanel.setOpaque(false);

        loadingPanel.add(loadingLabel, BorderLayout.CENTER);
        loadingPanel.add(textLoadingPanel, BorderLayout.SOUTH);
    }

    public void displayLoading() {
        this.loading = true;
        this.getGlassPane().setVisible(true);
        this.getContentPane().setEnabled(false);
        repaint();
    }

    public void finishedLoading() {
        this.loading = false;
        this.getGlassPane().setVisible(false);

        this.setEnabled(true);
        repaint();
    }
}
