package presentation.ui.views;

import domain.entities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import presentation.ui.windows.FavoritesManager;
import presentation.ui.windows.LocationsWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FavoritesViewTest {
    @Mock
    private FavoritesManager favoritesManager;


    @BeforeEach
    public void setUp() {
        favoritesManager = new FavoritesManager();
        favoritesManager.clearFavorites();
    }

    @Test
    public void testAddAndRemoveFavoritesFromView() {
        FavoritesManager favoritesManager = new FavoritesManager();
        favoritesManager.clearFavorites();

        String dummyApiKey = "test-api-key";

        // Implementing abstract methods in the anonymous class
        LocationsWindow dummyWindow = new LocationsWindow(
                "Dummy Window", new int[]{500, 500}, null, dummyApiKey, null
        ) {
            @Override
            protected void getWeatherData() {
                // Mock implementation for testing
            }

            @Override
            protected void displayWeatherData() {
                // Mock implementation for testing
            }

            @Override
            protected void openVisualization() {
                // Mock implementation for testing
            }
        };

        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyWindow);

        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);

        assert favoritesManager.getFavorites().contains(location) : "Favorites should contain Paris";

        favoritesManager.removeFavorite(location);

        assert !favoritesManager.getFavorites().contains(location) : "Favorites should no longer contain Paris";
    }



    @Test
    public void testFavoritesViewLaunch() {
        String dummyApiKey = "";

        LocationsWindow dummyLocationsWindow = new LocationsWindow(
                "Dummy Window",
                new int[]{500, 500},
                null,
                dummyApiKey,
                null
        ) {
            @Override
            protected void openVisualization() {

            }

            @Override
            protected void getWeatherData() {
            }

            @Override
            protected void displayWeatherData() {
            }
        };


        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyLocationsWindow);
        assertNotNull(favoritesView, "FavoritesView should be initialized.");
        favoritesView.setVisible(true);

    }

    @Test
    public void testEmptyFavoritesList() {
        String dummyApiKey = "test-api-key";
        LocationsWindow dummyWindow = new LocationsWindow(
                "Dummy Window", new int[]{500, 500}, null, dummyApiKey, null
        ) {
            @Override
            protected void openVisualization() {

            }

            @Override
            protected void getWeatherData() {
            }

            @Override
            protected void displayWeatherData() {
            }
        };

        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyWindow);
        assertTrue(favoritesManager.getFavorites().isEmpty(), "Favorites list should be empty initially.");

        String placeholderMessage = "No favorites added yet.";
        assertEquals(placeholderMessage, favoritesView.getFavoritesList().getModel().getElementAt(0),
                "Placeholder message should be displayed for an empty favorites list.");
    }


    @Test
    public void testAddFavoriteToView() {
        String dummyApiKey = "test-api-key";
        LocationsWindow dummyWindow = new LocationsWindow(
                "Dummy Window", new int[]{500, 500}, null, dummyApiKey, null
        ) {
            @Override
            protected void openVisualization() {

            }

            @Override
            protected void getWeatherData() {
            }

            @Override
            protected void displayWeatherData() {
            }
        };

        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyWindow);

        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);

        favoritesView.refreshFavoritesList();

        assertEquals(location.fullLocationName(), favoritesView.getFavoritesList().getModel().getElementAt(0),
                "FavoritesView should display the added location.");
    }


    @Test
    public void testRemoveFavoriteFromView() {
        String dummyApiKey = "test-api-key";
        LocationsWindow dummyWindow = new LocationsWindow(
                "Dummy Window", new int[]{500, 500}, null, dummyApiKey, null
        ) {
            @Override
            protected void openVisualization() {

            }
            @Override
            protected void getWeatherData() {
            }

            @Override
            protected void displayWeatherData() {
            }
        };

        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyWindow);
        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);
        favoritesManager.removeFavorite(location);

        assertFalse(favoritesManager.getFavorites().contains(location), "Favorites list should not contain the removed location.");
        assertEquals("No favorites added yet.", favoritesView.getFavoritesList().getModel().getElementAt(0),
                "Placeholder message should be displayed after removing all favorites.");
    }


    @Test
    public void testDoubleClickFavoriteInteraction() {
        String dummyApiKey = "test-api-key";

        LocationsWindow dummyWindow = new LocationsWindow(
                "Dummy Window", new int[]{500, 500}, null, dummyApiKey, null
        ) {
            @Override
            protected void openVisualization() {

            }
            @Override
            protected void getWeatherData() {
                // Empty implementation for testing purposes
            }

            @Override
            protected void displayWeatherData() {
                // Empty implementation for testing purposes
            }

            @Override
            public void setSearchBarText(String text) {
                // Mock behavior for setSearchBarText
            }

            @Override
            public void setWeatherLocation(Location location) {
                assertEquals("Paris, Ile-de-France, France", location.fullLocationName(),
                        "Parent window should update with the selected location.");
            }

        };

        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyWindow);

        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);

        favoritesView.getFavoritesList().setSelectedIndex(0);
        favoritesView.getFavoritesList().dispatchEvent(new java.awt.event.MouseEvent(
                favoritesView.getFavoritesList(), java.awt.event.MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, 2, false
        ));
    }

    @Test
    public void testRemoveNonexistentFavorite() {
        String dummyApiKey = "test-api-key";
        LocationsWindow dummyWindow = new LocationsWindow(
                "Dummy Window", new int[]{500, 500}, null, dummyApiKey, null
        ) {
            @Override
            protected void openVisualization() {

            }
            @Override
            protected void getWeatherData() {
            }

            @Override
            protected void displayWeatherData() {
            }
        };

        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyWindow);
        favoritesView.removeFavoriteByName("Nonexistent Location");

        assertTrue(favoritesManager.getFavorites().isEmpty(),
                "Favorites list should remain empty after trying to remove a nonexistent favorite.");
    }

    @Test
    public void testRemoveFavoriteWithNullList() {
        String dummyApiKey = "test-api-key";
        LocationsWindow dummyWindow = new LocationsWindow(
                "Dummy Window", new int[]{500, 500}, null, dummyApiKey, null
        ) {
            @Override
            protected void openVisualization() {

            }
            @Override
            protected void getWeatherData() {
            }

            @Override
            protected void displayWeatherData() {
            }
        };

        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyWindow);

        try {
            Field favoritesField = FavoritesManager.class.getDeclaredField("favorites");
            favoritesField.setAccessible(true);
            favoritesField.set(favoritesManager, null);


            favoritesView.removeFavoriteByName("Nonexistent Location");


        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set up test: " + e.getMessage());
        }
    }


    @Test
    public void testGetFavoritesAsStringsEmpty() {
        String dummyApiKey = "test-api-key";
        LocationsWindow dummyWindow = new LocationsWindow(
                "Dummy Window", new int[]{500, 500}, null, dummyApiKey, null
        ) {
            @Override
            protected void openVisualization() {

            }
            @Override
            protected void getWeatherData() {
            }

            @Override
            protected void displayWeatherData() {
            }
        };

        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyWindow);

        DefaultListModel<String> model = favoritesView.getFavoritesAsStrings();
        assertEquals(1, model.getSize(), "List model should contain one placeholder item.");
        assertEquals("No favorites added yet.", model.getElementAt(0),
                "Placeholder message should be displayed.");
    }

    @Test
    public void testRemoveFavoriteButton() {
        String dummyApiKey = "test-api-key";
        LocationsWindow dummyWindow = new LocationsWindow(
                "Dummy Window", new int[]{500, 500}, null, dummyApiKey, null
        ) {
            @Override
            protected void openVisualization() {

            }
            @Override
            protected void getWeatherData() {
            }

            @Override
            protected void displayWeatherData() {
            }
        };

        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyWindow);


        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);
        favoritesView.refreshFavoritesList();


        favoritesView.getFavoritesList().setSelectedIndex(0);


        for (ActionListener al : favoritesView.getRemoveButton().getActionListeners()) {
            al.actionPerformed(new ActionEvent(favoritesView.getRemoveButton(), ActionEvent.ACTION_PERFORMED, ""));
        }


        assertTrue(favoritesManager.getFavorites().isEmpty(), "Favorites list should be empty after removing the favorite.");
    }


    @Test
    public void testDoubleClickWithNullParentWindow() {
        String dummyApiKey = "test-api-key";
        FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);

        Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
        favoritesManager.addFavorite(location);

        favoritesView.getFavoritesList().setSelectedIndex(0);
        favoritesView.getFavoritesList().dispatchEvent(new java.awt.event.MouseEvent(
                favoritesView.getFavoritesList(), java.awt.event.MouseEvent.MOUSE_CLICKED, 0,
                0, 0, 0, 2, false
        ));

    }

    @Test
