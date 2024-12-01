package presentation.ui.windows;

import domain.entities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentation.ui.FavoritesManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FavoritesManagerTest {

    private FavoritesManager favoritesManager;

    @BeforeEach
    public void setUp() {
        // Initialize a new FavoritesManager instance before each test
        favoritesManager = new FavoritesManager();
        favoritesManager.clearFavorites(); // Clear any existing favorites for a clean test environment
    }

    @Test
    public void testAddFavorites() {
        Location location1 = new Location("New York", "New York", "USA", 40.7128, -74.0060);
        Location location2 = new Location("Toronto", "Ontario", "Canada", 43.65107, -79.347015);

        favoritesManager.addFavorite(location1);
        favoritesManager.addFavorite(location2);

        assertEquals(2, favoritesManager.getFavorites().size(), "Favorites size should be 2 after adding two unique locations.");

        // Attempt to add a duplicate
        favoritesManager.addFavorite(location1);
        assertEquals(2, favoritesManager.getFavorites().size(), "Favorites size should still be 2 after adding a duplicate.");
    }

    @Test
    public void testRemoveFavorites() {
        Location location1 = new Location("New York", "New York", "USA", 40.7128, -74.0060);
        Location location2 = new Location("Toronto", "Ontario", "Canada", 43.65107, -79.347015);

        favoritesManager.addFavorite(location1);
        favoritesManager.addFavorite(location2);

        favoritesManager.removeFavorite(location1);

        assertEquals(1, favoritesManager.getFavorites().size(), "Favorites size should be 1 after removing a location.");
        assertEquals(location2.fullLocationName(), favoritesManager.getFavorites().get(0).fullLocationName(), "Remaining favorite should be Toronto.");
    }

    @Test
    public void testSaveAndLoadFavorites() {
        Location location1 = new Location("New York", "New York", "USA", 40.7128, -74.0060);
        Location location2 = new Location("Toronto", "Ontario", "Canada", 43.65107, -79.347015);

        favoritesManager.addFavorite(location1);
        favoritesManager.addFavorite(location2);

        favoritesManager.saveFavorites();

        FavoritesManager newFavoritesManager = new FavoritesManager();
        newFavoritesManager.loadFavorites();

        assertEquals(2, newFavoritesManager.getFavorites().size(), "Favorites size after reload should be 2.");
        assertEquals(location1.fullLocationName(), newFavoritesManager.getFavorites().get(0).fullLocationName(), "Reloaded favorite should include New York.");
        assertEquals(location2.fullLocationName(), newFavoritesManager.getFavorites().get(1).fullLocationName(), "Reloaded favorite should include Toronto.");
    }

    @Test
    public void testClearFavorites() {
        Location location1 = new Location("New York", "New York", "USA", 40.7128, -74.0060);

        favoritesManager.addFavorite(location1);
        assertEquals(1, favoritesManager.getFavorites().size(), "Favorites size should be 1 after adding one location.");

        favoritesManager.clearFavorites();
        assertTrue(favoritesManager.getFavorites().isEmpty(), "Favorites list should be empty after clearing.");
    }

    @Test
    public void testFilePersistence() {
        File favoritesFile = new File(System.getProperty("user.dir") + "/favorites.txt");

        // Ensure the file exists after saving favorites
        favoritesManager.addFavorite(new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522));
        favoritesManager.saveFavorites();
        assertTrue(favoritesFile.exists(), "Favorites file should exist after saving.");

        // Clean up the file after the test
        favoritesFile.delete();
    }
}
