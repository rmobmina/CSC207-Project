package presentation.ui.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SelectNumberLocationsView extends View {

    private static JLabel numOfLocationsLabel = new JLabel();
    private static JTextField numOfLocationsText = new JTextField();
    private static JButton confirmButton = new JButton("Confirm");

    public SelectNumberLocationsView() {
        panel.setLayout(new GridLayout(3, 2, 20, 10));
        numOfLocationsLabel.setText("Please choose you desired number of Locations:");
        panel.add(numOfLocationsLabel);
        panel.add(numOfLocationsText);
        numOfLocationsText.setPreferredSize(new Dimension(10, 10));
        panel.add(confirmButton);

        panel.setVisible(true);
    }

    public int getNumOfLocations() {
        try {
            return Integer.parseInt(numOfLocationsText.getText());
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setActionListener(ActionListener listener) {
        confirmButton.addActionListener(listener);
    }

}
