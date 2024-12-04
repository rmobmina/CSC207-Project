package presentation.ui.views;

import domain.entities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import presentation.ui.windows.FavoritesManager;
import presentation.ui.windows.LocationsWindow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class FavoritesViewTest {

    private FavoritesManager favoritesManager;

    @BeforeEach
    public void setUp() {
        favoritesManager = new FavoritesManager();
        favoritesManager.clearFavorites();
    }

    @Test
    public void testFavoritesViewInitialization() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);
        assertNotNull(favoritesView, "FavoritesView should be initialized.");
        assertEquals("No favorites added yet.", favoritesView.getFavoritesList().getModel().getElementAt(0),
                "Placeholder message should be displayed for an empty favorites list.");
    }

    @Test
    public void testAddAndRemoveFavorites() {
        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);

        favoritesManager.addFavorite(location);
        assertTrue(favoritesManager.getFavorites().contains(location), "Favorites should contain the added location.");

        favoritesManager.removeFavorite(location);
        assertFalse(favoritesManager.getFavorites().contains(location), "Favorites should not contain the removed location.");
    }

    @Test
    public void testRefreshFavoritesList() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);
        favoritesView.refreshFavoritesList();

        assertEquals(location.fullLocationName(), favoritesView.getFavoritesList().getModel().getElementAt(0),
                "FavoritesView should display the added location.");
    }

    @Test
    public void testRemoveFavoriteFromView() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);
        favoritesView.refreshFavoritesList();

        favoritesView.getFavoritesList().setSelectedIndex(0);
        favoritesView.getRemoveButton().doClick();

        assertFalse(favoritesManager.getFavorites().contains(location), "Favorites should not contain the removed location.");
    }

    @Test
    public void testDoubleClickFavoriteInteraction() {
        LocationsWindow mockWindow = mock(LocationsWindow.class);
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", mockWindow);

        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);
        favoritesView.refreshFavoritesList();

        favoritesView.getFavoritesList().setSelectedValue(location.fullLocationName(), false);
        favoritesView.getFavoritesList().dispatchEvent(new java.awt.event.MouseEvent(
                favoritesView.getFavoritesList(), java.awt.event.MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, 2, false
        ));

        Mockito.verify(mockWindow).setSearchBarText(location.fullLocationName());
    }

    @Test
    public void testHandleFavoritesListDoubleClickWithInvalidSelection() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        favoritesView.getFavoritesList().setSelectedValue(null, false);
        favoritesView.getFavoritesList().dispatchEvent(new java.awt.event.MouseEvent(
                favoritesView.getFavoritesList(), java.awt.event.MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, 2, false
        ));
        // Expect no crash or exception
    }

    @Test
    public void testRemoveNonexistentFavorite() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        favoritesView.removeFavoriteByName("Nonexistent Location");
        assertTrue(favoritesManager.getFavorites().isEmpty(),
                "Favorites list should remain empty after trying to remove a nonexistent favorite.");
    }

    @Test
    public void testGetFavoritesAsStringsHandlesNullFavorites() {
        FavoritesManager mockManager = mock(FavoritesManager.class);
        Mockito.when(mockManager.getFavorites()).thenReturn(null);

        FavoritesView favoritesView = new FavoritesView(mockManager, "dummyApiKey", null);
        DefaultListModel<String> model = favoritesView.getFavoritesAsStrings();

        assertEquals(1, model.getSize(), "List model should contain one placeholder item.");
        assertEquals("No favorites added yet.", model.getElementAt(0), "Placeholder message should be displayed.");
    }

    @Test
    public void testGetLocationFromName() {
        Location paris = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(paris);

        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        Location result = favoritesView.getLocationFromName("Paris, Ile-de-France, France");
        assertNotNull(result, "Result should not be null when location is found.");
        assertEquals(paris, result, "Result should match the location object.");
    }


    @Test
    public void testOpenForecastDailyViewWithNullLocation() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        favoritesView.openForecastDailyView(null);
        // No assertion needed as we are testing for no crash or error
    }

    @Test
    public void testOpenForecastDailyViewWithNullParentWindow() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);

        favoritesView.openForecastDailyView(location);
        // No assertion needed as we are testing for no crash or error
    }

    @Test
    public void testConstructorInitialization() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        assertNotNull(favoritesView.getFavoritesList(), "Favorites list should be initialized.");
        assertNotNull(favoritesView.getRemoveButton(), "Remove button should be initialized.");
    }

    @Test
    public void testRemoveFavoriteByNameInvalidName() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);

        favoritesView.removeFavoriteByName("Invalid Location");
        assertTrue(favoritesManager.getFavorites().contains(location), "Favorites list should remain unchanged.");
    }

    @Test
    public void testFavoritesListListener() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);
        favoritesView.refreshFavoritesList();

        favoritesView.getFavoritesList().setSelectedValue(location.fullLocationName(), false);
        favoritesView.getFavoritesList().dispatchEvent(new java.awt.event.MouseEvent(
                favoritesView.getFavoritesList(), java.awt.event.MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, 2, false
        ));

        // Manually verify behavior or mock parentWindow for verification.
    }

    @Test
    public void testGetFavoritesAsStringsWithNullLocations() {
        List<Location> mockFavorites = new ArrayList<>();
        mockFavorites.add(null);

        FavoritesManager mockManager = mock(FavoritesManager.class);
        Mockito.when(mockManager.getFavorites()).thenReturn(mockFavorites);

        FavoritesView favoritesView = new FavoritesView(mockManager, "dummyApiKey", null);
        DefaultListModel<String> model = favoritesView.getFavoritesAsStrings();

        assertEquals(1, model.getSize(), "Favorites list should display one placeholder item.");
        assertEquals("No favorites added yet.", model.getElementAt(0), "Placeholder message should be displayed.");
    }

    @Test
    public void testRemoveFavoriteFromEmptyList() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        favoritesView.removeFavoriteByName("Nonexistent Location");

        assertTrue(favoritesManager.getFavorites().isEmpty(), "Favorites list should remain empty.");
    }

    @Test
    public void testGetLocationFromNameWithInvalidName() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        Location result = favoritesView.getLocationFromName("Invalid Location Name");
        assertNull(result, "Result should be null when the location is not found.");
    }
    @Test
    public void testHandleRemoveFavoriteWithoutSelection() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        favoritesView.getRemoveButton().doClick();

        assertTrue(favoritesManager.getFavorites().isEmpty(),
                "Favorites list should remain unchanged when no selection is made.");
    }
    @Test
    public void testFavoritesViewHandlesNullFavoritesGracefully() {
        FavoritesManager mockManager = mock(FavoritesManager.class);
        Mockito.when(mockManager.getFavorites()).thenReturn(null);

        FavoritesView favoritesView = new FavoritesView(mockManager, "dummyApiKey", null);

        DefaultListModel<String> model = favoritesView.getFavoritesAsStrings();

        assertEquals(1, model.getSize(), "List model should contain one placeholder item.");
        assertEquals("No favorites added yet.", model.getElementAt(0),
                "Placeholder message should be displayed when favorites are null.");
    }
    @Test
    public void testFavoritesListInitialEmptyState() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        assertEquals("No favorites added yet.",
                favoritesView.getFavoritesList().getModel().getElementAt(0),
                "Placeholder message should be displayed for an empty favorites list.");
    }

    @Test
    public void testHandleFavoritesListDoubleClickWithNoSelection() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        // No selection
        favoritesView.getFavoritesList().setSelectedValue(null, false);
        favoritesView.getFavoritesList().dispatchEvent(new java.awt.event.MouseEvent(
                favoritesView.getFavoritesList(), java.awt.event.MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, 2, false
        ));

        // Expect no crash or exceptions
    }

    @Test
    public void testRemoveFavoriteByNameHandlesEmptyList() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        favoritesView.removeFavoriteByName("Nonexistent Location");

        assertTrue(favoritesManager.getFavorites().isEmpty(),
                "Favorites list should remain empty after attempting to remove a location from an empty list.");
    }

    @Test
    public void testShowErrorWhenRemovingFromEmptyList() {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, "dummyApiKey", null);

        favoritesView.removeFavoriteByName("Nonexistent Location");
        // Verify an error dialog pops up (manual or Mockito verification of JOptionPane can be done)
        // No assertion needed as long as no exceptions occur
    }
}
