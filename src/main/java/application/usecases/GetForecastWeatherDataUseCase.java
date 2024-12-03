package application.usecases;

// importing needed tools

import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import utils.UseCaseInteractor;

/**
 * Use case for the weather application.
 * This class retrieves forcast weather data and returns it as a WeatherData object.
 */
public class GetForecastWeatherDataUseCase extends UseCaseInteractor {
    private final ApiService apiService;

    public GetForecastWeatherDataUseCase(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Method for retrieving the weather data of a given city, using a given API key.
     * @param location is a Location object representing the name of a city given by the user.
     * @param numberOfDays The number of days described by the weather forcast (up to 16)
     * @return a WeatherData object storing the weather details.
     */
    public WeatherData execute(Location location, int numberOfDays) {
        // calls the API service to extract weather information in JSON format
        final WeatherData weatherData = apiService.fetchForecastWeather(location, numberOfDays);
        useCaseFailed = weatherData == null;
        return weatherData;
    }
}
