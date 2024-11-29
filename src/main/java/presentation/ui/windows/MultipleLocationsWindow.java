package presentation.ui.windows;

import domain.entities.Location;
import presentation.ui.DropDownUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MultipleLocationsWindow extends LocationsWindow {
    private List<Location> chosenLocations;

    public MultipleLocationsWindow(String name, int width, int height, int numOfLocations) {
        super(name, width, height);
        panel.setLayout(new BorderLayout());
    }

    private List<String> getCities(){
        List<String> city = new ArrayList<>();
        for (Location location : chosenLocations) {
            city.add(location.getCity());
        }
        return city;
    }

}