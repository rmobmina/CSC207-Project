package presentation.ui;

import domain.entities.Location;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    private List<Location> favorites;
    private static final String FAVORITES_FILE = System.getProperty("user.dir") + "/favorites.txt"; // File for persistence

    // Constructor: Initializes the favorites list
    public FavoritesManager() {
        loadFavorites();
    }

    // Add a location to the favorites list
    public void addFavorite(Location location) {
        if (!favorites.contains(location)) {
            favorites.add(location);
        }
    }

    // Remove a location from the favorites list
    public void removeFavorite(Location location) {
        favorites.remove(location);
    }

    // Get the list of favorite locations
    public List<Location> getFavorites() {
        return new ArrayList<>(favorites); // Return a copy of the list
    }

    // Save the favorites list to a file
    public void saveFavorites() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FAVORITES_FILE))) {
            oos.writeObject(favorites);
        } catch (IOException e) {
            System.err.println("Error saving favorites: " + e.getMessage());
        }
    }

    // Load the favorites list from a file
    @SuppressWarnings("unchecked")
    public void loadFavorites() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FAVORITES_FILE))) {
            favorites = (List<Location>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // File not found or empty: start with an empty list
            favorites = new ArrayList<>();
        }
    }

    // clears the list, used for testing
    public void clearFavorites() {
        favorites.clear();
    }

}
