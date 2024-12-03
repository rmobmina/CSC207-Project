package presentation.ui.views;

import javax.swing.*;
import java.awt.*;

public class HelpInfoView {

    private JFrame frame;
    private JPanel panel;
    private JLabel helpLabel;

    public HelpInfoView() {
        frame = new JFrame("Help");
        panel = new JPanel();
        helpLabel = new JLabel("How to use");

        // Set up the frame
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        // Style the panel and label
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        helpLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        helpLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add the label to the panel
        panel.add(helpLabel, BorderLayout.CENTER);

        // Add the panel to the frame
        frame.add(panel);
    }

    public void display() {
        frame.setVisible(true);
    }
}
