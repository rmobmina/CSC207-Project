package infrastructure.adapters;

// importing needed tools
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;

import javax.xml.bind.SchemaOutputResolver;

/**
 * An implementation of the ApiService interface.
 * Parses the OpenWeatherMap API to retrieve location and weather data, given a valid location and API key.
 */
public class OpenWeatherApiService implements ApiService {
    // intializing variables
    private final int responseTreshold = 200;
    private JSONObject locData;
    private JSONObject weatherObject;

    @Override
    public Location fetchLocation(String city, String apiKey) {
        // initializes a local Location variable so we can avoid having multiple returns statements
        Location testLocation = null;

        // here, we try to construct a url to the API based on user input
//        final String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=1&appid=" + apiKey;
        final String urlString = "https://archive-api.open-meteo.com/v1/archive?latitude=52.52&longitude=13.41&"
                + "start_date=2024-10-26&end_date=2024-11-09&hourly=temperature_2m,relative_humidity_2m,apparent_"
                + "temperature,precipitation,rain,weather_code,cloud_cover,cloud_cover_low,cloud_cover_mid,"
                + "cloud_cover_high,wind_speed_10m,wind_speed_100m,wind_direction_10m,wind_direction_100m,"
                + "wind_gusts_10m";

        try {
            final HttpURLConnection conn = callApi(urlString);

            // notably, responseTreshold == 200; a call to the API is successfully IFF the response code is 200
            if (conn.getResponseCode() == responseTreshold) {

                // here, we want to make new object to parse through the result of the API call, then accumulate it
                //      into a string
                final StringBuilder resultJson = new StringBuilder();
                resultJson.append("[");
                final Scanner scanner = new Scanner(conn.getInputStream());

                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                resultJson.append("]");
                scanner.close();

                // the reason why we created the result string is so that we can create a JSON object to store our
                //      information as needed
                final JSONArray locationArray = new JSONArray(resultJson.toString());

                System.out.println(locationArray.toString(4));

                if (locationArray.length() > 0) {
                    locData = locationArray.getJSONObject(0);
                    testLocation = new Location(city, locData.getDouble("latitude"), locData.getDouble("longitude"));
                }
            }
        }

        catch (JSONException | IOException exception) {
            exception.printStackTrace();
        }
        return testLocation;
    }

    @Override
    public WeatherData fetchWeather(Location location, String apiKey) {
        // initializes a local WeatherData variable so we can avoid having multiple return statements
        WeatherData weatherData = null;

        // here, we try to construct a url to the API based on our location data
        final String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude()
                + "&lon=" + location.getLongitude() + "&appid=" + apiKey;

        try {
            final HttpURLConnection conn = callApi(urlString);

            // notably, responseTreshold == 200; a call to the API is successfully IFF the response code is 200
            if (conn.getResponseCode() == responseTreshold) {

                // here, we want to make new object to parse through the result of the API call, then accumulate it
                //      into a string
                final StringBuilder resultJson = new StringBuilder();
                final Scanner scanner = new Scanner(conn.getInputStream());
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();

                // the reason why we created the result string is so that we can create a JSON object to store our
                //      information as needed
                weatherObject = new JSONObject(resultJson.toString());
                weatherData = new WeatherData(weatherObject);
            }
        }
        catch (JSONException | IOException exception) {
            exception.printStackTrace();
        }
        return weatherData;
    }

    private HttpURLConnection callApi(String urlString) throws IOException {
        // we take in the URL string and convert it to a link
        final URL url = new URL(urlString);

        // here, we try to connect and return the response code and data (if any)
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        return conn;
    }
}
