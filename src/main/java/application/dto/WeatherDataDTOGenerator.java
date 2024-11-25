package application.dto;

import domain.entities.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import domain.entities.WeatherData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherDataDTOGenerator {
    public WeatherDataDTO createWeatherDataDTO(WeatherData weatherData, Location location, LocalDate startDate, LocalDate endDate) {
        JSONObject data = weatherData.getWeatherDetails();
        List<LocalDate> dates = new ArrayList<>();
        Map<String, Double> weatherDetails = new HashMap<>();
        Map<String, Double> temperatureHistory = new HashMap<>(); // For historical temperatures
        dates.add(startDate);
        dates.add(endDate);

        try {
            JSONObject dailyData = data.getJSONObject("daily");
            JSONObject hourlyData = data.getJSONObject("hourly");

            // Populate general weather details
            weatherDetails.put("temperature", WeatherDataDTO.getAverageData(hourlyData.getJSONArray("temperature_2m")));
            weatherDetails.put("humidity", WeatherDataDTO.getAverageData(hourlyData.getJSONArray("relative_humidity_2m")));
            weatherDetails.put("windSpeed", dailyData.getJSONArray("wind_speed_10m_max").getDouble(0));
            weatherDetails.put("windDirection", dailyData.getJSONArray("wind_direction_10m_dominant").getDouble(0));
            weatherDetails.put("percipitation", (double) dailyData.getJSONArray("precipitation_sum").getInt(0));

            // Populate temperature history
            JSONArray timeArray = hourlyData.getJSONArray("time");
            JSONArray temperatureArray = hourlyData.getJSONArray("temperature_2m");
            for (int i = 0; i < timeArray.length(); i++) {
                temperatureHistory.put(timeArray.getString(i), temperatureArray.getDouble(i));
            }

            return new WeatherDataDTO(location, dates, weatherDetails, temperatureHistory, new ArrayList<String>());
        } catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
