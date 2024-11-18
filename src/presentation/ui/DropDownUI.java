package presentation.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DropDownUI extends JPanel {
    private final JTextField locationField = new JTextField(20);
    private final JComboBox<String> locationDropdown = new JComboBox<>();
    private final List<String> cityList = Arrays.asList("alberta", "Los Angeles", "London", "Lagos", "Lisbon", "Lima", "Lahore", "Luxembourg");

    private Timer updateTimer;
    private boolean selectionMade = false;

    public DropDownUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(locationField);
        add(locationDropdown);
        locationDropdown.setVisible(false);

        // Initialize the timer with a 500ms delay
        updateTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDropdown(locationField.getText());
            }
        });
        updateTimer.setRepeats(false);

        locationField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Removed the method to check if the search bar is empty so that selection made is false as soon as the user starts typing
                if (selectionMade) {
                    selectionMade = false;
                }

                // Check if the down arrow key is pressed
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    // If dropdown has items, focus on it and show it
                    if (locationDropdown.getItemCount() > 0) {
                        locationDropdown.requestFocus();
                        locationDropdown.setPopupVisible(true);
                    }
                } else {
                    // Restart the timer for other keys
                    updateTimer.restart();
                }
            }
        });

        locationDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (locationDropdown.isPopupVisible() && locationDropdown.getSelectedItem() != null) {
                    locationField.setText((String) locationDropdown.getSelectedItem());
                    locationDropdown.setVisible(false);
                    selectionMade = true;
                }
            }
        });
    }

    private void updateDropdown(String input) {
        locationDropdown.removeAllItems();

        if (input.isEmpty()) {
            locationDropdown.setVisible(false);
            return;
        }

        // Filter the city list based on the input
        List<String> matchingCities = new ArrayList<>();
        for (String city : cityList) {
            if (city.toLowerCase().startsWith(input.toLowerCase())) {
                matchingCities.add(city);
            }
        }

        if (matchingCities.isEmpty()) {
            locationDropdown.setVisible(false);
        }
        else {
            for (String city : matchingCities) {
                locationDropdown.addItem(city);
            }
            locationDropdown.setVisible(true);
            locationDropdown.showPopup();
        }

        locationDropdown.setSelectedIndex(-1);
    }

    public JTextField getLocationField() {
        return locationField;
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("DropDown UI Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new DropDownUI());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
