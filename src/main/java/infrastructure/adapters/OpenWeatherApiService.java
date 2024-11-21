package infrastructure.adapters;

// importing needed tools
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
                    testLocation = new Location(city, locData.getDouble("lat"), locData.getDouble("lon"));
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
        final String urlString =
                "https://historical-forecast-api.open-meteo.com/v1/forecast?" +
                "latitude=" + location.getLatitude() + "&longitude=" + location.getLongitude() +
                "&hourly=temperature_2m,relative_humidity_2m&daily=temperature_2m_max,temperature_2m_min,precipitation_sum,snowfall_sum,wind_speed_10m_max,wind_gusts_10m_max,wind_direction_10m_dominant" +
                "&start_date=" + startDate.format(DateTimeFormatter.ISO_DATE) + "&end_date=" + endDate.format(DateTimeFormatter.ISO_DATE);
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

    // Used in DropDownUI to fetch and drop down MULTIPLE Locations in the menu
    public List<Location> fetchLocations(String city, String apiKey) {
        final List<Location> locations = new ArrayList<>();
        // Checkstyle fix
        final String unknown = "Unknown";
        final String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=5&appid=" + apiKey;

        try {
            final HttpURLConnection conn = callApi(urlString);

            if (conn.getResponseCode() == Constants.RESPONSE_TRESHOLD) {
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
                    final String state = locData.optString("state", unknown);

                    if (!cityName.equals(unknown) && lat != 0.0 && lon != 0.0) {
                        locations.add(new Location(cityName, state, country, lat, lon));
                    }
                }
            }
            else {
                System.err.println("Error: API returned response code " + conn.getResponseCode());
                return null;
            }
        }
        catch (JSONException | IOException exception) {
            System.err.println("Error fetching locations: " + exception.getMessage());

        }

        return locations;
    }

    /**
     * An online Api caller method.
     * Enters the given URL and returns an object representing the status of an API call.
     * @return an HttpURLConnection object storing the results of our API call.
     * @throws IOException if the URL is invalid
     */
    private HttpURLConnection callApi(String urlString) throws IOException {
        // we take in the URL string and convert it to a link
        final URL url = new URL(urlString);

        // here, we try to connect and return the response code and data (if any)
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        return conn;
    }

    // Determines if the given api key is valid for calling the weather api service
    public boolean isAPIKeyValid(String apiKey){
        if (apiKey.isEmpty()) return false;
        // To validate the given api key, we need a test location to use to call it and see if any errors occur
        String testCity = "Toronto";
        String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=" + testCity + "&limit=5&appid=" + apiKey;
        try {
            // if it fails to get the response code, then api key is invalid
            if (callApi(urlString).getResponseCode() != Constants.RESPONSE_TRESHOLD) {
                return false;
            }
        }
        catch (IOException exception) {
            // If it throws an error, then api key is invalid
            exception.printStackTrace();
            return false;
        }
        // If no errors above occur, then api key is valid
        return true;
    }
}
