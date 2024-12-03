package application.usecases;

import domain.interfaces.ApiService;
import presentation.ui.windows.ErrorLocationsWindow;
import presentation.ui.windows.LocationsWindow;
import presentation.ui.windows.LocationsWindowGenerator;
import utils.UseCaseInteractor;

/**
 * A use case class for generating a new LocationsWindow using a simple factory pattern.
 * This use case interacts with the LocationDataUseCase and the ApiService to create
 * a window for displaying location data.
 */
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
        final LocationsWindow window =
                LocationsWindowGenerator.generateLocationsWindow(type, dimensions, numOfLocations,
                locationDataUseCase, apiKey, apiService);
        window.getType();
        useCaseFailed = window instanceof ErrorLocationsWindow;
        return window;
    }
}
