package presentation.ui.windows;

import presentation.ui.views.*;

// Needs to be reworked (by Akram)
public class LocationsWindowGenerator {

    public static LocationsWindow generateLocationsWindow(String option, int numOfLocations) {
        // Generates a new window that display an error if no option is chosen
        switch(option){
            case ForecastDailyView.OPTION_NAME:
                return new ForecastDailyView("Forecast Daily View");
            case ForecastHourlyView.OPTION_NAME:
                return new ForecastHourlyView("Forecast Hourly View");
            case HistoricalWeatherView.OPTION_NAME:
                return new HistoricalWeatherView("Historical Weather View");
            case HistoricalWeatherComparisonView.OPTION_NAME:
                return new HistoricalWeatherComparisonView("Weather Comparison View", numOfLocations);
            case WeatherAlertView.OPTION_NAME:
                return new WeatherAlertView("Weather Alert View");
            case MercatorMapView.OPTION_NAME:
                return new MercatorMapView("Mercator Map View", numOfLocations);
            default:
                return new ErrorLocationsWindow("Error Locations Window");
        }
    }
}
