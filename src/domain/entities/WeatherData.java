package domain.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class representing weather data for a specified location.
 * Stores information (consisting of temperature, wind speed, location name, and more) in a JSONObject.
 */
public class WeatherData {
    private final JSONObject weatherDetails;
    private String condition; // Weather description (e.g., "light rain")
    private String iconCode;  // Weather icon code (e.g., "10d")

    public WeatherData(JSONObject weatherDetails) throws JSONException {
        this.weatherDetails = weatherDetails;

        // Parse `weather` array for condition and icon
        JSONArray weatherArray = weatherDetails.getJSONArray("weather");
        if (weatherArray.length() > 0) {
            JSONObject weatherCondition = weatherArray.getJSONObject(0);
            this.condition = weatherCondition.optString("description", "Unknown condition");
            this.iconCode = weatherCondition.optString("icon", null);
        } else {
            this.condition = "Unknown condition";
            this.iconCode = null;
        }
    }


    public JSONObject getWeatherDetails() {
        return weatherDetails;
    }

    public String getCondition() {
        return condition; // Return the parsed weather description
    }

    public String getIconCode() {
        return iconCode; // Return the parsed weather icon code
    }
}
