package domain.interfaces;

import domain.entities.Location;
import domain.entities.WeatherData;

public interface ApiService {
    Location fetchLocation(String city, String apiKey);
    WeatherData fetchWeather(Location location, String apiKey);
}
