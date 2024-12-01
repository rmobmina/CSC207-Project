package presentation.ui.views;

import domain.entities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentation.ui.FavoritesManager;
import presentation.ui.windows.LocationsWindow;

import static org.junit.jupiter.api.Assertions.*;

public class FavoritesViewTest {

    private FavoritesManager favoritesManager;

    @BeforeEach
    public void setUp() {
        favoritesManager = new FavoritesManager();
        favoritesManager.clearFavorites(); // Clear favorites before each test
    }

    @Test
    public void testAddAndRemoveFavoritesFromView() {
        FavoritesManager favoritesManager = new FavoritesManager();
        favoritesManager.clearFavorites(); // Clear previous favorites

        String dummyApiKey = "test-api-key";
        LocationsWindow dummyWindow = new LocationsWindow(
                "Dummy Window", new int[]{500, 500}, null, dummyApiKey, null
        ) {
            @Override
            protected void getWeatherData() {
            }

            @Override
            protected void displayWeatherData() {
            }
        };

        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyWindow);

        // Simulate adding a favorite
        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);

        // Verify it appears in FavoritesView
        assert favoritesManager.getFavorites().contains(location) : "Favorites should contain Paris";

        // Simulate removing a favorite
        favoritesManager.removeFavorite(location);

        // Verify it's removed
        assert !favoritesManager.getFavorites().contains(location) : "Favorites should no longer contain Paris";
    }


    @Test
    public void testFavoritesViewLaunch() {
        String dummyApiKey = "";

        // Create a dummy LocationsWindow
        LocationsWindow dummyLocationsWindow = new LocationsWindow(
                "Dummy Window",
                new int[]{500, 500},
                null,
                dummyApiKey,
                null
        ) {
            @Override
            protected void getWeatherData() {
            }

            @Override
            protected void displayWeatherData() {
            }
        };


        // Launch the FavoritesView
        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyLocationsWindow);
        assertNotNull(favoritesView, "FavoritesView should be initialized.");
        favoritesView.setVisible(true);

        // Manually validate the UI interaction or use a UI testing framework
    }
}
