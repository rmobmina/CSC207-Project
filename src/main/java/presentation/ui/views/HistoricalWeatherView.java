package presentation.ui.views;

import application.usecases.GetLocationDataUseCase;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.windows.LocationsWindow;

/**
 * Class that handles UI to display the historical temperature and climate trends over a certain period of time
 * of a given location. (Alternative: HistoricalWeatherComparisonView)
 */
public class HistoricalWeatherView extends LocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Historical";

    public HistoricalWeatherView(String name, int[] dimensions, GetLocationDataUseCase locationDataUseCase, String apiKey,
                                 ApiService apiService) {
        super(name, dimensions, locationDataUseCase, apiKey, apiService);
    }

    @Override
    protected void getWeatherData() {

    }

    @Override
    protected void displayWeatherData() {

    }
}
