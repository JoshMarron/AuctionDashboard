package Views.CustomComponents;

import javax.swing.*;
import java.io.File;

/**
 * Created by marro on 10/03/2017.
 */
public class CatFileChooser extends JFileChooser {

    public CatFileChooser(File homedir) {
        super(homedir);
        initChooser();
    }

    private void initChooser() {

    }
}
