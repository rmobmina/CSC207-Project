package infrastructure.adapters;

// importing needed tools
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;

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
        Location testLocation = null;
        final String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=1&appid=" + apiKey;

        try {
            final HttpURLConnection conn = callApi(urlString);

            if (conn.getResponseCode() == responseTreshold) {
                final StringBuilder resultJson = new StringBuilder();
                final Scanner scanner = new Scanner(conn.getInputStream());

                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();

                final JSONArray locationArray = new JSONArray(resultJson.toString());
                if (locationArray.length() > 0) {
                    final JSONObject locData = locationArray.getJSONObject(0);
                    testLocation = new Location(city, locData.getDouble("lat"), locData.getDouble("lon"));
                }
            }
        }
        catch (JSONException | IOException exception) {
            exception.printStackTrace();
        }

        return testLocation;
    }

    // Used in DropDownUI to fetch and drop down MULTIPLE Locations in the menu
    public List<Location> fetchLocations(String city, String apiKey) {
        final List<Location> locations = new ArrayList<>();
        // Checkstyle fix
        final String unknown = "Unknown";
        final String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=5&appid=" + apiKey;

        try {
            final HttpURLConnection conn = callApi(urlString);

            if (conn.getResponseCode() == responseTreshold) {
                final StringBuilder resultJson = new StringBuilder();
                final Scanner scanner = new Scanner(conn.getInputStream());

                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();

                final JSONArray locationArray = new JSONArray(resultJson.toString());
                for (int i = 0; i < locationArray.length(); i++) {
                    final JSONObject locData = locationArray.getJSONObject(i);
                    final String cityName = locData.optString("name", unknown);
                    final double lat = locData.optDouble("lat", 0.0);
                    final double lon = locData.optDouble("lon", 0.0);
                    final String country = locData.optString("country", unknown);

                    if (!cityName.equals(unknown) && lat != 0.0 && lon != 0.0) {
                        locations.add(new Location(cityName + ", " + country, lat, lon));
                    }
                }
            }
            else {
                System.err.println("Error: API returned response code " + conn.getResponseCode());
            }
        }
        catch (JSONException | IOException exception) {
            System.err.println("Error fetching locations: " + exception.getMessage());
        }

        return locations;
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