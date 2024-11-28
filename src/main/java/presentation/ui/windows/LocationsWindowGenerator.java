package presentation.ui.windows;

import presentation.ui.views.ForecastHourlyView;

// Needs to be reworked (by Akram)
public class LocationsWindowGenerator {
    public static LocationsWindow generateLocationsWindow(String type, int numOfLocations) {
        // Generates a new window that display an error if no valid locations were chosen
        if (numOfLocations == 1) {
            return new ForecastHourlyView("Forecast Hourly"); // Just for testing
        }
        // Generates a new window for multiple locations if there are atleast 2 valid locations chosen
        else if (numOfLocations > 1) {
            return null; // To be changed later ...
        }
        else {
            return new ErrorLocationsWindow("Error Locations Window");
        }
    }
}
