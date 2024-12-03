//package application.usecases;
//
//import domain.entities.Location;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import presentation.ui.windows.FavoritesManager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
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
//        Location location = new Location("Toronto", "ON", "Canada", 43.6532, -79.3832);
//        when(mockFavoritesManager.getFavorites()).thenReturn(new ArrayList<>());
//
//        boolean result = manageFavoritesUseCase.addFavorite(location);
//
//        assertTrue(result);
//        verify(mockFavoritesManager, times(1)).addFavorite(location);
//    }
//
//    @Test
//    void testAddFavorite_Failure_Duplicate() {
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
//    void testAddFavorite_NullLocation() {
//        // Call addFavorite with null and ensure it doesn't throw exceptions
//        boolean result = manageFavoritesUseCase.addFavorite(null);
//        assertFalse(result);
//
//        // Verify that no interaction occurred with the manager
//        verify(mockFavoritesManager, never()).addFavorite(any());
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
//    void testRemoveFavorite_Failure_NotInFavorites() {
//        Location location = new Location("Toronto", "ON", "Canada", 43.6532, -79.3832);
//        when(mockFavoritesManager.getFavorites()).thenReturn(new ArrayList<>());
//
//        boolean result = manageFavoritesUseCase.removeFavorite(location);
//
//        assertFalse(result);
//        verify(mockFavoritesManager, never()).removeFavorite(location);
//    }
//
//    @Test
//    void testRemoveFavorite_NullLocation() {
//        // Call removeFavorite with null and ensure it doesn't throw exceptions
//        boolean result = manageFavoritesUseCase.removeFavorite(null);
//        assertFalse(result);
//
//        // Verify that no interaction occurred with the manager
//        verify(mockFavoritesManager, never()).removeFavorite(any());
//    }
//
//    @Test
//    void testRemoveFavorite_FavoritesNull() {
//        when(mockFavoritesManager.getFavorites()).thenReturn(null);
//
//        boolean result = manageFavoritesUseCase.removeFavorite(new Location("Toronto", "ON",
//                "Canada", 43.6532, -79.3832));
//
//        assertFalse(result); // Ensure the method gracefully handles the null list
//        verify(mockFavoritesManager, never()).removeFavorite(any(Location.class));
//    }
//
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
//    void testGetFavorites_NullList() {
//        when(mockFavoritesManager.getFavorites()).thenReturn(null);
//
//        List<Location> result = manageFavoritesUseCase.getFavorites();
//
//        assertNull(result);
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