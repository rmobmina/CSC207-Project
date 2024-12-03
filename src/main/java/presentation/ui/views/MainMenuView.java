package presentation.ui.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuView extends View {
    private JLabel mainLabel = new JLabel("<html><center>The<br/>Weather<br/>App</center></html>");
    private JButton startButton = new JButton("Start");
    private JButton helpButton = new JButton("Help");

    public MainMenuView() {
        // Use GridBagLayout for flexibility
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        // Style the main label (title)
        mainLabel.setFont(new Font("Serif", Font.BOLD, 90));
        mainLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add the label to the layout
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        gbc.weightx = 1.0; // Allow horizontal stretching
        gbc.weighty = 0.6; // Allow vertical space allocation
        gbc.fill = GridBagConstraints.BOTH; // Resize both dimensions
        gbc.insets = new Insets(20, 10, 20, 10); // Add spacing
        panel.add(mainLabel, gbc);

        // Style and add the Start button
        startButton.setFont(new Font("SansSerif", Font.PLAIN, 30));
        gbc.gridy = 1; // Row 1
        gbc.weighty = 0.2; // Allocate vertical space
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(startButton, gbc);

        // Style and add the Help button
        helpButton.setFont(new Font("SansSerif", Font.PLAIN, 30));
        gbc.gridy = 2; // Row 2
        gbc.weighty = 0.2; // Allocate vertical space
        panel.add(helpButton, gbc);
    }

    public void setStartActionListener(ActionListener actionListener) {
        startButton.addActionListener(actionListener);
    }

    public void setHelpActionListener(ActionListener actionListener) {
        helpButton.addActionListener(actionListener);
    }
}
