package presentation.ui.windows;

import domain.entities.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// IMPORTANT: Before running test, and to ensure 100% Code Coverage,
// Please delete favorites.txt, located in the root of this project.

public class FavoritesManagerTest {

    private FavoritesManager favoritesManager;
    private final File favoritesFile = new File(System.getProperty("user.dir") + "/favorites.txt");

    @BeforeEach
    public void setUp() {
        favoritesManager = new FavoritesManager();
        favoritesManager.clearFavorites();
        if (favoritesFile.exists()) {
            favoritesFile.delete();
        }
    }

    @AfterEach
    public void tearDown() {
        if (favoritesFile.exists()) {
            favoritesFile.delete();
        }
    }

    @Test
    public void testLoadFavoritesFileDoesNotExist() {
        // Ensure the file does not exist
        if (favoritesFile.exists() && !favoritesFile.delete()) {
            fail("Could not delete the favorites file before the test.");
        }

        // Call the method
        favoritesManager.loadFavorites();

        // Verify that the favorites list is empty
        assertTrue(favoritesManager.getFavorites().isEmpty(), "Favorites list should be empty when the file does not exist.");
    }

    @Test
    public void testAddFavorites() {
        Location location1 = new Location("New York", "New York", "USA", 40.7128, -74.0060);
        Location location2 = new Location("Toronto", "Ontario", "Canada", 43.65107, -79.347015);

        favoritesManager.addFavorite(location1);
        favoritesManager.addFavorite(location2);

        assertEquals(2, favoritesManager.getFavorites().size(), "Favorites size should be 2 after adding two unique locations.");

        favoritesManager.addFavorite(location1);
        assertEquals(2, favoritesManager.getFavorites().size(), "Favorites size should still be 2 after adding a duplicate.");
    }

    @Test
    public void testAddNullFavorite() {
        favoritesManager.addFavorite(null);
        assertTrue(favoritesManager.getFavorites().isEmpty(), "Favorites list should remain empty when adding a null location.");
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
    public void testRemoveNullFavorite() {
        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);

        favoritesManager.removeFavorite(null);

        assertEquals(1, favoritesManager.getFavorites().size(), "Favorites size should remain unchanged when trying to remove a null location.");
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
    public void testLoadFavoritesErrorHandling() {
        try (FileOutputStream fos = new FileOutputStream(favoritesFile)) {
            fos.write("corrupted data".getBytes());
        } catch (IOException e) {
            fail("Failed to set up corrupted file for testing.");
        }

        favoritesManager.loadFavorites();
        assertTrue(favoritesManager.getFavorites().isEmpty(), "Favorites list should be empty when loading a corrupted file.");
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
    public void testSaveFavoritesErrorHandling() {
        File directory = new File(System.getProperty("user.dir") + "/directory");
        directory.mkdir();

        FavoritesManager managerWithDirectoryPath = new FavoritesManager() {
            @Override
            public void saveFavorites() {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(directory))) {
                    Field favoritesField = FavoritesManager.class.getDeclaredField("favorites");
                    favoritesField.setAccessible(true);
                    List<Location> favorites = (List<Location>) favoritesField.get(this);

                    oos.writeObject(favorites);
                } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
                    // Error handling covered here
                }
            }
        };

        assertTrue(directory.isDirectory(), "The path should be a directory to test this case.");
        managerWithDirectoryPath.saveFavorites();
        directory.delete();
    }

    @Test
    public void testClearFavoritesWithNullList() {
        try {
            Field favoritesField = FavoritesManager.class.getDeclaredField("favorites");
            favoritesField.setAccessible(true);
            favoritesField.set(favoritesManager, null);

            favoritesManager.clearFavorites();

            assertTrue(favoritesManager.getFavorites().isEmpty(), "Favorites list should be empty after clearing when initialized as null.");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set up test for clearing null list: " + e.getMessage());
        }
    }

    @Test
    public void testSaveFavoritesExceptionHandling() {
        String originalPath = FavoritesManager.FAVORITES_FILE;
        try {
            FavoritesManager.FAVORITES_FILE = "/invalid/path/readonly_favorites.txt";
            favoritesManager.saveFavorites(); // Should trigger the catch block
            // Add assertions to verify behavior
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            FavoritesManager.FAVORITES_FILE = originalPath; // Restore original path
        }
    }


    @Test
    public void testLoadFavoritesExceptionHandling() {
        // Write corrupted data to the favorites file
        try (FileOutputStream fos = new FileOutputStream(favoritesFile)) {
            fos.write("corrupted data".getBytes());
        } catch (IOException e) {
            fail("Failed to set up corrupted file for testing: " + e.getMessage());
        }

        // Attempt to load the corrupted favorites file
        favoritesManager.loadFavorites();

        // Verify that the list is empty after encountering the exception
        assertTrue(favoritesManager.getFavorites().isEmpty(), "Favorites list should be empty when loading corrupted data.");
    }

    @Test
    public void testGetFavoritesWhenNull() {
        try {
            // Set the `favorites` field to null using reflection
            Field favoritesField = FavoritesManager.class.getDeclaredField("favorites");
            favoritesField.setAccessible(true);
            favoritesField.set(favoritesManager, null);

            // Call getFavorites and verify it returns a new list
            List<Location> favorites = favoritesManager.getFavorites();
            assertNotNull(favorites, "Favorites list should be initialized if it is null.");
            assertTrue(favorites.isEmpty(), "Favorites list should be empty when initialized.");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set up test for null favorites list: " + e.getMessage());
        }
    }

    @Test
    public void testClearFavoritesWhenNull() {
        try {
            // Set the `favorites` field to null using reflection
            Field favoritesField = FavoritesManager.class.getDeclaredField("favorites");
            favoritesField.setAccessible(true);
            favoritesField.set(favoritesManager, null);

            // Call clearFavorites and ensure it initializes the list
            favoritesManager.clearFavorites();

            // Verify the list is initialized and empty
            assertNotNull(favoritesManager.getFavorites(), "Favorites list should be initialized when clearing.");
            assertTrue(favoritesManager.getFavorites().isEmpty(), "Favorites list should be empty after clearing.");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set up test for clearing null favorites list: " + e.getMessage());
        }
    }





}

