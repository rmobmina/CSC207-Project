package presentation.ui.windows;

import presentation.ui.views.*;

/**
 * A factory that generates a new LocationsWindow based on the chosen option and number of locations the user wants
 */
public class LocationsWindowGenerator {

    public static LocationsWindow generateLocationsWindow(String option, int width, int height, int numOfLocations) {
        if (numOfLocations < 1) {
            return new ErrorLocationsWindow("ERROR: Invalid number of locations!", width, height);
        }
        // Generates a new window that display an error if no option is chosen
        switch(option){
            case ForecastDailyView.OPTION_NAME:
                return new ForecastDailyView("Forecast Daily View", width, height);
            case ForecastHourlyView.OPTION_NAME:
                return new ForecastHourlyView("Forecast Hourly View", width, height);
            case HistoricalWeatherView.OPTION_NAME:
                return new HistoricalWeatherView("Historical Weather View", width, height);
            case HistoricalWeatherComparisonView.OPTION_NAME:
                return new HistoricalWeatherComparisonView("Weather Comparison View", width, height, numOfLocations);
            case WeatherAlertView.OPTION_NAME:
                return new WeatherAlertView("Weather Alert View", width, height);
            case MercatorMapView.OPTION_NAME:
                return new MercatorMapView("Mercator Map View", width, height, numOfLocations);
            default:
                return new ErrorLocationsWindow("ERROR: Invalid option chosen!", width, height);
        }
    }
}
