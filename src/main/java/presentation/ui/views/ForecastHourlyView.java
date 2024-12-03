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
    private JPanel forecastPanel;

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
            JOptionPane.showMessageDialog(this, "No location selected!",
                    MESSAGE_DIALOGUE_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        GetForecastWeatherDataUseCase forecastUseCase = new GetForecastWeatherDataUseCase(apiService);
        this.weatherData = forecastUseCase.execute(location, NUMBER_HOURS_OF_FORECAST);
    }

    @Override
    protected void displayWeatherData() {
        if (weatherData == null) {
            JOptionPane.showMessageDialog(this, "No weather data available to display!",
                    MESSAGE_DIALOGUE_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (forecastPanel != null) {
            mainPanel.remove(forecastPanel);
        }

        forecastPanel = new JPanel(new GridLayout(0, 2, GAP, GAP));
        forecastPanel.add(new JLabel("Local Time", SwingConstants.CENTER));
        forecastPanel.add(new JLabel("Temperature (Celsius)", SwingConstants.CENTER));

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

            forecastPanel.add(new JLabel(forecastLocalTime.getHour() + ":00", SwingConstants.CENTER));
            forecastPanel.add(new JLabel(String.valueOf(temperature), SwingConstants.CENTER));

            totalTemp += temperature;
        }

        mainPanel.add(forecastPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        double avgTemp = totalTemp / NUMBER_HOURS_OF_FORECAST;

        if (avgTemp <= TEMP_THRESHOLD) {
            JOptionPane.showMessageDialog(this,
                    "The average temperature for the next 8 hours: " + avgTemp
                            + "C!\n Don't forget to wear warm clothing.", "Temperature Warning",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
