package presentation.ui.views;

import javax.swing.*;

public class MainMenuView extends View {

    private JLabel mainLabel = new JLabel("Welcome to the Weather App");

    public MainMenuView() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        mainLabel.setSize(40,20);
        panel.add(mainLabel);
    }
}
