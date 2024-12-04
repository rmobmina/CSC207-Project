package presentation.ui.windows;

import application.usecases.GetLocationDataUseCase;

import domain.interfaces.ApiService;

/**
 * The `MultipleLocationsWindow` class extends the `LocationsWindow` class and provides
 * functionality for handling weather data for multiple locations. This class is designed
 * to support features like historical weather comparison by integrating dropdowns for
 * selecting two cities.
 */
public class MultipleLocationsWindow extends LocationsWindow {

    /**
     * Constructs a `MultipleLocationsWindow` instance.
     *
     * @param name                The name (title) of the window.
     * @param dimensions          The dimensions of the window (width and height).
     * @param numOfLocations      The number of locations this window supports (e.g., for comparison).
     * @param locationDataUseCase The use case for retrieving location data.
     * @param apiKey              The API key for accessing weather data services.
     * @param apiService          The API service for fetching weather data.
     */
    public MultipleLocationsWindow(String name, int[] dimensions, int numOfLocations,
                                   GetLocationDataUseCase locationDataUseCase, String apiKey,
                                   ApiService apiService) {
        super(name, dimensions, locationDataUseCase, apiKey, apiService);
        this.visualizationUI = new VisualizationUI(2, getMainPanel());
        this.setVisible(true);
    }

    /**
     * Opens a visualization for the selected weather data.
     * This method should be implemented to provide visualization functionality.
     */

    @Override
    protected void openVisualization() {

    }

    /**
     * Retrieves weather data for the selected locations.
     * This method should be implemented to fetch and process data for multiple locations.
     */
    @Override
    protected void getWeatherData() {

    }

    /**
     * Displays weather data for the selected locations.
     * This method should be implemented to present data for multiple locations in the UI.
     */
    @Override
    protected void displayWeatherData() {

    }
}
