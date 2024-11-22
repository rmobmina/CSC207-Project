package domain.services;

import domain.entities.Location;
import java.time.LocalDate;
import java.util.List;

/**
 * WeatherService interface for weather data retrieval.
 * Provides methods for fetching current weather, historical data, and temperature trends.
 */
public interface WeatherService {

    /**
     * Fetches the current weather for a given location.
     *
     * @param location Location object containing city, latitude, and longitude.
     * @return List of WeatherData objects containing current weather details.
     */
    WeatherData getCurrentWeather(Location location);

    /**
     * Fetches the historical weather data for a given location and date range.
     *
     * @param location Location object containing city, latitude, and longitude.
     * @param startDate Start date for the historical data range.
     * @param endDate End date for the historical data range.
     * @return List of WeatherData objects containing historical weather details.
     */
    List<WeatherData> getHistoricalWeather(Location location, LocalDate startDate, LocalDate endDate);

    /**
     * Fetches severe weather alerts for a given location.
     *
     * @param location Location object containing city, latitude, and longitude.
     * @return List of strings representing severe weather alerts.
     */
    List<String> getSevereWeatherAlerts(Location location);

    /**
     * Fetches the daily average temperature trends for a given location over a specific date range.
     *
     * @param location Location object containing city, latitude, and longitude.
     * @param startDate Start date for the trend data.
     * @param endDate End date for the trend data.
     * @return List of average daily temperatures as doubles.
     */
    List<Double> getTemperatureTrends(Location location, LocalDate startDate, LocalDate endDate);
}
