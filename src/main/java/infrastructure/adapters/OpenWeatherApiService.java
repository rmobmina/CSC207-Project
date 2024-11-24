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
 * Parses the OpenWeatherMap API to retrieve a location and the OpenMeteo Weather API,
 *      given a valid location and API key.
 */
public class OpenWeatherApiService implements ApiService {
    private JSONObject locData;
    private JSONObject weatherObject;

    @Override
    public WeatherData fetchForcastWeather(Location location, int numberOfDays){
        final String urlString =
                "https://api.open-meteo.com/v1/forecast?" +
                        "latitude=" + location.getLatitude() + "&longitude=" + location.getLongitude() +
                        "&hourly=temperature_2m,relative_humidity_2m" +
                        "&daily=temperature_2m_max,temperature_2m_min,temperature_2m_mean,precipitation_sum," +
                        "wind_speed_10m_max,wind_direction_10m_dominant" +
                        "&forecast_days=3";
        return fetchWeather(urlString);
    }

    @Override
    public WeatherData fetchHistoricalWeather(Location location, LocalDate startDate, LocalDate endDate){
        // since the user can choose a time range, we construct a call to the API using the user's selected times.
        //      furthermore, we take the stored longitude and latitude of the user's chosen city and enter it into the
        //      API call
        final String urlString =
                "https://archive-api.open-meteo.com/v1/archive?" +
                        "latitude=" + location.getLatitude() + "&longitude=" + location.getLongitude() +
                        "&start_date=" + startDate.format(DateTimeFormatter.ISO_DATE) + "&end_date=" + endDate.format(DateTimeFormatter.ISO_DATE) +
                        "&hourly=temperature_2m,relative_humidity_2m" +
                        "&daily=temperature_2m_max,temperature_2m_min,temperature_2m_mean,precipitation_sum," +
                        "wind_speed_10m_max,wind_direction_10m_dominant";
        return fetchWeather(urlString);

    }

    public WeatherData fetchWeather(String urlString) {
        // instantiating our return object
        WeatherData weatherData = null;

        // since it is possible that the date or location values are invalid, we try to run the API call and
        //  catch any related errors
        try {
            final String response = makeApiCall(urlString);
            if (response != null) {
                weatherObject = new JSONObject(response);
                weatherData = new WeatherData(weatherObject);
            }
        }

        catch (JSONException | IOException exception) {
            exception.printStackTrace();
        }

        return weatherData;
    }

    /**
     * Given a city name and API key, return up to five locations with the same name along with weather details.
     * @param city is a String with the user's inputted city
     * @param apiKey is a String with the user's entered API key
     * @return a List of Location objects storing the city name, state, country, and weather information.
     */
    public List<Location> fetchLocations(String city, String apiKey) {
        // instantiating our return object and use variables
        final List<Location> locations = new ArrayList<>();
        final String unknown = "Unknown";
        final String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&limit=5&appid=" + apiKey;

        try {
            final String response = makeApiCall(urlString);
            if (response != null) {
                final JSONArray locationArray = new JSONArray(response);
                for (int i = 0; i < locationArray.length(); i++) {
                    locData = locationArray.getJSONObject(i);
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
        }
        catch (JSONException | IOException exception) {
            System.err.println("Error fetching locations: " + exception.getMessage());
        }
        return locations;
    }

    /**
     * Tests if the given OpenWeatherMap API key is valid by checking for the location data of Toronto, ON, CA.
     * @param apiKey is a user-entered String object representing their OpenWeatherMap API key.
     * @return true if the call is successful, false otherwise.
     */
    public boolean isApiKeyValid(String apiKey) {
        boolean valid = true;

        if (apiKey.isEmpty()) {
            valid = false;
        }
        final String testCity = "Toronto";
        final String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=" + testCity + "&limit=5&appid="
                + apiKey;
        try {
            if (makeApiCall(urlString) == null) {
                valid = false;
            }
        }
        catch (IOException exception) {
            exception.printStackTrace();
            valid = false;
        }
        return valid;
    }

    private HttpURLConnection callApi(String urlString) throws IOException {
        final URL url = new URL(urlString);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        return conn;
    }

    private String makeApiCall(String urlString) throws IOException {
        String result = null;

        final HttpURLConnection conn = callApi(urlString);
        if (conn.getResponseCode() == Constants.RESPONSE_TRESHOLD) {
            final StringBuilder resultJson = new StringBuilder();
            try (Scanner scanner = new Scanner(conn.getInputStream())) {

                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }

            }

            result = resultJson.toString();
        }

        else {
            System.err.println("Error: API returned response code " + conn.getResponseCode());
        }

        return result;
    }
}
