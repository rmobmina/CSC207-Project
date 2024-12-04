package presentation.ui.views;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import domain.entities.Location;
import presentation.ui.windows.FavoritesManager;
import presentation.ui.windows.LocationsWindow;

/**
 * FavoritesView class is a JFrame that displays a list of favorite locations
 * and gives us the feature to remove chosen locations.
 */
public class FavoritesView extends JFrame {
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 300;
    private static final String ERROR_TITLE = "Error";

    private final JList<String> favoritesList;
    private final FavoritesManager favoritesManager;
    private final LocationsWindow parentWindow;
    private final JButton removeButton;

    /**
     * Constructs a FavoritesView instance.
     *
     * @param favoritesManager the FavoritesManager instance
     * @param apiKey           the API key (currently unused)
     * @param parentWindow     the parent LocationsWindow instance
     */
    public FavoritesView(FavoritesManager favoritesManager, String apiKey, LocationsWindow parentWindow) {
        this.favoritesManager = favoritesManager;
        this.parentWindow = parentWindow;
        setTitle("Favorites");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        favoritesList = new JList<>(getFavoritesAsStrings());
        add(new JScrollPane(favoritesList), BorderLayout.CENTER);

        favoritesList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleFavoritesListDoubleClick();
                }
            }
        });

        removeButton = new JButton("Remove Favorite");
        removeButton.addActionListener(event -> handleRemoveFavorite());
        add(removeButton, BorderLayout.SOUTH);
    }

    /**
     * Handles a double-click event on the favorites list.
     * Opens the forecast view for the selected location if valid.
     */
    private void handleFavoritesListDoubleClick() {
        final String selectedLocationName = favoritesList.getSelectedValue();
        if (selectedLocationName != null && !"No favorites added yet.".equals(selectedLocationName)) {
            final Location selectedLocation = getLocationFromName(selectedLocationName);
            if (selectedLocation != null) {
                openForecastDailyView(selectedLocation);
            }
            else {
                showError("Selected location not found.");
            }
        }
    }

    /**
     * Handles the removal of the selected favorite from the list.
     * Removes the selected favorite if a valid selection is made.
     */

    private void handleRemoveFavorite() {
        final int selectedIndex = favoritesList.getSelectedIndex();
        if (selectedIndex != -1) {
            final String selectedLocation = favoritesList.getSelectedValue();
            removeFavoriteByName(selectedLocation);
        }
    }

    protected DefaultListModel<String> getFavoritesAsStrings() {
        final DefaultListModel<String> model = new DefaultListModel<>();
        final List<Location> favorites = favoritesManager.getFavorites();

        if (favorites == null || favorites.isEmpty()) {
            model.addElement("No favorites added yet.");
            return model;
        }

        for (Location location : favorites) {
            if (location != null) {
                model.addElement(location.fullLocationName());
            }
        }

        if (model.isEmpty()) {
            model.addElement("No favorites added yet.");
        }

        return model;
    }

    protected void removeFavoriteByName(String locationName) {
        final List<Location> favorites = favoritesManager.getFavorites();
        if (favorites == null || favorites.isEmpty()) {
            showError("No favorites found to remove.");
            return;
        }

        boolean removed = false;
        for (Location loc : favorites) {
            if (loc.fullLocationName().equals(locationName)) {
                favoritesManager.removeFavorite(loc);
                refreshFavoritesList();
                JOptionPane.showMessageDialog(this, "Favorite removed!");
                removed = true;
                break;
            }
        }

        if (!removed) {
            showError("Location not found in favorites.");
        }
    }

    /**
     * Retrieves a Location object from its name.
     *
     * @param locationName The name of the location to retrieve.
     * @return The Location object if found; otherwise, null.
     */
    public Location getLocationFromName(String locationName) {
        final List<Location> favorites = favoritesManager.getFavorites();
        if (favorites == null || favorites.isEmpty()) {
            return null;
        }

        for (Location loc : favorites) {
            if (loc.fullLocationName().equals(locationName)) {
                return loc;
            }
        }
        return null;
    }

    void openForecastDailyView(Location location) {
        if (location != null) {
            this.setVisible(false);
            if (parentWindow != null) {
                parentWindow.setSearchBarText(location.fullLocationName());
                parentWindow.setWeatherLocation(location);
            }
            else {
                showError("Parent window reference is null.");
            }
        }
        else {
            showError("Location not found.");
        }
    }

    void showError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    public JList<String> getFavoritesList() {
        return favoritesList;
    }

    /**
     * Refreshes the displayed list of favorite locations.
     */
    public void refreshFavoritesList() {
        favoritesList.setModel(getFavoritesAsStrings());
        favoritesList.updateUI();
    }

    public JButton getRemoveButton() {
        return removeButton;
    }
}

