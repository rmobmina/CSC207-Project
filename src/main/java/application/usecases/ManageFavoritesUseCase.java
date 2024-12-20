package application.usecases;

import java.util.List;

import domain.entities.Location;
import presentation.ui.windows.FavoritesManager;
import utils.UseCaseInteractor;

/**
 * Use case for managing favorites (add, remove, retrieve, and save).
 */
public class ManageFavoritesUseCase extends UseCaseInteractor {

    private final FavoritesManager favoritesManager;

    /**
     * Constructor to initialize the FavoritesManager.
     *
     * @param favoritesManager The manager responsible for handling favorites.
     */
    public ManageFavoritesUseCase(FavoritesManager favoritesManager) {
        this.favoritesManager = favoritesManager;
    }

    /**
     * Adds a location to favorites if it doesn't already exist.
     *
     * @param location The location to add.
     * @return true if the location was added, false if it already exists or is null.
     */
    public boolean addFavorite(Location location) {
        boolean isAdded = false;

        if (location != null && !favoritesManager.getFavorites().contains(location)) {
            favoritesManager.addFavorite(location);
            isAdded = true;
        }

        return isAdded;
    }

    /**
     * Removes a location from favorites.
     *
     * @param location The location to remove.
     * @return true if the location was removed, false if it was not found.
     */
    public boolean removeFavorite(Location location) {
        boolean isRemoved = false;
        final List<Location> favorites = favoritesManager.getFavorites();

        if (favorites != null && favorites.contains(location)) {
            favoritesManager.removeFavorite(location);
            isRemoved = true;
        }

        return isRemoved;
    }

    /**
     * Retrieves the current list of favorites.
     *
     * @return A list of favorite locations.
     */
    public List<Location> getFavorites() {
        return favoritesManager.getFavorites();
    }

    /**
     * Saves the current list of favorites to persistent storage.
     */
    public void saveFavorites() {
        favoritesManager.saveFavorites();
    }

    /**
     * Reloads the favorites from persistent storage.
     */
    public void loadFavorites() {
        favoritesManager.loadFavorites();
    }

}
