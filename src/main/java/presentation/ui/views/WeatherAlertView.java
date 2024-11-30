package presentation.ui.views;

import presentation.ui.windows.LocationsWindow;

/**
 * Class that handles UI to display weather alerts for storms and other weather hazards
 * and provides details for dealing with these alerts.
 */
public class WeatherAlertView extends LocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Weather Alert";

    public WeatherAlertView(String name, int width, int height) {
        super(name, width, height);
    }
}