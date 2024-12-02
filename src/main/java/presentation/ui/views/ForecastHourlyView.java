package presentation.ui.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.ZoneOffset;

import javax.swing.*;

import org.json.JSONArray;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.windows.LocationsWindow;

/**
 * This class displays the hourly forecast for the next 8 hours for a given location.
 * If the temperature is below a certain degree (Celcius),
 * it gives a pop-up reminder to wear multiple layers of clothing (can add more features later).
 */
public class ForecastHourlyView extends LocationsWindow {
    private final OpenWeatherApiService apiService = new OpenWeatherApiService();
    private static final String MESSAGE_DIALOGUE_TITLE = "Error";
    private static final int GAP = 10;
    private static final int TEMP_THRESHOLD = 0;
    private static final int NUMBER_HOURS_OF_FORECAST = 8;
    public WeatherData weatherData;
    public static final String OPTION_NAME = "Forecast Hourly";

    public ForecastHourlyView(String name, int[] dimensions, GetLocationDataUseCase locationDataUseCase, String apiKey,
                              ApiService apiService) {
        super(name, dimensions, locationDataUseCase, apiKey, apiService);
    }

    @Override
    protected void getWeatherData() {
        if (location == null) {
            JOptionPane.showMessageDialog(this, "No location selected!",
                    MESSAGE_DIALOGUE_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Fetchhourly forecast data for the selected location
        final GetForecastWeatherDataUseCase forecastUseCase = new GetForecastWeatherDataUseCase(apiService);
        this.weatherData = forecastUseCase.execute(location, NUMBER_HOURS_OF_FORECAST);
    }

    @Override
    protected void displayWeatherData() {
        if (weatherData == null) {
            JOptionPane.showMessageDialog(this, "No weather data available to display!",
                    MESSAGE_DIALOGUE_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Creating or updating a panel to display forecast data
        JPanel forecastPanel = new JPanel(new GridLayout(0, 2, GAP, GAP));

        forecastPanel.add(new JLabel("Local Time", SwingConstants.CENTER));
        forecastPanel.add(new JLabel("Temperature (Celsius)", SwingConstants.CENTER));

        // Extracting hourly temperature data
        final JSONArray hourlyTemperatures = weatherData.getWeatherDetails().getJSONObject("hourly")
                .getJSONArray("temperature_2m");

        // Extracting UTC offset from weather data
        final int utcOffsetSeconds = weatherData.getWeatherDetails().getInt("utc_offset_seconds");
        final int utcOffsetHours = utcOffsetSeconds / 3600;

        // Getting current UTC time
        final LocalTime currentUtcTime = LocalTime.now(ZoneOffset.UTC);

        double totalTemp = 0.0;

        // Displaying hourly data
        for (int i = 0; i < NUMBER_HOURS_OF_FORECAST; i++) {
            // Adjust current UTC time to local time
            final LocalTime forecastLocalTime = currentUtcTime.plusHours(utcOffsetHours + i + 1);

            // Get temperature for this hour
            final double temperature = hourlyTemperatures.getDouble(i);

            forecastPanel.add(new JLabel(String.valueOf(forecastLocalTime.getHour()) + ":00", SwingConstants.CENTER));
            forecastPanel.add(new JLabel(String.valueOf(temperature), SwingConstants.CENTER));

            // Calculating the total temp for avg
            totalTemp += temperature;
        }

        // Add forecast panel below the input fields
        mainPanel.add(forecastPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        // Calculate and display the average temperature
        final double avgTemp = totalTemp / NUMBER_HOURS_OF_FORECAST;

        // Show a warning pop-up if the average temperature for the next 8 hours is below/equal to the threshold
        if (avgTemp <= TEMP_THRESHOLD) {
            // cutting the average temperature after one decimal place.
            final DecimalFormat onedecimal = new DecimalFormat("#.#");
            final String roundedAvgTemp = onedecimal.format(avgTemp);
            JOptionPane.showMessageDialog(this,
                    "The average temperature for the next 8 hours: " + roundedAvgTemp
                            + "°C!\n" + "Don't forget to wear warm clothing.",
                    "Temperature Warning", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
