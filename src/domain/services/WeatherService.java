package domain.services;

import domain.entities.Location;
import domain.entities.WeatherData;

import java.time.LocalDate;
import java.util.List;

public interface WeatherService {
    WeatherData getCurrentWeather(Location location);
    List<WeatherData> getHistoricalWeather(Location location, LocalDate startDate, LocalDate endDate);

    List<String> getSevereWeatherAlerts(Location location);
}

