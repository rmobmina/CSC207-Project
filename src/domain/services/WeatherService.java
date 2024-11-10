package domain.services;

import java.time.LocalDate;
import java.util.List;

import domain.entities.Location;
import domain.entities.WeatherData;

/**
 * An interface used for retrieving weather data.
 * Has methods to get a city's current weather, historical weather in a time range, and severe weather alerts.
 */
public interface WeatherService {

    /**
     * Given a Location, return the current weather.
     * @param location is a Location object storing a city's name, longitude, and latitude.
     * @return a JSON object storing location details
     */
    WeatherData getCurrentWeather(Location location);

    /**
     * Given a location object, return a list of severe weather alerts.
     * @param location is a Location object storing a city's name, longitude, and latitude.
     * @param startDate is a LocalDate object storing the year, month, and day
     * @param endDate is a LocalDate object storing the year, month, and day
     * @return a JSON object storing severe weater alerts (tornados, typhoons, ...)
     */
    List<WeatherData> getHistoricalWeather(Location location, LocalDate startDate, LocalDate endDate);

    /**
     * Given a location object, return a list of severe weather alerts.
     * @param location is a Location object storing a city's name, longitude, and latitude.
     * @return a JSON object storing severe weater alerts (tornados, typhoons, ...)
     */
    List<String> getSevereWeatherAlerts(Location location);
}

