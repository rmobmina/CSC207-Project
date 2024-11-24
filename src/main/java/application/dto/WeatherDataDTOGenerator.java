package application.dto;

import domain.entities.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import domain.entities.WeatherData;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Generates the weather data object to be used by the dashboard and rest of application
public class WeatherDataDTOGenerator {
    public static WeatherDataDTO createWeatherDataDTO(WeatherData weatherData, Location location, LocalDate startDate, LocalDate endDate){
        JSONObject data = weatherData.getWeatherDetails();
        List<LocalDate> dates = new ArrayList<>();
        Map<String, JSONArray> weatherDetails  = new HashMap<>();
        dates.add(startDate);
        dates.add(endDate);
        try {
            JSONObject dailyData = data.getJSONObject("daily");
            JSONObject hourlyData = data.getJSONObject("hourly");
            weatherDetails.put("temperature", hourlyData.getJSONArray("temperature_2m"));
            weatherDetails.put("humidity", hourlyData.getJSONArray("relative_humidity_2m"));
            weatherDetails.put("windSpeed", dailyData.getJSONArray("wind_speed_10m_max"));
            weatherDetails.put("windDirection", dailyData.getJSONArray("wind_direction_10m_dominant"));
            weatherDetails.put("percipitation", dailyData.getJSONArray("precipitation_sum"));
            return new WeatherDataDTO(location, dates, weatherDetails, new ArrayList<String>(){} );
            //NOTE: Don't have alerts (for now)
        }
        catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
