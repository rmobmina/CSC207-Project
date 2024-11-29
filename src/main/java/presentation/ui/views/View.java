package presentation.ui.views;

import javax.swing.*;

public class View {
    public JPanel panel = new JPanel();

    public JPanel getWindow(){
        return panel;
    }

    public void showWindow(){
        panel.setVisible(true);
    }

    public void hideWindow(){
        panel.setVisible(false);
    }
}
