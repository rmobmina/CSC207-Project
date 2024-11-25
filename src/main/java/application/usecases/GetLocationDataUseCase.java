package application.usecases;

import java.util.List;

import domain.entities.Location;
import domain.interfaces.ApiService;

/**
 * Use case for the weather application.
 * This class retrieves location data and returns it as a Location object.
 */
public class GetLocationDataUseCase {
    private final ApiService apiService;

    public GetLocationDataUseCase(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Method for retrieving the location data of a given city, using a given API key.
     * @param city is a String representing the name of a city given by the user. Not case-sensitive.
     * @param apiKey is a String representing the user's given OpenWeatherMap API key.
     * @return a Location object storing the associated data.
     */
    public List<Location> execute(String city, String apiKey) {
        return apiService.fetchLocations(city, apiKey);
    }
}
