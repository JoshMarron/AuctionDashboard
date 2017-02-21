package Views;

import javax.swing.*;

/**
 * Created by marro on 21/02/2017.
 */
public class DashboardMainFrame extends JFrame {

    public DashboardMainFrame() {

    }

    public void init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1200, 900);
        this.setName("Ad Auction Dashboard");

        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        this.setVisible(true);
    }

}
