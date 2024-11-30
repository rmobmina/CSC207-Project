package presentation.ui.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.windows.LocationsWindow;

/**
 * This class displays the hourly forecast for the next 8 hours for a given location.
 * If the temperature is below -24 degree Celcius,
 * it gives a pop-up reminder to wear multiple layers of clothing (can add more features later).
 */

public class ForecastHourlyView extends LocationsWindow {
    public static final String OPTION_NAME = "Forecast Hourly";

    public ForecastHourlyView(String name, int width, int height) {
        super(name, width, height);
    }

    /**
     * Displays the hourly forecast of the city for the next 8 hours.
     * @param city the name of the city you want to see the forecast of.
     */
    public void addForecastPanel(String city) {
        // Clear any previous content
        final String messageDialogueTitle = "Error";
        final int gap = 10;
        final int tempThreshold = -24;
        final int numberHoursofForecast = 8;
        this.getContentPane().removeAll();

        // Create the panel for displaying forecast data
        final JPanel forecastPanel = new JPanel();
        // Two columns: Hour and Temperature
        forecastPanel.setLayout(new GridLayout(0, 2, gap, gap));

        // Add column headers
        forecastPanel.add(new JLabel("Hour", SwingConstants.CENTER));
        forecastPanel.add(new JLabel("Temperature (Celsius)", SwingConstants.CENTER));

        // Initialize API service and fetching the location
        final OpenWeatherApiService apiService = new OpenWeatherApiService();
        final GetLocationDataUseCase locationUseCase = new GetLocationDataUseCase(apiService);
        final List<Location> possibleLocations = locationUseCase.execute(city, "0e85f616a96a624a0bf65bad89ff68c5");

        if (city == null || city.trim().isEmpty() || possibleLocations.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Location Not Found", messageDialogueTitle,
                    JOptionPane.ERROR_MESSAGE);

        }

        final Location chosenLocation = possibleLocations.get(0);

        // Fetch weather data for the location
        final GetForecastWeatherDataUseCase forecastUseCase = new GetForecastWeatherDataUseCase(apiService);
        final WeatherData weatherData = forecastUseCase.execute(chosenLocation, numberHoursofForecast);

        // Extract hourly temperature data
        final JSONObject weatherDetails = weatherData.getWeatherDetails();

        final JSONObject hourlyData = weatherDetails.getJSONObject("hourly");
        if (!hourlyData.has("temperature_2m")) {
            JOptionPane.showMessageDialog(this, "Hourly temperature data is unavailable!", messageDialogueTitle,
                    JOptionPane.ERROR_MESSAGE);
        }

        // Getting the hourly temperatures
        final JSONArray temperatureData = hourlyData.getJSONArray("temperature_2m");

        // Populate data for up to 8 hours
        for (int i = 0; i < numberHoursofForecast; i++) {
            forecastPanel.add(new JLabel("Hour " + (i + 1), SwingConstants.CENTER));
            forecastPanel.add(new JLabel(String.format("%.1f", temperatureData.getDouble(i)), SwingConstants.CENTER));

            // If the temp is less than the variable "TempThreshold", we get a pop-up reminder to wear multiple layers
            if (temperatureData.getDouble(i) < tempThreshold) {
                JOptionPane.showMessageDialog(this, "Hour " + (i + 1) + ": Don't forget to wear multiple layers!",
                        "Reminder", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        // Add the panel to the frame
        this.getContentPane().add(forecastPanel, BorderLayout.CENTER);
    }

    /**
     * Main method to test if the file is running as it's supposed to.
     * Used Regina as a sample city since the Dropdown UI is not working as of right now, so this is,
     * therefore I pass the location to the functions using this main method.
     *
     * @param args Command-line arguments.
     */

    public static void main(String[] args) {
        // Example usage
        final ForecastHourlyView view = new ForecastHourlyView(OPTION_NAME, 800, 600);

        // Displaying the frame
        view.addForecastPanel("Regina");
        view.setVisible(true);
    }
}
