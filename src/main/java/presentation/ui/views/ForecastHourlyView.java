package presentation.ui.views;

import application.usecases.GetLocationDataUseCase;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.windows.LocationsWindow;

/**
 * Class that handles UI to display the hourly forcast to how exact
 * temperatures and humidities each hour of a certain day
 */
public class ForecastHourlyView extends LocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Forecast Hourly";

    public ForecastHourlyView(String name, int[] dimensions, GetLocationDataUseCase locationDataUseCase, String apiKey,
                              ApiService apiService) {
        super(name, dimensions, locationDataUseCase, apiKey, apiService);
    }

    @Override
    protected void getWeatherData() {
        // This is where you should get the weather data details from the use case interactor and use it to generate a weatherDTO object

    }

    @Override
    protected void displayWeatherData() {
        // This is where you should update your JPanel components based on weather details
    }
}
