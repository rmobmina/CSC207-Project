package presentation.ui.views;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * The MainMenuView class represents the main menu of the Weather App.
 * It displays the title of the application and provides buttons for starting the app
 * or accessing the help section.
 */
public class MainMenuView extends View {
    private final JLabel mainLabel = new JLabel("<html><center>The<br/>Weather<br/>App</center></html>");
    private final JButton startButton = new JButton("Start");
    private final JButton helpButton = new JButton("Help");

    public MainMenuView() {
        // Use GridBagLayout for flexibility
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        final GridBagConstraints gbc = new GridBagConstraints();

        // Style the main label (title)
        mainLabel.setFont(new Font("Serif", Font.BOLD, 90));
        mainLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add the label to the layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 10, 20, 10);
        panel.add(mainLabel, gbc);

        // Style and add the Start button
        startButton.setFont(new Font("SansSerif", Font.PLAIN, 30));
        gbc.gridy = 1;
        gbc.weighty = 0.2;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(startButton, gbc);

        // Style and add the Help button
        helpButton.setFont(new Font("SansSerif", Font.PLAIN, 30));
        gbc.gridy = 2;
        gbc.weighty = 0.2;
        panel.add(helpButton, gbc);
    }

    /**
     * Sets the action listener for the Start button.
     *
     * @param actionListener the ActionListener to be triggered when the Start button is clicked.
     */
    public void setStartActionListener(ActionListener actionListener) {

        startButton.addActionListener(actionListener);
    }

    /**
     * Sets the action listener for the Help button.
     *
     * @param actionListener the ActionListener to be triggered when the Help button is clicked.
     */
    public void setHelpActionListener(ActionListener actionListener) {

        helpButton.addActionListener(actionListener);
    }
}
