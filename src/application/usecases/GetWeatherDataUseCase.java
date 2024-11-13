package application.usecases;

// importing needed tools
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;

import java.time.LocalDate;

/**
 * Use case for the weather application.
 * This class retrieves weather data and returns it as a WeatherData object.
 */
public class GetWeatherDataUseCase {
    private final ApiService apiService;

    public GetWeatherDataUseCase(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Method for retrieving the weather data of a given city, using a given API key.
     * @param location is a Location object representing the name of a city given by the user.
     * @return a WeatherData object storing the weather details.
     */
    public WeatherData execute(Location location, LocalDate startDate, LocalDate endDate) {
        // calls the API service to extract weather information in JSON format
        return apiService.fetchWeather(location, startDate, endDate);
    }
}
