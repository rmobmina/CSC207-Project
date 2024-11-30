package presentation.ui.windows;

import application.usecases.GetLocationDataUseCase;
import domain.interfaces.ApiService;
import presentation.ui.views.*;

/**
 * A factory that generates a new LocationsWindow based on the chosen option and number of locations the user wants
 */
public class LocationsWindowGenerator {
    public static LocationsWindow generateLocationsWindow(String option, int[] dimensions, int numOfLocations,
                                                          GetLocationDataUseCase locationDataUseCase, String apiKey,
                                                          ApiService apiService) {
        ErrorLocationsWindow errorWindow = new ErrorLocationsWindow("ERROR: Invalid number of locations!", dimensions, locationDataUseCase, apiKey, apiService);
        if (numOfLocations < 1) {
            return errorWindow;
        }
        // Generates a new window that display an error if no option is chosen
        switch(option){
            case ForecastDailyView.OPTION_NAME:
                return new ForecastDailyView("Forecast Daily View", dimensions, locationDataUseCase, apiKey, apiService);
            case ForecastHourlyView.OPTION_NAME:
                return new ForecastHourlyView("Forecast Hourly View", dimensions, locationDataUseCase, apiKey, apiService);
            case HistoricalWeatherView.OPTION_NAME:
                return new HistoricalWeatherView("Historical Weather View", dimensions, locationDataUseCase, apiKey, apiService);
            case HistoricalWeatherComparisonView.OPTION_NAME:
                return new HistoricalWeatherComparisonView("Weather Comparison View", dimensions, numOfLocations, locationDataUseCase, apiKey, apiService);
            case MercatorMapView.OPTION_NAME:
                return new MercatorMapView("Mercator Map View", dimensions, numOfLocations, locationDataUseCase, apiKey, apiService);
            default:
                return new ErrorLocationsWindow("ERROR: Invalid option chosen!", dimensions, locationDataUseCase, apiKey, apiService);
        }
    }
}
