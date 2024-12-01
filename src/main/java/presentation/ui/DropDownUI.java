package presentation.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import infrastructure.adapters.OpenWeatherApiService;


public class DropDownUI extends JPanel {
    private final JTextField locationField = new JTextField(20);
    private final JComboBox<String> locationDropdown = new JComboBox<>();
    private final String apiKey;

    private final Timer updateTimer;
    private boolean selectionMade;
    private List<Location> matchingLocations = new ArrayList<>();

    public DropDownUI(String apiKey, GetLocationDataUseCase locationDataUseCase) {
        this.apiKey = apiKey;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(locationField);
        add(locationDropdown);
        locationDropdown.setVisible(false);

        // Initialize the timer with a 500ms delay
        updateTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDropdown(locationField.getText(), locationDataUseCase);
            }
        });
        updateTimer.setRepeats(false);

        locationField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
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
                }
                else {
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

    public void resetSelection() {
        locationDropdown.removeAllItems(); // Clear all items
        locationDropdown.setVisible(false); // Hide the dropdown
    }

    private void updateDropdown(String input, GetLocationDataUseCase locationDataUseCase) {
        locationDropdown.removeAllItems();

        if (input.isEmpty()) {
            locationDropdown.setVisible(false);
            return;
        }

        matchingLocations = locationDataUseCase.execute(input, apiKey);
        if (matchingLocations.isEmpty()) {
            locationDropdown.setVisible(false);
        }
        else {
            for (Location location : matchingLocations) {
                locationDropdown.addItem(location.fullLocationName());
            }
            locationDropdown.setVisible(true);
            locationDropdown.showPopup();
        }
        locationDropdown.setSelectedIndex(-1);
    }

    public JTextField getLocationField()
    {
        return locationField;
    }
    public Location getSelectedLocation() {
        if (locationDropdown.getSelectedIndex() >= 0) {
            return matchingLocations.get(locationDropdown.getSelectedIndex());
        }
        return null;
    }

//    public static void main(String[] args) {
//        final JFrame frame = new JFrame("DropDown UI Example");
//        final String testApiKey = "0e85f616a96a624a0bf65bad89ff68c5"; // Bader's API Key
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        DropDownUI dropDownUI = new DropDownUI(testApiKey);
//        frame.add(dropDownUI);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }
}

