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

    // Weather details over the given time interval
    private Map<String, JSONArray> weatherDetails = new HashMap<>();
    private Map<String, String> weatherUnits = new HashMap<>();

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

    public JSONArray getWeatherData(String category) {
        return weatherDetails.get(category);
    }

    /**
     * Safely retrieves a weather data value at a given index for a specific category.
     * If the value is null or the index is out of bounds, it returns null.
     *
     * @param category The weather detail category (e.g., "temperatureMeanDaily").
     * @param index    The index of the value in the array.
     * @return The numeric value, or null if the data is invalid.
     */
    public Double getWeatherDataValue(String category, int index) {
        JSONArray dataArray = getWeatherData(category);
        if (dataArray == null || index >= dataArray.length() || dataArray.isNull(index)) {
            return null; // Return null for missing or invalid data
        }
        return dataArray.getDouble(index);
    }

    /**
     * Gets the unit for a specific weather detail category.
     *
     * @param category The category of a weather detail.
     * @return The units of that category.
     */
    public String getWeatherUnit(String category) {
        return weatherUnits.get(category);
    }

    /**
     * Converts a weather detail value to a string formatted with its unit.
     *
     * @param weatherDetail The weather detail (e.g., "temperatureMeanDaily").
     * @param index         The index of the value in the array.
     * @return The formatted string.
     */
    public String dataToString(String weatherDetail, int index) {
        return dataToString(weatherDetail, index, weatherUnits.get(weatherDetail));
    }

    public String dataToString(String category, int index, String units) {
        Double value = getWeatherDataValue(category, index);
        return value != null ? value + " " + units : "N/A";
    }

    /**
     * Adds a new weather detail to the internal storage.
     *
     * @param category          The category in the JSONObject (e.g., "daily").
     * @param weatherDetailKey  The key mapping to a specific detail (e.g., "temperature_2m_mean").
     * @param key               The internal storage key (e.g., "temperatureMeanDaily").
     */
    public void addWeatherDetail(String category, String weatherDetailKey, String key) {
        weatherDetails.put(key, weatherDetailsObject.getJSONObject(category).getJSONArray(weatherDetailKey));
        weatherUnits.put(key, unitsObject.getJSONObject(category).getString(weatherDetailKey));
    }

    /**
     * Retrieves the temperature value at a given index and converts it if necessary.
     *
     * @param temperatureCategory The category of the temperature data.
     * @param index               The index of the temperature value.
     * @param unitType            The desired unit type ("cel", "kel", or "fah").
     * @return The temperature value.
     */
    public double getTemperature(String temperatureCategory, int index, String unitType) {
        Double value = getWeatherDataValue(temperatureCategory, index);
        if (value == null) {
            return Double.NaN; // Return NaN for missing or invalid data
        }

        switch (unitType) {
            case "kel":
                return getTemperatureInKelvin(value);
            case "fah":
                return getTemperatureInFahrenheit(value);
            default:
                return value;
        }
    }

    public String temperatureToString(String temperatureCategory, int index, String unitType) {
        double temperature = getTemperature(temperatureCategory, index, unitType);
        return !Double.isNaN(temperature) ? temperature + " " + weatherUnits.get(temperatureCategory) : "N/A";
    }

    private double getTemperatureInKelvin(double temperature) {
        return temperature + Constants.CELSIUS_TO_KELVIN_OFFSET;
    }

    private double getTemperatureInFahrenheit(double temperature) {
        return temperature * Constants.CELSIUS_TO_FAHRENHEIT_FACTOR + Constants.CELSIUS_TO_FAHRENHEIT_OFFSET;
    }

    /**
     * Retrieves the temperature history as a map of dates to temperature values.
     * Invalid or missing data is skipped.
     *
     * @return A map where keys are dates (as strings) and values are temperatures.
     */
    public Map<String, Double> getTemperatureHistory() {
        LocalDate startDate = getTimeInterval().get(0);
        LocalDate endDate = getTimeInterval().get(1);
        Map<String, Double> temperatureHistory = new HashMap<>();

        long numberOfDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

        for (int i = 0; i < numberOfDays; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            double temperature = getTemperature("temperatureMeanDaily", i, Constants.CELCIUS_UNIT_TYPE);
            if (!Double.isNaN(temperature)) { // Only include valid data
                temperatureHistory.put(currentDate.toString(), temperature);
            }
        }

        return temperatureHistory;
    }

    /**
     * Calculates the average of a given weather detail.
     *
     * @param weatherDetail The weather detail key.
     * @return The average value.
     */
    public double getAverageWeatherData(String weatherDetail) {
        JSONArray weatherDataArray = getWeatherData(weatherDetail);
        return WeatherDataDTO.getAverageData(weatherDataArray);
    }

    /**
     * Calculates the average value of a JSONArray containing numeric values.
     *
     * @param dataArr JSONArray of values (either Double or Integer).
     * @return The average value.
     * @throws JSONException If an error occurs.
     */
    public static double getAverageData(JSONArray dataArr) throws JSONException {
        double sum = 0;
        int count = 0;
        for (int i = 0; i < dataArr.length(); i++) {
            if (!dataArr.isNull(i)) {
                Object dataElement = dataArr.get(i);
                if (dataElement instanceof Double) {
                    sum += (Double) dataElement;
                } else if (dataElement instanceof Integer) {
                    sum += (Integer) dataElement;
                }
                count++;
            }
        }
        return count > 0 ? sum / count : Double.NaN; // Avoid division by zero
    }
}
