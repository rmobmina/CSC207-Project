package presentation.ui.views;

import javax.swing.*;

/**
 * A class that represents a basic view that gets its contents displayed on the dash board
 */
public class View {
    public JPanel panel = new JPanel();

    public JPanel getPanel(){
        return panel;
    }

    public void showPanel(){
        panel.setVisible(true);
    }

    public void hidePanel(){
        panel.setVisible(false);
    }
}