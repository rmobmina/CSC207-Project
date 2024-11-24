package presentation.ui;

import domain.entities.Location;

import java.util.List;

public class LocationsWindowGenerator {
    public static LocationsWindow generateLocationsWindow(List<Location> locations) {
        // Generates a new window that display an error if no valid locations were chosen
        if (locations.isEmpty()) {
            return new ErrorLocationsWindow();
        }
        // Generates a new window for multiple locations if there are atleast 2 valid locations chosen
        else if (locations.size() > 1) {
            return new MultipleLocationsWindow(locations);
        }
        else {
            return null;
        }
    }
}
