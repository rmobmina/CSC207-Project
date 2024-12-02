package presentation.ui.views;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import domain.entities.Location;

import application.usecases.GetForecastWeatherDataUseCase;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.FavoritesManager;
import presentation.ui.NewDashBoardUi;
import presentation.ui.windows.LocationsWindow;

public class FavoritesView extends JFrame {
    private JList<String> favoritesList;
    private FavoritesManager favoritesManager;
    private LocationsWindow parentWindow;


    public FavoritesView(FavoritesManager favoritesManager, String apiKey, LocationsWindow parentWindow) {
        this.favoritesManager = favoritesManager;
        this.parentWindow = parentWindow; // Store the parent window reference
        setTitle("Favorites");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());


        // Create a list for displaying favorite locations
        favoritesList = new JList<>(getFavoritesAsStrings());
        add(new JScrollPane(favoritesList), BorderLayout.CENTER);

        // Add a selection listener to handle location selection
        favoritesList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click detected
                    String selectedLocationName = favoritesList.getSelectedValue();
                    if (selectedLocationName != null && !selectedLocationName.equals("No favorites added yet.")) {
                        Location selectedLocation = getLocationFromName(selectedLocationName);
                        if (selectedLocation != null) {
                            openForecastDailyView(selectedLocation); // Open ForecastDailyView for the selected location
                        } else {
                            JOptionPane.showMessageDialog(FavoritesView.this,
                                    "Error: Selected location not found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });


        // Button to remove a selected favorite
        JButton removeButton = new JButton("Remove Favorite");
        removeButton.addActionListener(e -> {
            int selectedIndex = favoritesList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedLocation = favoritesList.getSelectedValue();
                removeFavoriteByName(selectedLocation);
            }
        });
        add(removeButton, BorderLayout.SOUTH);
    }

    private DefaultListModel<String> getFavoritesAsStrings() {
        DefaultListModel<String> model = new DefaultListModel<>();
        List<Location> favorites = favoritesManager.getFavorites();

        // Error handling: Check if favorites is null
        if (favorites == null) {
            favorites = new ArrayList<>();
        }

        for (Location location : favorites) {
            if (location != null) {
                model.addElement(location.fullLocationName());
            }
        }

        // Empty list handling: Add a placeholder message if no favorites exist
        if (model.isEmpty()) {
            model.addElement("No favorites added yet.");
        }

        return model;
    }

    private void removeFavoriteByName(String locationName) {
        List<Location> favorites = favoritesManager.getFavorites();

        // Error handling: Check if favorites is null
        if (favorites == null) {
            JOptionPane.showMessageDialog(this, "No favorites found to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Location loc : favorites) {
            if (loc.fullLocationName().equals(locationName)) {
                favoritesManager.removeFavorite(loc);
                favoritesList.setModel(getFavoritesAsStrings()); // Refresh the list
                favoritesList.updateUI(); // Ensure UI reflects changes immediately
                JOptionPane.showMessageDialog(this, "Favorite removed!");
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Location not found in favorites.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Helper method to retrieve a Location object from its name
    private Location getLocationFromName(String locationName) {
        for (Location loc : favoritesManager.getFavorites()) {
            if (loc.fullLocationName().equals(locationName)) {
                return loc;
            }
        }
        return null;
    }


    // Opens the ForecastDailyView for the selected location
    private void openForecastDailyView(Location location) {
        if (location != null) {
            this.setVisible(false); // Hide the current favorites view
            if (parentWindow != null) {
                parentWindow.setSearchBarText(location.fullLocationName()); // Update the search bar
                parentWindow.setWeatherLocation(location); // Update and fetch weather
            } else {
                JOptionPane.showMessageDialog(this, "Error: Parent window reference is null.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error: Location not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}

