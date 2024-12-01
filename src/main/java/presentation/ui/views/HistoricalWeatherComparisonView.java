package presentation.ui.views;

import application.usecases.GetLocationDataUseCase;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.windows.MultipleLocationsWindow;

import javax.swing.*;


/**
 * A class that handles the UI for comparing multiple cities
 * based on temperature trends and other weather data over a certain range of time (Alternative: HistoricalWeatherData)
 */
public class HistoricalWeatherComparisonView extends MultipleLocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Historical Comparison";

    public HistoricalWeatherComparisonView(String name, int[] dimensions, int numOfLocations,
                                           GetLocationDataUseCase locationDataUseCase, String apiKey,
                                           ApiService apiService) {
        super(name, dimensions, numOfLocations, locationDataUseCase, apiKey, apiService);
    }
}
