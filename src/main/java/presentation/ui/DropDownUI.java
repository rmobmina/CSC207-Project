package presentation.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;

/**
 * The `DropDownUI` class provides a user interface component for selecting a location.
 * It includes a text field for entering a location name and a dropdown for displaying
 * matching locations as the user types. The dropdown updates dynamically based on the input.
 */
public class DropDownUI extends JPanel {
    private final JTextField locationField = new JTextField(20);
    private final JComboBox<String> locationDropdown = new JComboBox<>();
    private final String apiKey;

    private final Timer updateTimer;
    private boolean selectionMade;
    private List<Location> matchingLocations = new ArrayList<>();

    /**
     * Constructs a `DropDownUI` instance.
     *
     * @param apiKey              The API key for accessing location data.
     * @param locationDataUseCase The use case for retrieving matching locations.
     */
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

    /**
     * Resets the dropdown selection by clearing all items and hiding the dropdown.
     */
    public void resetSelection() {
        locationDropdown.removeAllItems();
        locationDropdown.setVisible(false);
    }

    /**
     * Updates the dropdown with matching locations based on user input.
     *
     * @param input               The user input text to search for matching locations.
     * @param locationDataUseCase The use case for retrieving matching locations.
     */
    private void updateDropdown(String input, GetLocationDataUseCase locationDataUseCase) {
        locationDropdown.removeAllItems();

        // Trim the input but allow leading/trailing spaces in the text field
        if (input.trim().isEmpty()) {
            locationDropdown.setVisible(false);
            return;
        }

        matchingLocations = locationDataUseCase.execute(input.trim(), apiKey);
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

    /**
     * Retrieves the text field for entering a location name.
     *
     * @return The location input text field.
     */
    public JTextField getLocationField() {
        return locationField;
    }

    /**
     * Retrieves the currently selected location from the dropdown.
     *
     * @return The selected `Location` object, or `null` if no selection is made.
     */
    public Location getSelectedLocation() {
        if (locationDropdown.getSelectedIndex() >= 0) {
            return matchingLocations.get(locationDropdown.getSelectedIndex());
        }
        return null;
    }
}

