package presentation.ui.views;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import presentation.ui.windows.GraphSelectionWindow;
import presentation.ui.windows.LocationsWindow;
import presentation.visualization.BarGraphWeatherComparison;
import presentation.visualization.LineGraphWeatherComparison;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import static utils.Constants.MAX_FORECAST_DAYS;

/**
 * Class that handles UI to display the daily forecast (up to 16 days) showing max and min
 * temperature, precipitation sum, and wind speed (with dominant wind direction).
 */
public class ForecastDailyView extends LocationsWindow {

    public static final String OPTION_NAME = "Forecast Daily";

    private final JLabel numberOfDaysLabel = new JLabel();
    final JTextField numberOfDaysField = new JTextField(5);

    private final JButton nextDayButton = new JButton("Next Day");
    private final JButton previousDayButton = new JButton("Previous Day");
    private final JButton visualizeButton = new JButton("Visualize!");

    private final JLabel temperatureUnits = new JLabel();
    private String currentUnits = Constants.CELCIUS_UNIT_TYPE;

    private int numberOfDays;
    private LocalDate currentDate = LocalDate.now();
    private GetForecastWeatherDataUseCase forecastWeatherDataUseCase;
    private int selectedDayIndex = 0;
    private DayPanel dayPanel;

    public ForecastDailyView(String name, int[] dimensions, Location location,
                             GetForecastWeatherDataUseCase forecastWeatherDataUseCase,
                             GetLocationDataUseCase locationDataUseCase,
                             String apiKey, ApiService apiService) {
        super(name, dimensions, locationDataUseCase, apiKey, apiService);
        this.location = location;
        this.forecastWeatherDataUseCase = forecastWeatherDataUseCase;

        numberOfDaysLabel.setText("Number of Days to Forecast: ");
        inputPanel.add(numberOfDaysLabel);
        inputPanel.add(numberOfDaysField);
        inputPanel.add(nextDayButton);
        inputPanel.add(previousDayButton);
        inputPanel.add(visualizeButton);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        numberOfDays = MAX_FORECAST_DAYS;
        dayPanel = new DayPanel(currentDate, true);
        dayPanel.setVisible(true);
        mainPanel.add(dayPanel);

        nextDayButton.addActionListener(e -> showNextDay());
        previousDayButton.addActionListener(e -> showPrevDay());
        visualizeButton.addActionListener(e -> openGraphSelectionWindow());
    }

    @Override
    protected void openVisualization() {
        openGraphSelectionWindow();
    }

    private void openGraphSelectionWindow() {
        if (weatherDataDTO == null) {
            JOptionPane.showMessageDialog(mainPanel, "No weather data available for visualization!",
                    "Visualization Error", JOptionPane.ERROR_MESSAGE);
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
        BarGraphWeatherComparison barGraph = new BarGraphWeatherComparison("Daily Forecast Bar Graph");

        try {
            for (int dayIndex = 0; dayIndex < numberOfDays; dayIndex++) {
                LocalDate date = currentDate.plusDays(dayIndex);
                Double temperature = weatherDataDTO.getWeatherDataValue("temperatureMeanDaily", dayIndex);

                if (temperature != null) {
                    barGraph.addData("Temperature", date.toString(), temperature);
                }
            }
            barGraph.display();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Error displaying bar graph: " + e.getMessage(),
                    "Visualization Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showLineGraph() {
        LineGraphWeatherComparison lineGraph = new LineGraphWeatherComparison("Daily Forecast Line Graph");

        try {
            for (int dayIndex = 0; dayIndex < numberOfDays; dayIndex++) {
                LocalDate date = currentDate.plusDays(dayIndex);
                Double temperature = weatherDataDTO.getWeatherDataValue("temperatureMeanDaily", dayIndex);

                if (temperature != null) {
                    lineGraph.addData("Temperature", date.toString(), temperature);
                }
            }
            lineGraph.display();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Error displaying line graph: " + e.getMessage(),
                    "Visualization Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showNextDay() {
        selectedDayIndex = Math.min(selectedDayIndex + 1, numberOfDays - 1);
        showSelectedDayPanel();
    }

    private void showPrevDay() {
        selectedDayIndex = Math.max(selectedDayIndex - 1, 0);
        showSelectedDayPanel();
    }

    @Override
    protected void getWeatherData() {
        try {
            final WeatherData weatherData = forecastWeatherDataUseCase.execute(location, MAX_FORECAST_DAYS);

            if (forecastWeatherDataUseCase.isUseCaseFailed()) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Error: Could not fetch weather data for the selected location.",
                        "Weather Data Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            final LocalDate startDate = currentDate;
            final LocalDate endDate = startDate.plusDays(MAX_FORECAST_DAYS);
            generateWeatherDataDTO(weatherData, startDate, endDate);

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(mainPanel,
                    "An unexpected error occurred while fetching weather data: " + exception.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }

    protected void displayWeatherData() {
        if (dayPanel != null) {
            showSelectedDayPanel();
        }
    }

    private void showSelectedDayPanel() {
        dayPanel.setDate(currentDate.plusDays(selectedDayIndex), selectedDayIndex == 0);
        updateDayPanel(selectedDayIndex);
        dayPanel.setVisible(true);
    }

    private void updateDayPanel(int index) {
        try {
            dayPanel.updateWeatherDataValues(weatherDataDTO, index, currentUnits);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Error displaying weather data: " + exception.getMessage(),
                    "Data Error", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }

    public void setNumberOfDaysField(String numDays) {
        this.numberOfDaysField.setText(numDays);
    }
}
