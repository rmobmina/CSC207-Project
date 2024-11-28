package presentation.ui.views;

import presentation.ui.windows.LocationsWindow;

/**
 * Class that handles UI to display the historical temperature and climate trends over a certain period of time
 * of a given location. (Alternative: HistoricalWeatherComparisonView)
 */
public class HistoricalWeatherView extends LocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Historical";

    public HistoricalWeatherView(String name) {
        super(name);
    }
}
