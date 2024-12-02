package application.dto;

import domain.entities.Location;
import org.json.JSONException;
import org.json.JSONObject;
import domain.entities.WeatherData;
import static utils.Constants.DAILY;
import static utils.Constants.HOURLY;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


// Generates the weather data object to be used by the dashboard and the rest of the application
public class WeatherDataDTOGenerator {
    public static WeatherDataDTO createWeatherDataDTO(WeatherData weatherData, Location location, LocalDate startDate, LocalDate endDate){
        JSONObject data = weatherData.getWeatherDetails();
        List<LocalDate> dates = new ArrayList<>();
        dates.add(startDate);
        dates.add(endDate);
        try {
            JSONObject dailyUnits = data.getJSONObject("daily_units");
            JSONObject hourlyUnits = data.getJSONObject("hourly_units");
            JSONObject unitsData = new JSONObject();
            unitsData.put(DAILY, dailyUnits.toMap());
            unitsData.put(HOURLY, hourlyUnits.toMap());

            System.out.println(unitsData.toString(4));

            WeatherDataDTO weatherDataDTO = new WeatherDataDTO(location, dates, data, unitsData);
            weatherDataDTO.addWeatherDetail(HOURLY, "temperature_2m", "temperatureHourly");
            weatherDataDTO.addWeatherDetail(HOURLY, "relative_humidity_2m", "humidityHourly");
            weatherDataDTO.addWeatherDetail(DAILY, "temperature_2m_max", "temperatureMaxDaily");
            weatherDataDTO.addWeatherDetail(DAILY, "temperature_2m_mean", "temperatureMeanDaily");
            weatherDataDTO.addWeatherDetail(DAILY, "temperature_2m_min", "temperatureMinDaily");
            weatherDataDTO.addWeatherDetail(DAILY, "precipitation_sum", "percipitationDaily");
            weatherDataDTO.addWeatherDetail(DAILY, "wind_speed_10m_max", "windSpeedDaily");
            weatherDataDTO.addWeatherDetail(DAILY, "wind_direction_10m_dominant", "windDirectionDaily");

            return weatherDataDTO;
        }
        catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
