package presentation.ui.views;

import presentation.ui.windows.MultipleLocationsWindow;

public class HistoricalWeatherComparisonView extends MultipleLocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Historical Comparison";

    public HistoricalWeatherComparisonView(String name, int numOfLocations) {
        super(name, numOfLocations);
    }
}
