package application.usecases;

import domain.entities.WeatherData;
import domain.interfaces.ApiService;

/**
 * Use case for retrieving the current weather condition of a location.
 */
public class GetCurrentWeatherConditionUseCase {
    private final ApiService apiService;

    public GetCurrentWeatherConditionUseCase(ApiService apiService) {
        this.apiService = apiService;
    }

    /**
     * Retrieves the main weather condition (e.g., sunny, rainy) for a given location.
     *
     * @param weatherData The weather data object containing details for the location.
     * @return The main weather condition as a string (or "No data available" if not found).
     */
    public String execute(WeatherData weatherData) {
        if (weatherData == null) {
            return "No data available";
        }

        // Delegate condition extraction to the WeatherData entity
        String condition = weatherData.getCondition();
        return condition != null ? condition : "No data available";
    }
}
