package domain.interfaces;

import domain.entities.Location;
import domain.entities.WeatherData;

import java.time.LocalDate;

/**
 * An interface used for API calls.
 * Has methods to fetch both the location and the weather data.
 */
public interface ApiService {

    /**
     * Given a city and an API key, calls the OpenWeatherMap API to retrieve details regarding the city.
     * @param city is a String with the user's inputted city
     * @param apiKey is a String with the user's entered API key
     * @return a JSON object storing location details
     */
    Location fetchLocation(String city, String apiKey);

    /**
     * Given a Location object and an API key, calls the OpenWeatherMap API to retrieve details regarding the weather.
     * @param location is a Location object associated with the user's inputted city
     * @return a JSON object storing weather details for the given location
     */
    WeatherData fetchWeather(Location location, LocalDate startDate, LocalDate endDate);

}
