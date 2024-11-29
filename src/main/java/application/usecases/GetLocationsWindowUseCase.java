package application.usecases;

import presentation.ui.windows.LocationsWindow;
import presentation.ui.windows.LocationsWindowGenerator;

public class GetLocationsWindowUseCase {

    public LocationsWindow execute(String type, int width, int height, int numOfLocations) {
        return LocationsWindowGenerator.generateLocationsWindow(type, width, height, numOfLocations);
    }
}
