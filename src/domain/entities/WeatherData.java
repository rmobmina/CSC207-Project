package domain.entities;

import org.json.JSONObject;

public class WeatherData {
    private JSONObject weatherDetails;

    public WeatherData(JSONObject weatherDetails) {
        this.weatherDetails = weatherDetails;
    }

    public JSONObject getWeatherDetails() {
        return weatherDetails;
    }
}
