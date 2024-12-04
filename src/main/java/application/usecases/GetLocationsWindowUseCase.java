package application.usecases;

import domain.interfaces.ApiService;
import presentation.ui.dashboard.NewDashBoardUi;
import presentation.ui.windows.LocationsWindow;
import presentation.ui.windows.LocationsWindowGenerator;
import utils.UseCaseInteractor;

/**
 * Use case for creating a new Locations window using a Simple Factory.
 */

public class GetLocationsWindowUseCase extends UseCaseInteractor {

    /**
     * Creates a new Locations window using a Simple Factory.
     *
     * @param type                the specific view that is a subclass of LocationsWindow
     * @param dimensions          the width and height (put as an array) of the JFrame window
     * @param numOfLocations      the number of locations the user entered
     * @param locationDataUseCase the use case interactor to get the location
     * @param apiKey              the API key used by ApiService and locationDataUseCase
     * @param apiService          the API service where all information is extracted from
     * @param dashboard           the dashboard UI to interact with the created window
     * @return a new LocationsWindow instance based on a specific view and other parameters
     * @null type if an invalid or unsupported type is provided
     * @null apiKey if the API key is invalid or missing
     */
    public LocationsWindow execute(String type, int[] dimensions, int numOfLocations,
                                   GetLocationDataUseCase locationDataUseCase, String apiKey,
                                   ApiService apiService, NewDashBoardUi dashboard) {
        return LocationsWindowGenerator.generateLocationsWindow(type, dimensions, numOfLocations,
                locationDataUseCase, apiKey, apiService, dashboard);
    }

}
