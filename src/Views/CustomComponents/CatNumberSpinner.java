package Views.CustomComponents;

import javax.swing.*;

/**
 * CatNumberSpinner is a customised JSpinner to fit with the CatAnalysis application
 */
public class CatNumberSpinner extends JSpinner {

    public CatNumberSpinner(int startingValue) {
        int min = 0;
        int max = Integer.MAX_VALUE;
        int step = 1;
        SpinnerNumberModel model = new SpinnerNumberModel(startingValue, min, max, step);
        this.setModel(model);
        this.init();
    }

    private void init() {

    }
}
