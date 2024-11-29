package presentation.ui.views;

import presentation.ui.windows.LocationsWindow;

/**
 * Class that handles UI to display the daily forecast (up to 16 days) showing max and min
 * temperature, percipitation sum, and wind speed (with dominant wind direction).
 */
public class ForecastDailyView extends LocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Forecast Daily";

    public ForecastDailyView(String name, int width, int height) {
        super(name, width, height);
        panel.setVisible(true);
    }
}
