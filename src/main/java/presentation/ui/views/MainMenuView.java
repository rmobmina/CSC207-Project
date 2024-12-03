package presentation.ui.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuView extends View {

    private JLabel mainLabel = new JLabel("Welcome to the Weather App");

    private JButton startButton = new JButton("Start");

    public MainMenuView() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        mainLabel.setSize(40,20);
        panel.add(mainLabel);
        panel.add(startButton);
    }

    public void setStartActionListener(ActionListener actionListener) {
        startButton.addActionListener(actionListener);
    }
}
