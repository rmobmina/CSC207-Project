package application.usecases;

import domain.interfaces.ApiService;
import presentation.ui.windows.LocationsWindow;
import presentation.ui.windows.LocationsWindowGenerator;
import utils.UseCaseInteractor;

public class GetLocationsWindowUseCase extends UseCaseInteractor {
    /**
     * Creates a new Locations window using a Simple Factory.
     * @param type the specific view that is a subclass of LocationsWindow
     * @param dimensions the width and height (put as an array) of the JFrame window
     * @param numOfLocations the number of locations the user entered
     * @param locationDataUseCase the usecase interactor to get the location
     * @param apiKey the api key used by apiService and locationDataUseCase
     * @param apiService the api service where all information is extracted from
     * @return a new LocationsWindow instance based on a specific view and other parameters
     */
    public LocationsWindow execute(String type, int[] dimensions, int numOfLocations,
                                   GetLocationDataUseCase locationDataUseCase, String apiKey,
                                   ApiService apiService) {
        return LocationsWindowGenerator.generateLocationsWindow(type, dimensions, numOfLocations,
                locationDataUseCase, apiKey, apiService);
    }
}
