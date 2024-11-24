package presentation.visualization;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WeatherHeatMap {
    public static void main(String[] args) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String apiKey = "2d6d6124e844c3e976842b19833dfa3b";

        // List of cities to include in the heatmap
        List<String> cities = Arrays.asList("London", "Paris", "Berlin", "New York", "Tokyo");
        StringBuilder heatmapData = new StringBuilder("latitude,longitude,temperature\n");

        for (String city : cities) {
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey
                    + "&units=metric";

            Request request = new Request.Builder().url(url).build();
            try (Response response = client.newCall(request).execute()) {
                String jsonData = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();

                Object test = jsonObject.toString();

                // Extract relevant data
                double lat = jsonObject.getAsJsonObject("coord").get("lat").getAsDouble();
                double lon = jsonObject.getAsJsonObject("coord").get("lon").getAsDouble();
                double temp = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();

                // Append to CSV for visualization
                heatmapData.append(lat).append(",").append(lon).append(",").append(temp).append("\n");
                System.out.println("Fetched data for " + city + ": Temp = " + temp + "°C");
            }
        }

        // Write the data to a CSV file
        try (FileWriter writer = new FileWriter("heatmap_data.csv")) {
            writer.write(heatmapData.toString());
            System.out.println("Heatmap data saved to heatmap_data.csv");
        }
    }
}
