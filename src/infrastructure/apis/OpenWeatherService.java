package infrastructure.apis;

import java.net.HttpURLConnection;
import java.util.Scanner;
import java.io.IOException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OpenWeatherService {
    private String API_KEY = "";
    private String lon;
    private String lat;
    private String location = "";
    private JSONArray locDat;
    private JSONObject weatherDat;

    public OpenWeatherService() {
        while (true) {
            getInformation();
            getData();
            getWeatherData();
        }
    }

    public void getInformation() {
        Scanner myObj = new Scanner(System.in);
        // Get city and API key input from the user
        System.out.println("(Type 'quit' to exit) Enter a city:");
        location = myObj.nextLine();
        if ("quit".equals(location)) {
            System.exit(0);
        }
        if (API_KEY.isEmpty()) {
            System.out.println("Enter your OpenWeatherMap API key:");
            API_KEY = myObj.nextLine();
        }
    }

    public void getData() {
        // Build API request URL with location coordinates
        String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=" + this.location +
                "&limit=1&appid=" + API_KEY;

        getAPIData(urlString, "location");

        try {
            if (locDat != null && locDat.length() > 0) {
                this.lon = locDat.getJSONObject(0).getDouble("lon") + "";
                this.lat = locDat.getJSONObject(0).getDouble("lat") + "";
            } else {
                System.out.println("Error: No location data found.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getWeatherData() {
        // Build API request URL with location coordinates
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon +
                "&appid=" + API_KEY;

        getAPIData(urlString, "weather");
    }

    private void getAPIData(String urlString, String key) {
        try {
            HttpURLConnection conn = callApi(urlString);

            if (conn != null && conn.getResponseCode() == 200) {
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());
                while (scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }
                scanner.close();

                if (key.equals("location")) {
                    locDat = new JSONArray(resultJson.toString());
                    System.out.println("\nLocation Data:\n" + locDat.toString(4));
                } else if (key.equals("weather")) {
                    weatherDat = new JSONObject(resultJson.toString());
                    System.out.println("\nWeather Data:\n" + weatherDat.toString(4));
                }
            } else {
                System.out.println("Error: Could not connect to API. Retrying...");
                getInformation();
                getData();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static HttpURLConnection callApi(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
