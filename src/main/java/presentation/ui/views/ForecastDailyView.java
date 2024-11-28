package presentation.ui.views;

import presentation.ui.windows.LocationsWindow;

/**
 * Class that handles UI to display the daily forecast (up to 16 days) showing max and min
 * temperature, percipitation sum, and wind speed (with dominant wind direction).
 */
public class ForecastDailyView extends LocationsWindow {
    public ForecastDailyView(String name) {
        super(name);
    }
}
