package presentation.ui;

import domain.entities.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MultipleLocationsWindow extends LocationsWindow {
    private List<Location> chosenLocations;
    private DropDownUI locationsDropdown = new DropDownUI();

    public MultipleLocationsWindow(List<Location> locations) {
        panel.setLayout(new BorderLayout());
        panel.setName("Multiple Locations");
        this.chosenLocations = locations;
    }

    private List<String> getCities(){
        List<String> city = new ArrayList<>();
        for (Location location : chosenLocations) {
            city.add(location.getCity());
        }
        return city;
    }

}
