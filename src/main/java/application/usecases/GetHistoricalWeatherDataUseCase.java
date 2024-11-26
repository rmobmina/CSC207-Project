package application.usecases;

// importing needed tools
import java.time.LocalDate;

import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;

/**
 * Use case for the weather application.
 * This class retrieves historical weather data and returns it as a WeatherData object.
 */
public class GetHistoricalWeatherDataUseCase {
    private final ApiService apiService;

    public GetHistoricalWeatherDataUseCase(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Method for retrieving the weather data of a given city, using a given API key.
     * @param location is a Location object representing the name of a city given by the user.
     * @param startDate is a LocalDate object storing the exact start of a date range.
     * @param endDate is a LocalDate object storing the exact end of a date range.
     * @return a WeatherData object storing the weather details.
     */
    public WeatherData execute(Location location, LocalDate startDate, LocalDate endDate) {
        // calls the API service to extract weather information in JSON format
        return apiService.fetchHistoricalWeather(location, startDate, endDate);
    }
}
