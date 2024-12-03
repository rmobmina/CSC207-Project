package presentation.ui.views;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.windows.GraphSelectionWindow;
import presentation.ui.windows.LocationsWindow;
import presentation.visualization.BarGraphWeatherComparison;
import presentation.visualization.LineGraphWeatherComparison;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.ZoneOffset;

import org.json.JSONArray;

public class ForecastHourlyView extends LocationsWindow {

    public static final String OPTION_NAME = "Forecast Hourly";
    private static final String MESSAGE_DIALOGUE_TITLE = "Error";
    private static final int GAP = 10;
    private static final int TEMP_THRESHOLD = 0;
    private static final int NUMBER_HOURS_OF_FORECAST = 8;

    private final OpenWeatherApiService apiService = new OpenWeatherApiService();
    private WeatherData weatherData;
//    private JPanel forecastPanel;

    private final JButton visualizeButton = new JButton("Visualize!");

    public ForecastHourlyView(String name, int[] dimensions, GetLocationDataUseCase locationDataUseCase, String apiKey,
                              ApiService apiService) {
        super(name, dimensions, locationDataUseCase, apiKey, apiService);

        inputPanel.add(visualizeButton);
        visualizeButton.addActionListener(e -> openGraphSelectionWindow());
    }

    @Override
    protected void openVisualization() {
        openGraphSelectionWindow();
    }

    private void openGraphSelectionWindow() {
        if (weatherData == null) {
            JOptionPane.showMessageDialog(this, "No weather data available for visualization!",
                    MESSAGE_DIALOGUE_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        GraphSelectionWindow graphSelectionWindow = new GraphSelectionWindow(graphType -> {
            if ("bar".equals(graphType)) {
                showBarGraph();
            } else if ("line".equals(graphType)) {
                showLineGraph();
            }
        });
        graphSelectionWindow.display();
    }

    private void showBarGraph() {
        BarGraphWeatherComparison barGraph = new BarGraphWeatherComparison("Hourly Forecast Bar Graph");

        try {
            JSONArray hourlyTemperatures = weatherData.getWeatherDetails()
                    .getJSONObject("hourly")
                    .getJSONArray("temperature_2m");

            int utcOffsetSeconds = weatherData.getWeatherDetails().getInt("utc_offset_seconds");
            int utcOffsetHours = utcOffsetSeconds / 3600;

            LocalTime currentUtcTime = LocalTime.now(ZoneOffset.UTC);

            for (int i = 0; i < NUMBER_HOURS_OF_FORECAST; i++) {
                LocalTime forecastLocalTime = currentUtcTime.plusHours(utcOffsetHours + i + 1);
                double temperature = hourlyTemperatures.getDouble(i);

                barGraph.addData("Temperature", forecastLocalTime.getHour() + ":00", temperature);
            }

            barGraph.display();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error displaying bar graph: " + e.getMessage(),
                    "Visualization Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showLineGraph() {
        LineGraphWeatherComparison lineGraph = new LineGraphWeatherComparison("Hourly Forecast Line Graph");

        try {
            JSONArray hourlyTemperatures = weatherData.getWeatherDetails()
                    .getJSONObject("hourly")
                    .getJSONArray("temperature_2m");

            int utcOffsetSeconds = weatherData.getWeatherDetails().getInt("utc_offset_seconds");
            int utcOffsetHours = utcOffsetSeconds / 3600;

            LocalTime currentUtcTime = LocalTime.now(ZoneOffset.UTC);

            for (int i = 0; i < NUMBER_HOURS_OF_FORECAST; i++) {
                LocalTime forecastLocalTime = currentUtcTime.plusHours(utcOffsetHours + i + 1);
                double temperature = hourlyTemperatures.getDouble(i);

                lineGraph.addData("Temperature", forecastLocalTime.getHour() + ":00", temperature);
            }

            lineGraph.display();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error displaying line graph: " + e.getMessage(),
                    "Visualization Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void getWeatherData() {
        if (location == null) {
            throw new RuntimeException("No location selected!!");
        }

        GetForecastWeatherDataUseCase forecastUseCase = new GetForecastWeatherDataUseCase(apiService);
        this.weatherData = forecastUseCase.execute(location, NUMBER_HOURS_OF_FORECAST);
    }    public WeatherData getWeather() {
        getWeatherData();
        return weatherData;
    }

    public WeatherData makeWeatherDataNull() {
        this.weatherData = null;
        return this.weatherData;
    }

    // made this getter method so that the tests can access this value to tests
    public static int getTempThreshold() {

        return TEMP_THRESHOLD;
    }

    // Track the weather details panel so that we can remove the pprevious weather details
    // when entered a new location in the dropdown
    private JPanel forecastpanel;

    public JPanel getForecastPanel() {
        return forecastpanel;
    }

    @Override
    protected void displayWeatherData() {

        if (weatherData == null) {
            throw new RuntimeException("No weather data available to display!");
        }

        if (forecastpanel != null) {
            mainPanel.remove(forecastpanel);
        }

        forecastpanel = new JPanel(new GridLayout(0, 2, GAP, GAP));
        forecastpanel.add(new JLabel("Local Time", SwingConstants.CENTER));
        forecastpanel.add(new JLabel("Temperature (Celsius)", SwingConstants.CENTER));

        JSONArray hourlyTemperatures = weatherData.getWeatherDetails()
                .getJSONObject("hourly")
                .getJSONArray("temperature_2m");

        int utcOffsetSeconds = weatherData.getWeatherDetails().getInt("utc_offset_seconds");
        int utcOffsetHours = utcOffsetSeconds / 3600;
        LocalTime currentUtcTime = LocalTime.now(ZoneOffset.UTC);

        double totalTemp = 0.0;

        for (int i = 0; i < NUMBER_HOURS_OF_FORECAST; i++) {
            LocalTime forecastLocalTime = currentUtcTime.plusHours(utcOffsetHours + i + 1);
            double temperature = hourlyTemperatures.getDouble(i);

            forecastpanel.add(new JLabel(forecastLocalTime.getHour() + ":00", SwingConstants.CENTER));
            forecastpanel.add(new JLabel(String.valueOf(temperature), SwingConstants.CENTER));

            totalTemp += temperature;
        }

        mainPanel.add(forecastpanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        // Calculate and display the average temperature
        final double avgTemp = totalTemp / NUMBER_HOURS_OF_FORECAST;

        // Show a warning pop-up if the average temperature for the next 8 hours is below/equal to the threshold
        if (avgTemp <= TEMP_THRESHOLD) {
            // Cutting the average temperature after one decimal place
            final DecimalFormat onedecimal = new DecimalFormat("#.#");
            final String roundedAvgTemp = onedecimal.format(avgTemp);
            JOptionPane.showMessageDialog(this,
                    "The average temperature for the next 8 hours: " + roundedAvgTemp
                            + "C\n Don't forget to wear warm clothing.", "Temperature Warning",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

