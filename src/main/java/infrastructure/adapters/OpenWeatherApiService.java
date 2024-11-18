package infrastructure.adapters;

// importing needed tools
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import utils.Constants;

/**
 * An implementation of the ApiService interface.
 * Parses the OpenWeatherMap API to retrieve location and weather data, given a valid location and API key.
 */
public class OpenWeatherApiService implements ApiService {
    // intializing variables
    private JSONObject locData;
    private JSONObject weatherObject;

    @Override
    public Location fetchLocation(String city, String apiKey) {
        // initializes a local Location variable so we can avoid having multiple returns statements
        Location testLocation = null;

        // here, we try to construct a url to the API based on user input
        final String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=5&appid=" + apiKey;

        try {
            final HttpURLConnection conn = callApi(urlString);

            // notably, responseTreshold == 200; a call to the API is successfully IFF the response code is 200
            if (conn.getResponseCode() == Constants.RESPONSE_TRESHOLD) {

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
                final JSONArray locationArray = new JSONArray(resultJson.toString());

                System.out.println(locationArray.toString(Constants.INDENT_FACTOR));

                if (locationArray.length() > 0) {
                    locData = locationArray.getJSONObject(0);
                    testLocation = new Location(city.substring(0, 1).toUpperCase()
                            + city.substring(1).toLowerCase(), locData.getString("state"),
                            locData.getString("country"),
                            locData.getDouble("lat"), locData.getDouble("lon"));
                }
            }
        }

        catch (JSONException | IOException exception) {
            exception.printStackTrace();
        }
        return testLocation;
    }

    @Override
    public WeatherData fetchWeather(Location location, LocalDate startDate, LocalDate endDate) {
        // initializes a local WeatherData variable so we can avoid having multiple return statements
        WeatherData weatherData = null;

        // here, we try to construct a url to the API based on our location data
        final String urlString = "https://api.open-meteo.com/v1/forecast?latitude="
                + location.getLatitude()
                + "&longitude=" + location.getLongitude()
                + "&start_date=" + "2024-10-09" + "&end_date=" + "2024-10-09"
                + "&hourly=temperature_2m,relative_humidity_2m&daily=temperature_2m_max,temperature_2m_min,"
                + "apparent_temperature_max,apparent_temperature_min,precipitation_sum,rain_sum,snowfall_sum,"
                + "wind_speed_10m_max,wind_gusts_10m_max,wind_direction_10m_dominant";
        try {
            final HttpURLConnection conn = callApi(urlString);

            // notably, responseTreshold == 200; a call to the API is successfully IFF the response code is 200
            if (conn.getResponseCode() == Constants.RESPONSE_TRESHOLD) {

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

    public HttpURLConnection callApi(String urlString) throws IOException {
        // we take in the URL string and convert it to a link
        final URL url = new URL(urlString);

        // here, we try to connect and return the response code and data (if any)
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        return conn;
    }
}
