package presentation.ui.views;

import presentation.ui.windows.MultipleLocationsWindow;

/**
 * Class that handles UI to display where a given location (or locations) is on a Mercator projection map
 * and provide brief weather details through visuals
 */
public class MercatorMapView extends MultipleLocationsWindow {
    public MercatorMapView(String name, int numOfLocations) {
        super(name, numOfLocations);
    }
}
