package infrastructure.adapters;

import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OpenWeatherApiService implements ApiService {

    @Override
    public Location fetchLocation(String city, String apiKey) {
        String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=1&appid=" + apiKey;
        try {
            HttpURLConnection conn = callApi(urlString);
            if (conn.getResponseCode() == 200) {
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();

                JSONArray locationArray = new JSONArray(resultJson.toString());
                if (locationArray.length() > 0) {
                    JSONObject locData = locationArray.getJSONObject(0);
                    return new Location(city, locData.getDouble("lat"), locData.getDouble("lon"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public WeatherData fetchWeather(Location location, String apiKey) {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude()
                + "&lon=" + location.getLongitude() + "&appid=" + apiKey;
        try {
            HttpURLConnection conn = callApi(urlString);
            if (conn.getResponseCode() == 200) {
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();

                JSONObject weatherObject = new JSONObject(resultJson.toString());
                return new WeatherData(weatherObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpURLConnection callApi(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        return conn;
    }
}
