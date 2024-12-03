//package application.usecases;
//
//import application.usecases.ManageFavoritesUseCase;
//import domain.entities.Location;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import presentation.ui.FavoritesManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//Mockito
//class ManageFavoritesUseCaseTest {
//
//    @Mock
//    private FavoritesManager mockFavoritesManager;
//
//    private ManageFavoritesUseCase manageFavoritesUseCase;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        manageFavoritesUseCase = new ManageFavoritesUseCase(mockFavoritesManager);
//    }
//
//    @Test
//    void testAddFavorite_Success() {
//        Location location = new Location("Toronto", "ON", "Canada", 43.6532, 80);
//        List<Location> favorites = new ArrayList<>();
//        when(mockFavoritesManager.getFavorites()).thenReturn(favorites);
//
//        boolean result = manageFavoritesUseCase.addFavorite(location);
//
//        assertTrue(result);
//        verify(mockFavoritesManager, times(1)).addFavorite(location);
//    }
//
//    @Test
//    void testAddFavorite_Failure() {
//        Location location = new Location("Toronto", "ON", "Canada", 43.6532, -79.3832);
//        List<Location> favorites = new ArrayList<>();
//        favorites.add(location);
//        when(mockFavoritesManager.getFavorites()).thenReturn(favorites);
//
//        boolean result = manageFavoritesUseCase.addFavorite(location);
//
//        assertFalse(result);
//        verify(mockFavoritesManager, never()).addFavorite(location);
//    }
//
//    @Test
//    void testRemoveFavorite_Success() {
//        Location location = new Location("Toronto", "ON", "Canada", 43.6532, -79.3832);
//        List<Location> favorites = new ArrayList<>();
//        favorites.add(location);
//        when(mockFavoritesManager.getFavorites()).thenReturn(favorites);
//
//        boolean result = manageFavoritesUseCase.removeFavorite(location);
//
//        assertTrue(result);
//        verify(mockFavoritesManager, times(1)).removeFavorite(location);
//    }
//
//    @Test
//    void testRemoveFavorite_Failure() {
//        Location location = new Location("Toronto", "ON", "Canada", 43.6532, -79.3832);
//        List<Location> favorites = new ArrayList<>();
//        when(mockFavoritesManager.getFavorites()).thenReturn(favorites);
//
//        boolean result = manageFavoritesUseCase.removeFavorite(location);
//
//        assertFalse(result);
//        verify(mockFavoritesManager, never()).removeFavorite(location);
//    }
//
//    @Test
//    void testGetFavorites() {
//        List<Location> favorites = new ArrayList<>();
//        favorites.add(new Location("Toronto", "ON", "Canada", 43.6532, -79.3832));
//        when(mockFavoritesManager.getFavorites()).thenReturn(favorites);
//
//        List<Location> result = manageFavoritesUseCase.getFavorites();
//
//        assertEquals(favorites, result);
//        verify(mockFavoritesManager, times(1)).getFavorites();
//    }
//
//    @Test
//    void testSaveFavorites() {
//        manageFavoritesUseCase.saveFavorites();
//
//        verify(mockFavoritesManager, times(1)).saveFavorites();
//    }
//
//    @Test
//    void testLoadFavorites() {
//        manageFavoritesUseCase.loadFavorites();
//
//        verify(mockFavoritesManager, times(1)).loadFavorites();
//    }
//}