public void testDoubleClickWithoutSelection() {
    String dummyApiKey = "test-api-key";
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);


    favoritesView.getFavoritesList().dispatchEvent(new java.awt.event.MouseEvent(
            favoritesView.getFavoritesList(), java.awt.event.MouseEvent.MOUSE_CLICKED, 0,
            0, 0, 0, 2, false
    ));
    }



    @Test
public void testRemoveFavoriteWithoutSelection() {
    String dummyApiKey = "test-api-key";
    LocationsWindow dummyWindow = new LocationsWindow(
            "Dummy Window", new int[]{500, 500}, null, dummyApiKey, null
    ) {
        @Override
        protected void openVisualization() {

        }
        @Override
        protected void getWeatherData() {}
        @Override
        protected void displayWeatherData() {}
    };

    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyWindow);


    for (ActionListener al : favoritesView.getRemoveButton().getActionListeners()) {
        al.actionPerformed(new ActionEvent(favoritesView.getRemoveButton(), ActionEvent.ACTION_PERFORMED, ""));
    }

}
@Test
public void testGetFavoritesWithNullManager() {
    String dummyApiKey = "test-api-key";
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);

    try {

        Field favoritesField = FavoritesManager.class.getDeclaredField("favorites");
        favoritesField.setAccessible(true);
        favoritesField.set(favoritesManager, null);


        favoritesView.refreshFavoritesList();

        assertEquals("No favorites added yet.",
                favoritesView.getFavoritesList().getModel().getElementAt(0),
                "UI should display placeholder message when no favorites exist.");
    } catch (NoSuchFieldException | IllegalAccessException e) {
        fail("Reflection setup failed: " + e.getMessage());
    }
}

