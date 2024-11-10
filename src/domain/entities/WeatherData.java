package domain.entities;

import org.json.JSONObject;

/**
 * A class representing weather data for a specified location.
 * Stores information (consisting of temperature, wind speed, location name, and more) in a JSONObject.
 */
public class WeatherData {
    private JSONObject weatherDetails;

    public WeatherData(JSONObject weatherDetails) {
        this.weatherDetails = weatherDetails;
    }

    public JSONObject getWeatherDetails() {
        return weatherDetails;
    }
}
