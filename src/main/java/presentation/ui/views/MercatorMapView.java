package presentation.ui.views;

import application.usecases.GetLocationDataUseCase;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import infrastructure.frameworks.MercatorDisplayApp;
import presentation.ui.windows.MultipleLocationsWindow;
import presentation.ui.windows.VisualizationUI;

/**
 * Class that handles UI to display where a given location (or locations) is on a Mercator projection map
 * and provide brief weather details through visuals
 */
public class MercatorMapView extends MultipleLocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Mercator Map";

    private MercatorDisplayApp displayApp;

    public MercatorMapView(String name, int[] dimensions, int numOfLocations,
                           GetLocationDataUseCase locationDataUseCase, String apiKey,
                           ApiService apiService) {
        super(name, dimensions, numOfLocations, locationDataUseCase, apiKey, apiService);
//        mainPanel.setVisible(false);
//        new infrastructure.frameworks.MercatorDisplayApp().startMercatorMap(apiKey,
//                locationDataUseCase, (OpenWeatherApiService) apiService, dimensions[0], dimensions[1]);
    }
}
