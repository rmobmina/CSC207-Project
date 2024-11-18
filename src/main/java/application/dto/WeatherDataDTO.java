package application.dto;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.LocalDate;
import java.util.List;

public class WeatherDataDTO {
    public String location;
    public LocalDate startDate;
    public LocalDate endDate;
    public double temperature;
    public int humidity;
    public double windSpeed;
    public double precipitation;
    public List<String> alerts;

    // Constructor for mapping from domain entities
    public WeatherDataDTO(String location, LocalDate startDate, LocalDate endDate, double temperature, int humidity, double windSpeed,
                          double precipitation, List<String> alerts) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.precipitation = precipitation;
        this.alerts = alerts;
    }

    //Alternate constuctor for a single date for this location
    public WeatherDataDTO(String location, LocalDate currentDate, double temperature, int humidity, double windSpeed,
                          double precipitation, List<String> alerts) {
        this.location = location;
        this.startDate = currentDate;
        this.endDate = currentDate;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.precipitation = precipitation;
        this.alerts = alerts;
    }

    public WeatherDataDTO() { this.location = "n/a"; }

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
        return this.temperature + 273.15;
    }

    public double getTemperatureInFahrenheit() {
        return this.temperature * 1.8 + 32;
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
