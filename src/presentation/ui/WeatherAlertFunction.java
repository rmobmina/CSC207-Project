package presentation.ui;

import org.json.JSONArray;
import org.json.JSONObject;

import infrastructure.adapters.WeatherAPIService;

/**
 *
 */
public class WeatherAlertFunction {
    private final WeatherAPIService weatherApiService;

    // Constructor
    public WeatherAlertFunction(WeatherAPIService weatherApiService) {
        this.weatherApiService = weatherApiService;
    }

    // Fetch and process severe weather alerts
    /**
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public String getSevereWeather(double latitude, double longitude) {
        try {
            // Fetch data using WeatherAPIService
            final String response = weatherApiService.fetchWeatherData(latitude, longitude);

            // Parse the JSON response
            final JSONObject jsonResponse = new JSONObject(response);
            if (!jsonResponse.has("alerts")) {
                return "No weather alerts available.";
            }

            final JSONArray alertsArray = jsonResponse.getJSONArray("alerts");
            final StringBuilder severeAlerts = new StringBuilder();

            for (int i = 0; i < alertsArray.length(); i++) {
                final JSONObject alert = alertsArray.getJSONObject(i);

                // Extract severity, urgency, and event type
                final String defaultValue = "Unknown";
                final String severity = alert.optString("severity", defaultValue);
                final String urgency = alert.optString("urgency", defaultValue);
                final String event = alert.optString("event", defaultValue);
                final String description = alert.optString("desc", "");

                // Identify if the alert qualifies as severe
                if ("Severe".equalsIgnoreCase(severity) || "Immediate".equalsIgnoreCase(urgency)) {
                    severeAlerts.append("Event: ").append(event)
                                .append("\nSeverity: ").append(severity)
                                .append("\nUrgency: ").append(urgency)
                                .append("\nDescription: ").append(description)
                                .append("\n\n");
                }
            }

            return severeAlerts.length() > 0 ? severeAlerts.toString() : "No severe weather alerts.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching weather alert data.";
        }
    }
}