@Test
public void testHandleFavoritesListDoubleClickWithInvalidLocation() {
    String dummyApiKey = "test-api-key";
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);


    Location validLocation = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
    favoritesManager.addFavorite(validLocation);


    favoritesView.getFavoritesList().setSelectedIndex(0);
    favoritesView.getFavoritesList().dispatchEvent(new java.awt.event.MouseEvent(
        favoritesView.getFavoritesList(), java.awt.event.MouseEvent.MOUSE_CLICKED, 0,
        0, 0, 0, 2, false
    ));


}



@Test
public void testRemoveFavoriteNotInFavorites() {
    String dummyApiKey = "test-api-key";
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);

    Location validLocation = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
    favoritesManager.addFavorite(validLocation);

    favoritesView.removeFavoriteByName("London");


}





@Test
public void testHandleFavoritesListDoubleClickWithNullLocation() {
    String dummyApiKey = "test-api-key";

    LocationsWindow dummyWindow = new LocationsWindow(
        "Dummy Window", new int[]{500, 500}, null, dummyApiKey, null
    ) {
        @Override
        protected void openVisualization() {

        }
        @Override
        protected void getWeatherData() {}

        @Override
        protected void displayWeatherData() {}
    };

    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, dummyWindow);


    favoritesManager.addFavorite(null);


    favoritesView.getFavoritesList().setSelectedValue(null, false);
    favoritesView.getFavoritesList().dispatchEvent(new java.awt.event.MouseEvent(
        favoritesView.getFavoritesList(), java.awt.event.MouseEvent.MOUSE_CLICKED, 0,
        0, 0, 0, 2, false
    ));


}




