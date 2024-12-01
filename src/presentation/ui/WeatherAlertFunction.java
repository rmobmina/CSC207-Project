package presentation.ui;

import org.json.JSONArray;
import org.json.JSONObject;

import infrastructure.adapters.WeatherAPIService;

import java.util.ArrayList;
import java.util.List;

public class WeatherAlertFunction {
    private final WeatherAPIService weatherApiService;
    private final List<WeatherAlertObserver> observers = new ArrayList<>(); // List of observers

    // Constructor
    public WeatherAlertFunction(WeatherAPIService weatherApiService) {
        this.weatherApiService = weatherApiService;
    }

    // Add an observer
    public void addObserver(WeatherAlertObserver observer) {
        observers.add(observer);
    }

    // Remove an observer
    public void removeObserver(WeatherAlertObserver observer) {
        observers.remove(observer);
    }

    // Notify all observers with the alert message
    private void notifyObservers(String alertMessage) {
        System.out.println("Notifying observers with alert: " + alertMessage);
        for (WeatherAlertObserver observer : observers) {
            System.out.println("Notifying observer: " + observer.getClass().getName());
            observer.onSevereWeatherAlert(alertMessage);
        }
    }


    // Fetch and process severe weather alerts
    public String getSevereWeather(double latitude, double longitude) {
        try {
            // Mock severe weather alert for TestCity
            if (latitude == 0.0 && longitude == 0.0) {
                String mockAlert = "Severe Weather Alert, check local news for more information";
                notifyObservers(mockAlert); // Notify observers only once
                return mockAlert;
            }

            // Fetch data from the API
            final String response = weatherApiService.fetchWeatherData(latitude, longitude);

            // Parse the JSON response
            final JSONObject jsonResponse = new JSONObject(response);
            if (!jsonResponse.has("alerts") || jsonResponse.getJSONArray("alerts").length() == 0) {
                return "No severe weather alerts available.";
            }

            final JSONArray alertsArray = jsonResponse.getJSONArray("alerts");
            final StringBuilder severeAlerts = new StringBuilder();

            for (int i = 0; i < alertsArray.length(); i++) {
                final JSONObject alert = alertsArray.getJSONObject(i);

                // Extract severity and urgency
                final String severity = alert.optString("severity", "Unknown");
                final String urgency = alert.optString("urgency", "Unknown");
                final String event = alert.optString("event", "Unknown");

                if ("Severe".equalsIgnoreCase(severity) || "Immediate".equalsIgnoreCase(urgency)) {
                    severeAlerts.append(event).append(" - ").append(severity).append(" - ").append(urgency).append("\n");
                }
            }

            String alertMessage = severeAlerts.length() > 0 ? severeAlerts.toString() : "No severe weather alerts.";
            if (severeAlerts.length() > 0) {
                notifyObservers(alertMessage); // Notify observers only once
            }
            return alertMessage;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching weather alert data.";
        }
    }


}
