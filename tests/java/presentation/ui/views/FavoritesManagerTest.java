package presentation.ui.views;

import domain.entities.Location;
import presentation.ui.FavoritesManager;

public class FavoritesManagerTest {
    public static void main(String[] args) {
        FavoritesManager favoritesManager = new FavoritesManager();

        // Clear any existing favorites for clean testing
        favoritesManager.clearFavorites();

        // Test adding favorites
        Location location1 = new Location("New York", "New York", "USA", 40.7128, -74.0060);
        Location location2 = new Location("Toronto", "Ontario", "Canada", 43.65107, -79.347015);

        System.out.println("Adding locations to favorites...");
        favoritesManager.addFavorite(location1);
        favoritesManager.addFavorite(location2);

        assert favoritesManager.getFavorites().size() == 2 : "Favorites size should be 2 after adding two unique locations.";

        System.out.println("Attempting to add duplicate location...");
        favoritesManager.addFavorite(location1);

        assert favoritesManager.getFavorites().size() == 2 : "Favorites size should still be 2 after adding a duplicate.";

        System.out.println("Removing New York from favorites...");
        favoritesManager.removeFavorite(location1);

        assert favoritesManager.getFavorites().size() == 1 : "Favorites size should be 1 after removing a location.";

        assert favoritesManager.getFavorites().get(0).fullLocationName().equals(location2.fullLocationName()) :
            "Remaining favorite should be Toronto.";

        System.out.println("Saving favorites...");
        favoritesManager.saveFavorites();

        System.out.println("Reloading favorites...");
        FavoritesManager newFavoritesManager = new FavoritesManager();

        assert newFavoritesManager.getFavorites().size() == 1 : "Favorites size after reload should be 1.";
        assert newFavoritesManager.getFavorites().get(0).fullLocationName().equals(location2.fullLocationName()) :
            "Reloaded favorite should be Toronto.";

        System.out.println("All tests passed!");
    }
}
