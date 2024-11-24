package application.dto;

import domain.entities.Location;
import org.json.JSONArray;
import org.json.JSONException;
import utils.Constants;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class WeatherDataDTO {
    public Location location;
    public LocalDate startDate;
    public LocalDate endDate;
    // These represent all the weather details over the given time interval as lists by category of data
    public JSONArray temperature;
    public JSONArray humidity;
    public JSONArray windSpeed;
    public JSONArray windDirection;
    public JSONArray precipitation;
    public List<String> alerts;

    // Constructor for mapping from domain entities
    public WeatherDataDTO(Location location, List<LocalDate> timeInterval, Map<String, JSONArray> weatherDetails, List<String> alerts) {

        this.location = location;
        this.startDate = timeInterval.get(0);
        this.endDate = timeInterval.get(timeInterval.size() - 1);
        this.temperature = weatherDetails.get("temperature");
        this.humidity = weatherDetails.get("humidity");
        this.windSpeed = weatherDetails.get("windSpeed");
        this.windDirection = weatherDetails.get("windDirection");
        this.precipitation = weatherDetails.get("percipitation");
        this.alerts = alerts;
    }

    public double getTemperature(String type, int index) {
        switch (type) {
            case "kel":
                return getTemperatureInKelvin(index);
            case "fah":
                return getTemperatureInFahrenheit(index);
            default:
                return temperature.getDouble(index);
        }
    }

    public double getTemperatureInKelvin(int index) {
        return this.temperature.getDouble(index) + Constants.CELSIUS_TO_KELVIN_OFFSET;
    }

    public double getTemperatureInFahrenheit(int index) {
        return this.temperature.getDouble(index) * Constants.CELSIUS_TO_FAHRENHEIT_FACTOR + Constants.CELSIUS_TO_FAHRENHEIT_OFFSET;
    }

    /**
     * Returns the average of all the data values in the JSONArray (assuming values are int or double)
     * @param dataArr JSONArray of values (either double or int)
     * @return avaerage of all data values in dataArr
     * @throws org.json.JSONException if some error occurs
     */
    public static double getAverageData(JSONArray dataArr) throws JSONException {
        double sum = 0;
        for (int i = 0; i < dataArr.length(); i++) {
            Object dataElement = dataArr.get(i);
            if (dataElement instanceof Double) {
                sum += (Double) dataElement;
            }
            else if (dataElement instanceof Integer) {
                sum += (Integer) dataElement;
            }
        }
        return sum / dataArr.length();
    }

}
