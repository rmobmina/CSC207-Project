package presentation.ui.windows;

import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.DropDownUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MultipleLocationsWindow extends LocationsWindow {
    private List<Location> chosenLocations;

    public MultipleLocationsWindow(String name, int[] dimensions, int numOfLocations, GetLocationDataUseCase locationDataUseCase, String apiKey,
                                   ApiService apiService) {
        super(name, dimensions, locationDataUseCase, apiKey, apiService);
        this.visualizationUI = new VisualizationUI(2, getMainPanel());
        this.setVisible(true);
    }

    // add two dropdowns, one for city one and one for city two fields to replace later for historicalweathercomparison

    private List<String> getCities(){
        List<String> city = new ArrayList<>();
        for (Location location : chosenLocations) {
            city.add(location.getCity());
        }
        return city;
    }

    @Override
    protected void openVisualization() {

    }

    @Override
    protected void getWeatherData() {

    }

    @Override
    protected void displayWeatherData() {

    }
}