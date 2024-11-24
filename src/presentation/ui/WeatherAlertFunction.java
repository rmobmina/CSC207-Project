package presentation.ui;

import org.json.JSONArray;
import org.json.JSONObject;
import infrastructure.adapters.WeatherAPIService;

public class WeatherAlertFunction {
    private final WeatherAPIService weatherAPIService;

    // Constructor
    public WeatherAlertFunction(WeatherAPIService weatherAPIService) {
        this.weatherAPIService = weatherAPIService;
    }

    // Fetch and process severe weather alerts
    public String getSevereWeather(double latitude, double longitude) {
        try {
            // Fetch data using WeatherAPIService
            String response = weatherAPIService.fetchWeatherData(latitude, longitude);

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response);
            if (!jsonResponse.has("alerts")) {
                return "No weather alerts available.";
            }

            JSONArray alertsArray = jsonResponse.getJSONArray("alerts");
            StringBuilder severeAlerts = new StringBuilder();

            for (int i = 0; i < alertsArray.length(); i++) {
                JSONObject alert = alertsArray.getJSONObject(i);

                // Extract severity, urgency, and event type
                String severity = alert.optString("severity", "Unknown");
                String urgency = alert.optString("urgency", "Unknown");
                String event = alert.optString("event", "Unknown");
                String description = alert.optString("desc", "");

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
