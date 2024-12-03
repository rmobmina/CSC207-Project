package application.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import domain.entities.Location;
import utils.Constants;

/**
 * Data Transfer Object (DTO) for weather data.
 * Encapsulates weather details, units, and relevant operations for a specific location and time interval.
 */
public class WeatherDataDTO {
    private final Location location;
    private final LocalDate startDate;
    private final LocalDate endDate;

    // Weather details over the given time interval
    private final Map<String, JSONArray> weatherDetails = new HashMap<>();
    private final Map<String, String> weatherUnits = new HashMap<>();

    private final JSONObject unitsObject;
    private final JSONObject weatherDetailsObject;

    /**
     * Constructor for creating a WeatherDataDTO object.
     *
     * @param location             The location for which the weather data applies.
     * @param timeInterval         A list containing the start and end dates of the time interval.
     * @param weatherDetailsObject The JSON object containing the weather details.
     * @param unitsObject          The JSON object containing the weather detail units.
     */
    public WeatherDataDTO(Location location, List<LocalDate> timeInterval,
                          JSONObject weatherDetailsObject, JSONObject unitsObject) {
        this.location = location;
        this.startDate = timeInterval.get(0);
        this.endDate = timeInterval.get(timeInterval.size() - 1);
        this.weatherDetailsObject = weatherDetailsObject;
        this.unitsObject = unitsObject;
    }

    /**
     * Gets the location associated with this weather data.
     *
     * @return The location object.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the time interval for which the weather data applies.
     *
     * @return A list containing the start and end dates of the time interval.
     */
    public List<LocalDate> getTimeInterval() {
        final List<LocalDate> timeInterval = new ArrayList<>();
        timeInterval.add(startDate);
        timeInterval.add(endDate);
        return timeInterval;
    }

    /**
     * Retrieves weather data for a specific category.
     *
     * @param category The weather detail category (e.g., "temperatureMeanDaily").
     * @return A JSONArray containing weather data for the category.
     */
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
        final JSONArray dataArray = getWeatherData(category);
        if (dataArray == null || index >= dataArray.length() || dataArray.isNull(index)) {
            return null;
        }
        return dataArray.getDouble(index);
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

    /**
     * Converts a weather detail value to a string formatted with its unit.
     *
     * @param category The weather detail category.
     * @param index    The index of the value in the array.
     * @param units    The unit of the weather detail.
     * @return The formatted string.
     */
    public String dataToString(String category, int index, String units) {
        final Double value = getWeatherDataValue(category, index);
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
        final Double value = getWeatherDataValue(temperatureCategory, index);
        if (value == null) {
            return Double.NaN;
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

    /**
     * Returns a string representation of the temperature given a specific category and unit.
     *
     * @param temperatureCategory The specific category of temperature (e.g., "mean").
     * @param index               The index representing the data value at a specific point in time.
     * @param unitType            The type of temperature unit (e.g., "cel", "kel", "fah").
     * @return A string representation of the temperature.
     */
    public String temperatureToString(String temperatureCategory, int index, String unitType) {
        final double temperature = getTemperature(temperatureCategory, index, unitType);
        return !Double.isNaN(temperature) ? temperature + " "
                +
                getTemperatureUnit(temperatureCategory, unitType) : "N/A";
    }

    /**
     * Returns the string representing the temperature unit given unitType.
     *
     * @param temperatureCategory The temperature category.
     * @param unitType            The unit type (e.g., "cel", "kel", "fah").
     * @return The temperature unit as a string.
     */
    public String getTemperatureUnit(String temperatureCategory, String unitType) {
        switch (unitType) {
            case Constants.FAHRENHEIT_UNIT_TYPE:
                return Constants.FAHRENHEIT_UNIT;
            case Constants.KELVIN_UNIT_TYPE:
                return Constants.KELVIN_UNIT;
            default:
                return weatherUnits.get(temperatureCategory);
        }
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
        final LocalDate intervalStartDate = getTimeInterval().get(0);
        final LocalDate intervalEndDate = getTimeInterval().get(1);
        final Map<String, Double> temperatureHistory = new HashMap<>();

        final long numberOfDays = java.time.temporal.ChronoUnit.DAYS.between(intervalStartDate, intervalEndDate) + 1;

        for (int i = 0; i < numberOfDays; i++) {
            final LocalDate currentDate = intervalStartDate.plusDays(i);
            final double temperature = getTemperature("temperatureMeanDaily", i, Constants.CELCIUS_UNIT_TYPE);
            if (!Double.isNaN(temperature)) {
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
        final JSONArray weatherDataArray = getWeatherData(weatherDetail);
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
                final Object dataElement = dataArr.get(i);
                if (dataElement instanceof Double) {
                    sum += (Double) dataElement;
                }
                else if (dataElement instanceof Integer) {
                    sum += (Integer) dataElement;
                }
                count++;
            }
        }
        return count > 0 ? sum / count : Double.NaN;
    }
}