@Test
public void testHandleFavoritesListDoubleClickValidLocation() {
    String dummyApiKey = "test-api-key";


    LocationsWindow mockWindow = Mockito.mock(LocationsWindow.class);
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, mockWindow);


    Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
    favoritesManager.addFavorite(location);
    favoritesView.refreshFavoritesList();


    favoritesView.getFavoritesList().setSelectedValue("Paris, Ile-de-France, France", false);


    favoritesView.getFavoritesList().dispatchEvent(new java.awt.event.MouseEvent(
        favoritesView.getFavoritesList(), java.awt.event.MouseEvent.MOUSE_CLICKED, 0,
        0, 0, 0, 2, false
    ));


    Mockito.verify(mockWindow).setSearchBarText("Paris, Ile-de-France, France");
}

@Test
public void testGetFavoritesAsStringsNullFavorites() {
    String dummyApiKey = "test-api-key";


    FavoritesManager favoritesManager = Mockito.mock(FavoritesManager.class);
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);


    Mockito.when(favoritesManager.getFavorites()).thenReturn(null);


    DefaultListModel<String> result = favoritesView.getFavoritesAsStrings();
    assertEquals(1, result.getSize());
    assertEquals("No favorites added yet.", result.getElementAt(0));
}







@Test
public void testGetLocationFromNameNullFavorites() {
    String dummyApiKey = "test-api-key";
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);

    Mockito.when(favoritesManager.getFavorites()).thenReturn(null);

    Location result = favoritesView.getLocationFromName("Paris");
    assertNull(result);
}

@Test
public void testGetLocationFromNameEmptyFavorites() {
    String dummyApiKey = "test-api-key";
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);

    favoritesManager.clearFavorites();

    Location result = favoritesView.getLocationFromName("Paris");
    assertNull(result);
}

@Test
public void testGetLocationFromNameNotFound() {
    String dummyApiKey = "test-api-key";


    FavoritesManager favoritesManager = Mockito.mock(FavoritesManager.class);
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);

    List<Location> favorites = new ArrayList<>();
    favorites.add(new Location("London", "England", "UK", 51.5074, -0.1278));
    Mockito.when(favoritesManager.getFavorites()).thenReturn(favorites);

    Location result = favoritesView.getLocationFromName("Paris");
    assertNull(result, "Result should be null when location is not found in the favorites.");
}


@Test
public void testGetLocationFromNameFound() {
    String dummyApiKey = "test-api-key";


    FavoritesManager favoritesManager = Mockito.mock(FavoritesManager.class);
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);

    List<Location> favorites = new ArrayList<>();
    Location paris = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
    favorites.add(paris);
    Mockito.when(favoritesManager.getFavorites()).thenReturn(favorites);


    Location result = favoritesView.getLocationFromName("Paris, Ile-de-France, France");
    assertNotNull(result, "Result should not be null when location is found.");
    assertEquals(paris, result, "Result should match the location object.");
}


@Test
public void testOpenForecastDailyViewNullLocation() {
    String dummyApiKey = "test-api-key";
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);

    favoritesView.openForecastDailyView(null);

}

@Test
public void testOpenForecastDailyViewNullParentWindow() {
    String dummyApiKey = "test-api-key";
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);

    Location location = new Location("Paris", "Ile-de-France", "France", 48.8566, 2.3522);
    favoritesView.openForecastDailyView(location);

}



@Test
public void testGetLocationFromNameFavoritesNull() {
    String dummyApiKey = "test-api-key";

    FavoritesManager favoritesManager = Mockito.mock(FavoritesManager.class);
    FavoritesView favoritesView = new FavoritesView(favoritesManager, dummyApiKey, null);

    Mockito.when(favoritesManager.getFavorites()).thenReturn(null);

    Location result = favoritesView.getLocationFromName("Paris");
    assertNull(result, "Result should be null when favorites list is null.");
}

}
