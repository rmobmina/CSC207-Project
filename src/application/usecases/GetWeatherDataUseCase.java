package application.usecases;

import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;

public class GetWeatherDataUseCase {
    private final ApiService apiService;

    public GetWeatherDataUseCase(ApiService apiService) {
        this.apiService = apiService;
    }

    public WeatherData execute(Location location, String apiKey) {
        return apiService.fetchWeather(location, apiKey);
    }
}
