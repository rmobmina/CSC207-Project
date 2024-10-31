package application.usecases;

import domain.entities.Location;
import domain.entities.WeatherData;
import domain.services.WeatherService;

public class RetrieveCurrentWeather {
    private final WeatherService weatherService;

    public RetrieveCurrentWeather(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public WeatherData execute(Location location) {
        return weatherService.getCurrentWeather(location);
    }
}
