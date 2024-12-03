package presentation.ui.windows;

import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;

import java.util.ArrayList;
import java.util.List;

public class MultipleLocationsWindow extends LocationsWindow {
    private List<Location> chosenLocations;

    public MultipleLocationsWindow(String name, int[] dimensions, int numOfLocations, GetLocationDataUseCase locationDataUseCase, String apiKey,
                                   ApiService apiService) {
        super(name, dimensions, locationDataUseCase, apiKey, apiService);
        this.setVisible(true);
    }



    private List<String> getCities(){
        List<String> city = new ArrayList<>();
        for (Location location : chosenLocations) {
            city.add(location.getCity());
        }
        return city;
    }

    @Override
    protected void getWeatherData() {

    }

    @Override
    protected void displayWeatherData() {

    }
}