package presentation.ui;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAlertFunction {
    private final String apiKey;

    // Constructor that accepts the API key
    public WeatherAlertFunction(String apiKey) {
        this.apiKey = apiKey;
    }

    // Method to get severe weather alerts
    public String getSevereWeather(double latitude, double longitude) {
        try {
            // Construct URL with the given location and API key
            String urlString = "https://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + latitude + "," + longitude;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response from the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(response.toString());
            JSONObject current = json.getJSONObject("current");
            String condition = current.getJSONObject("condition").getString("text");

            // Check for dangerous weather keywords in condition
            if (condition.toLowerCase().contains("storm")
                    || condition.toLowerCase().contains("extreme")
                    || condition.toLowerCase().contains("alert")) {
                return "Dangerous Weather Alert: " + condition;
            }
            else {
                return "No severe weather alerts.";
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error fetching weather alert data.";
        }
    }
}

//    public String getSevereWeather(String location) {
//        try {
//
//            String urlString = "https://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + location;
//            System.out.println("Request URL: " + urlString);
//            URL url = new URL(urlString);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            reader.close();
//
//            JSONObject json = new JSONObject(response.toString());
//            JSONObject current = json.getJSONObject("current");
//            String condition = current.getJSONObject("condition").getString("text");
//
//            if (condition.toLowerCase().contains("storm") || condition.toLowerCase().contains("extreme") || condition.toLowerCase().contains("alert")) {
//                return "Severe Weather Alert: " + condition;
//            } else {
//                return condition; // Return the general weather condition
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error fetching weather alert data.";
//        }
//    }
