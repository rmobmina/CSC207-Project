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
        this.getContentPane().removeAll();

        // Create the panel for displaying forecast data
        JPanel forecastPanel = new JPanel();
        forecastPanel.setLayout(new GridLayout(0, 2, 10, 10)); // Two columns: Hour and Temperature

        // Add column headers
        forecastPanel.add(new JLabel("Hour", SwingConstants.CENTER));
        forecastPanel.add(new JLabel("Temperature (Celsius)", SwingConstants.CENTER));

            if (city == null || city.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "City name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Initialize API service and fetching the location
            OpenWeatherApiService apiService = new OpenWeatherApiService();
            GetLocationDataUseCase locationUseCase = new GetLocationDataUseCase(apiService);
            List<Location> possibleLocations = locationUseCase.execute(city, "0e85f616a96a624a0bf65bad89ff68c5");

            if (possibleLocations.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No location found for the given city!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Location chosenLocation = possibleLocations.get(0);

            // Fetch weather data for the location
            GetForecastWeatherDataUseCase forecastUseCase = new GetForecastWeatherDataUseCase(apiService);
            WeatherData weatherData = forecastUseCase.execute(chosenLocation, 8); // Fetching 8 hours of forecast

        // Extract hourly temperature data
            JSONObject weatherDetails = weatherData.getWeatherDetails();
            // debuggingg
            System.out.println(weatherDetails.toString());
            if (weatherDetails == null || !weatherDetails.has("hourly")) {
                JOptionPane.showMessageDialog(this, "Hourly data is unavailable!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JSONObject hourlyData = weatherDetails.getJSONObject("hourly");
            if (!hourlyData.has("temperature_2m")) {
                JOptionPane.showMessageDialog(this, "Hourly temperature data is unavailable!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JSONArray temperatureData = hourlyData.getJSONArray("temperature_2m"); // Getting the hourly temperatures

            // Populate data for up to 8 hours
            for (int i = 0; i <8; i++) {
                forecastPanel.add(new JLabel("Hour " + (i + 1), SwingConstants.CENTER));
                forecastPanel.add(new JLabel(String.format("%.1f", temperatureData.getDouble(i)), SwingConstants.CENTER));
                // If the temperature is less than negative 24 Celcius, we get a pop up reminder with the message
                if (temperatureData.getDouble(i) < -24) {
                    JOptionPane.showMessageDialog(this, "Hour " + (i + 1) + ": Don't forget to wear multiple layers!", "Reminder", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

        // Add the panel to the frame
        this.getContentPane().add(forecastPanel, BorderLayout.CENTER);
    }


    public static void main(String[] args) {
        // Example usage
        ForecastHourlyView view = new ForecastHourlyView("Forecast Hourly", 800, 600);

        // Display the frame
        view.addForecastPanel("Regina");
        view.setVisible(true);
    }
}
