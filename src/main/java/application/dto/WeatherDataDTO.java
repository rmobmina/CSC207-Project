package application.dto;

import domain.entities.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherDataDTO {
    private Location location;
    private LocalDate startDate;
    private LocalDate endDate;
    // These represent all the weather details over the given time interval as JSONArrays by category of data
    private Map<String, JSONArray> weatherDetails = new HashMap<>();
    private Map<String, String> weatherUnits = new HashMap<>();
    // Represents the units for each weather detail
    private JSONObject unitsObject;
    private JSONObject weatherDetailsObject;

    // Constructor for mapping from domain entities
    public WeatherDataDTO(Location location, List<LocalDate> timeInterval, JSONObject weatherDetailsObject, JSONObject unitsObject) {
        this.location = location;
        this.startDate = timeInterval.get(0);
        this.endDate = timeInterval.get(timeInterval.size() - 1);
        this.weatherDetailsObject = weatherDetailsObject;
        this.unitsObject = unitsObject;
    }

    public Location getLocation() {
        return location;
    }

    public List<LocalDate> getTimeInterval() {
        List<LocalDate> timeInterval = new ArrayList<>();
        timeInterval.add(startDate);
        timeInterval.add(endDate);
        return timeInterval;
    }

    public JSONArray getWeatherData(String category){
        return weatherDetails.get(category);
    }

    public Double getWeatherDataValue(String category, int index){
        return getWeatherData(category).getDouble(index);
    }

    /**
     * Gets the specific unit of the weather detail category
     * @param category The category of a weather detail
     * @return the units of that category
     */
    public String getWeatherUnit(String category){
        return weatherUnits.get(category);
    }

    /**
     * Returns a string representation of a data value (given the data category and index) using its corresponding units
     * @param weatherDetail The weatherDetail that the data belongs to in weatherDetails
     * @param index The index of the JSONArray representing an instance of weatherDetail at a point in time
     * @return a String representation of the data value formmated with its units
     */
    public String dataToString(String weatherDetail, int index) {
        return dataToString(weatherDetail, index, weatherUnits.get(weatherDetail));
    }

    public String dataToString(String category, int index, String units) {
        return weatherDetails.get(category).get(index) + " " + units;
    }

    /**
     * Adds a new weather detail from a data object mapped to a given key in weatherDeatils
     * @param category the category in the JSONObject that the new weather detail belongs in
     * @param weatherDetailKey a key that maps to a JSONArray, which represents a specific weather detail
     * @param key the key that maps to the associated weather detail in weatherDetails
     */
    public void addWeatherDetail(String category, String weatherDetailKey, String key){
        weatherDetails.put(key, weatherDetailsObject.getJSONObject(category).getJSONArray(weatherDetailKey));
        weatherUnits.put(key, unitsObject.getJSONObject(category).getString(weatherDetailKey));
    }

    public double getTemperature(String temperatureCategory, int index, String unitType) {
        switch (unitType) {
            case "kel":
                return getTemperatureInKelvin(getWeatherDataValue(temperatureCategory, index));
            case "fah":
                return getTemperatureInFahrenheit(getWeatherDataValue(temperatureCategory, index));
            default:
                return getWeatherDataValue(temperatureCategory, index);
        }
    }

    public String temperatureToString(String temperatureCategory, int index, String unitType) {
        return getTemperature(temperatureCategory, index, unitType) + " " + weatherUnits.get(temperatureCategory);
    }

    private double getTemperatureInKelvin(double temperature) {
        return temperature + Constants.CELSIUS_TO_KELVIN_OFFSET;
    }

    private double getTemperatureInFahrenheit(double temperature) {
        return temperature * Constants.CELSIUS_TO_FAHRENHEIT_FACTOR + Constants.CELSIUS_TO_FAHRENHEIT_OFFSET;
    }

    /**
     * Returns the average of all the data values in the JSONArray (assuming values are int or double)
     * @param dataArr JSONArray of values (either double or int)
     * @return avaerage of all data values in dataArr
     * @throws JSONException if some error occurs
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
