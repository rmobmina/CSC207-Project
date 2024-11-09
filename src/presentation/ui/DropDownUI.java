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

    // Sample list (Will be replaced soon with the cities file)
    private final List<String> cityList = Arrays.asList("alberta", "bader", "Los Angeles", "London", "Lagos", "Lisbon", "Lima", "Lahore", "Luxembourg");

    // B: this creates the dropdown box
    public DropDownUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(locationField);
        add(locationDropdown);
        locationDropdown.setVisible(false); // this makes the drop menu invisible until we input something.
        // might change it so that the menu button is visible and drops down when pressed

        // Add KeyListener to the locationField
        locationField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = locationField.getText();
                updateDropdown(input);
            }
        });

        locationDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCity = (String) locationDropdown.getSelectedItem();
                if (selectedCity != null) {
                    locationField.setText(selectedCity);
                    locationDropdown.setVisible(false);
                }
            }
        });

    }

    // BUG TO BE FIXED: search bar does not let me delete characters
    private void updateDropdown(String input) {
        locationDropdown.removeAllItems(); // Clear previous items

        if (input.isEmpty()) {
            locationDropdown.setVisible(false); // Hide dropdown if input is empty
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
            locationDropdown.setVisible(false); // Hide if no matches
        } else {
            // loop that adds simlarly named cities
            for (String city : matchingCities) {
                locationDropdown.addItem(city);
            }
            locationDropdown.setVisible(true); // Show the dropdown if matches found
        }
    }



    public JTextField getLocationField() {
        return locationField;
    }
}
