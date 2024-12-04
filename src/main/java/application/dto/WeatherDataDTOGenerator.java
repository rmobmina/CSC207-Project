package application.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import domain.entities.Location;
import domain.entities.WeatherData;
import utils.Constants;

/**
 * Generates the WeatherDataDTO object to be used by the dashboard and the rest of the application.
 */
public class WeatherDataDTOGenerator {

    private static final int JSON_INDENT_FACTOR = 4;

    /**
     * Creates a WeatherDataDTO object based on the given WeatherData, location, and time interval.
     * It maps relevant weather details and units into the DTO for easy access by the application.
     *
     * @param weatherData The raw weather data object containing details in JSON format.
     * @param location    The location associated with the weather data.
     * @param startDate   The start date of the weather data interval.
     * @param endDate     The end date of the weather data interval.
     * @return A populated WeatherDataDTO object, or null if an error occurs during processing.
     */
    public static WeatherDataDTO createWeatherDataDTO(WeatherData weatherData,
                                                      Location location, LocalDate startDate, LocalDate endDate) {

        final JSONObject data = weatherData.getWeatherDetails();
        final List<LocalDate> dates = new ArrayList<>();
        dates.add(startDate);
        dates.add(endDate);
        try {
            final JSONObject dailyUnits = data.getJSONObject("daily_units");
            final JSONObject hourlyUnits = data.getJSONObject("hourly_units");
            final JSONObject unitsData = new JSONObject();
            unitsData.put(Constants.DAILY_KEY, dailyUnits.toMap());
            unitsData.put(Constants.HOURLY_KEY, hourlyUnits.toMap());

            System.out.println(data.toString(JSON_INDENT_FACTOR));

            final WeatherDataDTO weatherDataDTO = new WeatherDataDTO(location, dates, data, unitsData);
            weatherDataDTO.addWeatherDetail(Constants.HOURLY_KEY, "temperature_2m", "temperatureHourly");
            weatherDataDTO.addWeatherDetail(Constants.HOURLY_KEY, "relative_humidity_2m", "humidityHourly");
            weatherDataDTO.addWeatherDetail(Constants.DAILY_KEY, "temperature_2m_max", "temperatureMaxDaily");
            weatherDataDTO.addWeatherDetail(Constants.DAILY_KEY, "temperature_2m_mean", "temperatureMeanDaily");
            weatherDataDTO.addWeatherDetail(Constants.DAILY_KEY, "temperature_2m_min", "temperatureMinDaily");
            weatherDataDTO.addWeatherDetail(Constants.DAILY_KEY, "precipitation_sum", "percipitationDaily");
            weatherDataDTO.addWeatherDetail(Constants.DAILY_KEY, "wind_speed_10m_max", "windSpeedDaily");
            weatherDataDTO.addWeatherDetail(Constants.DAILY_KEY, "wind_direction_10m_dominant", "windDirectionDaily");

            return weatherDataDTO;
        }
        catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
