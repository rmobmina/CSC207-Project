package infrastructure.adapters;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAlertFunction {
    private final String apiKey;

    public WeatherAlertFunction(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSevereWeather(String location) {
        try {

            String urlString = "https://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + location;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

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

            if (condition.contains("storm") || condition.contains("extreme") || condition.contains("alert")) {
                return "Severe Weather Alert: " + condition;
            } else {
                return "No severe weather alerts.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching weather alert data.";
        }
    }
}

