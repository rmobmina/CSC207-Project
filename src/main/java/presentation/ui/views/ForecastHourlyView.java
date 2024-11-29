package presentation.ui.views;

import presentation.ui.windows.LocationsWindow;

/**
 * Class that handles UI to display the hourly forcast to how exact
 * temperatures and humidities each hour of certain day
 */
public class ForecastHourlyView extends LocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Forecast Hourly";

    public ForecastHourlyView(String name, int width, int height) {
        super(name, width, height);
    }
}
