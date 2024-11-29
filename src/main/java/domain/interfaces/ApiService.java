package domain.interfaces;

import java.time.LocalDate;
import java.util.List;

import domain.entities.Location;
import domain.entities.WeatherData;

/**
 * An interface used for API calls.
 * Has methods to fetch both the location and the weather data.
 */
public interface ApiService {

    /**
     * Given a Location object and an API key, calls the OpenWeatherMap API to retrieve weather forcast details.
     * @param location is a Location object associated with the user's inputted city.
     * @param numberOfDays The number of days that the forcast is set to
     * @return a JSON object storing weather details for the given location
     */
    WeatherData fetchForecastWeather(Location location, int numberOfDays);

    /**
     * Given a Location object and an API key, calls the OpenWeatherMap API to retrieve details
     * regarding the historical weather data over a range of time.
     * @param location is a Location object associated with the user's inputted city.
     * @param startDate is a LocalDate object storing the exact start of a date range.
     * @param endDate is a LocalDate object storing the exact end of a date range.
     * @return a JSON object storing weather details for the given location
     */
    WeatherData fetchHistoricalWeather(Location location, LocalDate startDate, LocalDate endDate);

    /**
     * Given a valid Open-Meteo API call, return a WeatherData object storing relevant weather details.
     * @param urlString a String object representing a valid Open-Meteo API call url.
     * @return a WeatherData object storing weather details in JSON form.
     */
    WeatherData fetchWeather(String urlString);

    /**
     * Given a city name and an API key, calls the OpenWeatherMap API to retrieve details of up to five cities with
     *      the same name.
     * @param city is a String with the user's inputted city
     * @param apiKey is a String with the user's entered API key
     * @return a List of matching Locations
     */
    List<Location> fetchLocations(String city, String apiKey);

}
