package presentation.ui.windows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import domain.entities.Location;

/**
 * Manages a list of favorite locations.
 * Provides methods for adding, removing, and retrieving favorites,
 * as well as persisting them to a file.
 */
public class FavoritesManager {

    static String FAVORITES_FILE = System.getProperty("user.dir") + "/favorites.txt";
    private List<Location> favorites;

    /**
     * Constructor to initialize the favorites list.
     */
    public FavoritesManager() {
        loadFavorites();
    }

    /**
     * Adds a location to the favorites list.
     *
     * @param location The location to add.
     */
    public void addFavorite(Location location) {
        if (location != null && !favorites.contains(location)) {
            favorites.add(location);
        }
    }

    /**
     * Removes a location from the favorites list.
     *
     * @param location The location to remove.
     */
    public void removeFavorite(Location location) {
        if (location != null) {
            favorites.remove(location);
        }
    }

    /**
     * Retrieves the list of favorite locations.
     *
     * @return A copy of the list of favorite locations.
     */
    public List<Location> getFavorites() {
        if (favorites == null) {
            return new ArrayList<>();
        }
        return favorites;
    }


    /**
     * Saves the favorites list to a file.
     */
    public void saveFavorites() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FAVORITES_FILE))) {
            oos.writeObject(favorites);
        } catch (IOException ioException) {
            System.err.println("Error saving favorites: " + ioException.getMessage());
        }
    }

    /**
     * Loads the favorites list from a file.
     * If the file doesn't exist, initializes an empty list.
     */
    public void loadFavorites() {
        final File file = new File(FAVORITES_FILE);
        favorites = new ArrayList<>();

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                favorites = (List<Location>) ois.readObject();
            } catch (IOException | ClassNotFoundException exception) {
                // handles the exception as needed
            }
        } else {
            System.out.println("Favorites file does not exist. Initializing empty favorites list.");
            favorites.clear();
        }
    }


    /**
     * Clears the list of favorite locations.
     */
    public void clearFavorites() {
        if (favorites == null) {
            favorites = new ArrayList<>();
        }
        favorites.clear();
    }

}