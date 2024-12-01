package presentation.ui;

import domain.entities.Location;
import domain.entities.WeatherData;
import org.json.JSONException;
import org.json.JSONObject;

public class MockWeatherDataProvider {
    private static final String TEST_CITY_NAME = "TestCity";

    public static boolean isTestCity(Location location) {
        return TEST_CITY_NAME.equalsIgnoreCase(location.getCity());
    }

    public static WeatherData getMockWeatherData() throws JSONException {
        // Mock JSON data for TestCity
        JSONObject mockWeatherDetails = new JSONObject();
        mockWeatherDetails.put("temp", 4.0);
        mockWeatherDetails.put("humidity", 83);
        mockWeatherDetails.put("wind_speed", 4.45);
        mockWeatherDetails.put("weather", new JSONObject[] {
                new JSONObject().put("description", "Heavy Rain").put("icon", "10d")
        });

        return new WeatherData(mockWeatherDetails);
    }

    public static String getMockSevereAlert() {
        return "Flood (check local news for more information)";
    }
}
