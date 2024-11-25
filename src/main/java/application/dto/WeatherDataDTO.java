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
    public double temperature;
    public int humidity;
    public double windSpeed;
    public double windDirection;
    public double precipitation;
    public List<String> alerts;

    private Map<String, Double> temperatureHistory; // New field for temperature history

    // Constructor for mapping from domain entities
    public WeatherDataDTO(Location location, List<LocalDate> timeInterval, Map<String, Double> weatherDetails, Map<String, Double> temperatureHistory, List<String> alerts) {
        this.location = location;
        this.startDate = timeInterval.get(0);
        this.endDate = timeInterval.get(timeInterval.size() - 1);
        this.temperature = weatherDetails.get("temperature");
        this.humidity = weatherDetails.get("humidity").intValue();
        this.windSpeed = weatherDetails.get("windSpeed");
        this.windDirection = weatherDetails.get("windDirection");
        this.precipitation = weatherDetails.get("percipitation");
        this.alerts = alerts;
        this.temperatureHistory = temperatureHistory; // Initialize temperature history
    }

    public double getTemperature(String type) {
        switch (type) {
            case "kel":
                return getTemperatureInKelvin();
            case "fah":
                return getTemperatureInFahrenheit();
            default:
                return temperature;
        }
    }

    public double getTemperatureInKelvin() {
        return this.temperature + Constants.CELSIUS_TO_KELVIN_OFFSET;
    }

    public double getTemperatureInFahrenheit() {
        return this.temperature * Constants.CELSIUS_TO_FAHRENHEIT_FACTOR + Constants.CELSIUS_TO_FAHRENHEIT_OFFSET;
    }

    /**
     * Returns the temperature history.
     * @return Map<String, Double> where keys are dates and values are temperatures.
     */
    public Map<String, Double> getTemperatureHistory() {
        return temperatureHistory;
    }

    /**
     * Returns the average of all the data values in the JSONArray (assuming values are int or double)
     * @param dataArr JSONArray of values (either double or int)
     * @return average of all data values in dataArr
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
