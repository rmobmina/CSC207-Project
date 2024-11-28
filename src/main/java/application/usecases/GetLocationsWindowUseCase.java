package application.usecases;

import presentation.ui.windows.LocationsWindow;
import presentation.ui.windows.LocationsWindowGenerator;

public class GetLocationsWindowUseCase {

    public LocationsWindow execute(String type, int numOfLocations) {
        return LocationsWindowGenerator.generateLocationsWindow(type, numOfLocations);
    }
}
