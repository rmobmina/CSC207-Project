package presentation.ui.views;

import application.usecases.GetLocationDataUseCase;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.windows.MultipleLocationsWindow;

/**
 * Class that handles UI to display where a given location (or locations) is on a Mercator projection map
 * and provide brief weather details through visuals
 */
public class MercatorMapView extends MultipleLocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Mercator Map";

    public MercatorMapView(String name, int[] dimensions, int numOfLocations,
                           GetLocationDataUseCase locationDataUseCase, String apiKey,
                           ApiService apiService) {
        super(name, dimensions, numOfLocations, locationDataUseCase, apiKey, apiService);
    }
}
