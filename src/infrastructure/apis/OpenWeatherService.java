package infrastructure.apis;

import java.net.HttpURLConnection;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.io.IOException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OpenWeatherService<JSONObject> {
    private static final String API_KEY = "Enter you API Key here.";
    private String lon;
    private String lat;
    private String location = "";
    private JSONArray locDat;

    public OpenWeatherService(String location) {
        this.location = location;
        getData();

    }

    //    public static <JSONObject, JSONArray> JSONObject getWeatherData(String locationName) {
    public void getData() {

        // build API request URL with location coordinates
        String urlString = "http://api.openweathermap.org/geo/1.0/direct?q=" + this.location +
                "&limit=5&appid=" + API_KEY;

        getAPIData(urlString);

        try {
            this.lon = locDat.getJSONObject(0).getDouble("lon") + "";
            this.lat = locDat.getJSONObject(0).getDouble("lat") + "";
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String key) {
        // (Switchs statements regarding keys)
        // build API request URL with location coordinates
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon +
                "&appid=" + API_KEY;

        getAPIData(urlString);

    }

    private void getAPIData(String urlString) {
        try {
            HttpURLConnection conn = callApi(urlString);

            // catch errors; the call is a success IFF the response code is 200
            assert conn != null;
            if(conn.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
            }

            // store resulting json data
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while(scanner.hasNext()){
                // read and store into the string builder
                resultJson.append(scanner.nextLine());
            }

            scanner.close();
            locDat = new JSONArray(resultJson.toString());
            System.out.println(locDat.toString());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static HttpURLConnection callApi(String urlString){
        try{
            // attempt to create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to get
            conn.setRequestMethod("GET");

            // connect to our API
            conn.connect();
            return conn;
        }catch(IOException e){
            e.printStackTrace();
        }

        // could not make connection
        return null;
    }


}
