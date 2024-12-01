package application.usecases;

import domain.entities.Location;
import domain.services.WeatherService;

import java.util.List;

/**
 * Use case for retrieving severe weather alerts.
 */
public class GetSevereWeatherAlertsUseCase {
    // Uses WeatherService specifically for more domain-specific weather conditions
    private final WeatherService weatherService;

    public GetSevereWeatherAlertsUseCase(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Retrieves severe weather alerts for a given location.
     * @param location The location for which to retrieve alerts.
     * @return A list of severe weather alerts (or an empty list if none are available).
     */
    public List<String> execute(Location location) {
        // Delegate the logic to the WeatherService
        return weatherService.getSevereWeatherAlerts(location);
    }
}
