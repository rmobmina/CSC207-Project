package infrastructure.apis;

import domain.entities.Location;
import domain.entities.WeatherData;
import domain.services.WeatherService;
import utils.HttpUtils;

import java.time.LocalDate;
import java.util.List;

public class OpenWeatherService implements WeatherService {
    private static final String API_KEY = "I will handle this later.";
    private static final String BASE_URL = "https://api.openweathermap.org/data/3.0/onecall";

    @Override
    public WeatherData getCurrentWeather(Location location) {
        String url = BASE_URL + "?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + API_KEY;
        String response = HttpUtils.makeHttpRequest(url);

        // Parse JSON and map to WeatherData
        // Return new WeatherData object with parsed data
        return null;
    }

    @Override
    public List<WeatherData> getHistoricalWeather(Location location, LocalDate startDate, LocalDate endDate) {
        // Similar logic for historical weather data
        return java.util.Collections.emptyList();
    }

    @Override
    public List<String> getSevereWeatherAlerts(Location location) {
        // Retrieve alerts using API, parse response
        return java.util.Collections.emptyList();
    }

}
