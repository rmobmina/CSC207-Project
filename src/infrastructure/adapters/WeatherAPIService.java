package infrastructure.adapters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 */
public class WeatherAPIService {
    private final String apiKey;

    // Constructor to initialize the API key
    public WeatherAPIService(String apiKey) {
        this.apiKey = apiKey;
    }

    // Fetch weather data from the API

    /**
     *
     * @param latitude
     * @param longitude
     * @return
     * @throws Exception
     */
    public String fetchWeatherData(double latitude, double longitude) throws Exception {
        try {
            // Construct the API URL
            final String urlString = String.format(
                "https://api.weatherapi.com/v1/current.json?key=%s&q=%s,%s",
                URLEncoder.encode(apiKey, "UTF-8"),
                latitude,
                longitude
            );
            final URL url = new URL(urlString);

            // Open connection
            final HttpURLConnection connection = openConnection(url);

            // Check response code
            final int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new Exception(
                    "Failed to fetch weather data. HTTP code: " + responseCode
                );
            }

            // Read the response
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
                final StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }

        }
        catch (Exception e) {
            throw new Exception("Error fetching weather data: " + e.getMessage(), e);
        }
    }

    private HttpURLConnection openConnection(URL url) throws Exception {
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        return connection;
    }
}
