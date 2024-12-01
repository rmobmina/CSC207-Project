package application.usecases;

import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.windows.LocationsWindow;
import presentation.ui.windows.LocationsWindowGenerator;

public class GetLocationsWindowUseCase {

    public LocationsWindow execute(String type, int[] dimensions, int numOfLocations,
                                   GetLocationDataUseCase locationDataUseCase, String apiKey,
                                   ApiService apiService) {
        return LocationsWindowGenerator.generateLocationsWindow(type, dimensions, numOfLocations,
                locationDataUseCase, apiKey, apiService);
    }
}
