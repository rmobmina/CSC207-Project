package presentation.ui.windows;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.interfaces.ApiService;
import presentation.ui.dashboard.NewDashBoardUi;
import presentation.ui.views.*;

/**
 * A factory that generates a new LocationsWindow based on the chosen option and the number of locations the user wants.
 */
public class LocationsWindowGenerator {


    /**
     * Generates a LocationsWindow based on the option selected and other dependencies.
     *
     * @param option               The option selected by the user (e.g., "Forecast Daily").
     * @param dimensions           The dimensions of the window.
     * @param numOfLocations       The number of locations the user wants to use.
     * @param locationDataUseCase  The use case to retrieve location data.
     * @param apiKey               The API key for the weather service.
     * @param apiService           The API service to fetch weather data.
     * @return The corresponding LocationsWindow for the selected option.
     */
    public static LocationsWindow generateLocationsWindow(String option, int[] dimensions, int numOfLocations,
                                                          GetLocationDataUseCase locationDataUseCase, String apiKey,
                                                      ApiService apiService, NewDashBoardUi dashboard) {
        final ErrorLocationsWindow errorWindow = new ErrorLocationsWindow(
                "ERROR: Invalid number of locations!", dimensions, locationDataUseCase, apiKey, apiService);

        // Check for invalid number of locations
        if (numOfLocations < 1) {
            return errorWindow;
        }

        // Handle the selected option and generate the appropriate window
        switch (option) {
            case ForecastDailyView.OPTION_NAME: {
                // Create a default Location for ForecastDailyView
                Location defaultLocation = createDefaultLocation(); // Uses one of the Location constructors
                GetForecastWeatherDataUseCase forecastWeatherDataUseCase = new GetForecastWeatherDataUseCase(apiService);

                // Return ForecastDailyView with all required parameters
                return new ForecastDailyView("Forecast Daily View", dimensions, defaultLocation,
                        forecastWeatherDataUseCase, locationDataUseCase, apiKey, apiService);
            }
            case ForecastHourlyView.OPTION_NAME:
                return new ForecastHourlyView("Forecast Hourly View", dimensions, locationDataUseCase, apiKey, apiService);
            case HistoricalWeatherComparisonView.OPTION_NAME:
            return new HistoricalWeatherComparisonView("Weather Comparison View", dimensions, numOfLocations, locationDataUseCase, apiKey, apiService, dashboard);
            case MercatorMapView.OPTION_NAME:
                return new MercatorMapView("Mercator Map View", dimensions, numOfLocations,
                        locationDataUseCase, apiKey, apiService);
            default:
                // Return an error window for invalid options
                return new ErrorLocationsWindow("ERROR: Invalid option chosen!", dimensions, locationDataUseCase, apiKey, apiService);
        }
    }

    /**
     * Creates a default Location object using the available constructor.
     *
     * @return A default Location object for testing or fallback purposes.
     */
    private static Location createDefaultLocation() {
        // Example: Replace with actual default values or dynamic input
        String city = "Toronto";
        String state = "Ontario";
        String country = "Canada";
        double latitude = 43.65107;
        double longitude = -79.347015;

        // Use the full constructor of the Location class
        return new Location(city, state, country, latitude, longitude);
    }
}
