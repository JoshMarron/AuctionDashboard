package Views.CustomComponents;

import javax.swing.*;
import java.awt.*;

/**
 * CatTitlePanel just takes a string and creates a title panel to be inserted - this should help eliminate duplicate code
 */
public class CatTitlePanel extends CatPanel {

    private String title;

    public CatTitlePanel(String title) {
        this.title = title;
        this.initTitlePanel();
    }

    private void initTitlePanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        CatLabel titleLabel = new CatLabel(title);

        this.add(Box.createRigidArea(new Dimension(0, 40)));
        this.add(Box.createHorizontalGlue());
        this.add(titleLabel);
        this.add(Box.createHorizontalGlue());

    }
}
