package presentation.ui.views;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import org.json.JSONArray;
import org.json.JSONException;
import presentation.ui.windows.GraphSelectionWindow;
import presentation.ui.windows.LocationsWindow;
import presentation.visualization.BarGraphWeatherComparison;
import presentation.visualization.LineGraphWeatherComparison;
import utils.Constants;

import javax.swing.*;
import java.time.LocalDate;

import static utils.Constants.MAX_FORECAST_DAYS;

/**
 * Class that handles UI to display the daily forecast (up to 16 days) showing max and min
 * temperature, precipitation sum, and wind speed (with dominant wind direction).
 */
public class ForecastDailyView extends LocationsWindow {

    public static final String OPTION_NAME = "Forecast Daily";

    private final JLabel numberOfDaysLabel = new JLabel();
    private final JTextField numberOfDaysField = new JTextField(5);

    private final JButton nextDayButton = new JButton("Next Day");
    private final JButton previousDayButton = new JButton("Previous Day");
    private final JButton visualizeButton = new JButton("Visualize!");

    private final String currentUnits = Constants.CELCIUS_UNIT_TYPE;

    private int numberOfDays;
    private final LocalDate currentDate = LocalDate.now();
    private final GetForecastWeatherDataUseCase forecastWeatherDataUseCase;
    private int selectedDayIndex;
    private DayPanel dayPanel;

    // This variable is used to detect certain tests that show error message pop up
    private boolean errorOccured;

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

        nextDayButton.addActionListener(actionEvent -> showNextDay());
        previousDayButton.addActionListener(actionEvent -> showPrevDay());
        visualizeButton.addActionListener(actionEvent -> openGraphSelectionWindow());
    }

    @Override
    protected void openVisualization() {
        openGraphSelectionWindow();
    }

    private void openGraphSelectionWindow() {
        if (weatherDataDTO == null) {
            showErrorMessage("No weather data available for visualization!");
        }
        else {
            final GraphSelectionWindow graphSelectionWindow = new GraphSelectionWindow(graphType -> {
                if ("bar".equals(graphType)) {
                    showBarGraph();
                }
                else if ("line".equals(graphType)) {
                    showLineGraph();
                }
            });
            graphSelectionWindow.display();
        }
    }

    private void showBarGraph() {
        final BarGraphWeatherComparison barGraph = new BarGraphWeatherComparison("Daily Forecast Bar Graph");

        try {
            for (int dayIndex = 0; dayIndex < numberOfDays; dayIndex++) {
                final LocalDate date = currentDate.plusDays(dayIndex);
                final Double temperature = weatherDataDTO.getWeatherDataValue("temperatureMeanDaily", dayIndex);

                if (temperature != null) {
                    barGraph.addData("Temperature", date.toString(), temperature);
                }
            }
            barGraph.display();
        }
        catch (JSONException exception) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Error displaying bar graph: " + exception.getMessage(),
                    "Visualization Error", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }

    private void showLineGraph() {
        final LineGraphWeatherComparison lineGraph = new LineGraphWeatherComparison("Daily Forecast Line Graph");

        try {
            for (int dayIndex = 0; dayIndex < numberOfDays; dayIndex++) {
                final LocalDate date = currentDate.plusDays(dayIndex);
                final Double temperature = weatherDataDTO.getWeatherDataValue("temperatureMeanDaily", dayIndex);

                if (temperature != null) {
                    lineGraph.addData("Temperature", date.toString(), temperature);
                }
            }
            lineGraph.display();
        }
        catch (JSONException exception) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Error displaying line graph: " + exception.getMessage(),
                    "Visualization Error", JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
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
            final boolean isNumberOfDaysValid = getNumberOfDays();
            if (isNumberOfDaysValid) {
                final WeatherData weatherData = forecastWeatherDataUseCase.execute(location, MAX_FORECAST_DAYS);

                if (forecastWeatherDataUseCase.isUseCaseFailed()) {
                    showErrorMessage("Error: Could not fetch weather data for the selected location.");
                }
                else {

                    final LocalDate startDate = currentDate;
                    final LocalDate endDate = startDate.plusDays(MAX_FORECAST_DAYS);
                    generateWeatherDataDTO(weatherData, startDate, endDate);
                }
            }

        }
        catch (JSONException exception) {
            showErrorMessage("An unexpected error occurred while fetching weather data: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    public boolean isWeatherDataUseCaseFailed() {
        return forecastWeatherDataUseCase.isUseCaseFailed();
    }

    private void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(mainPanel,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
        errorOccured = true;
    }

    /**
     * Returns whether an error occured (which led to an error pop up window to show).
     * @return whether an error occured or not
     */
    public boolean didErrorOccur() {
        return errorOccured;
    }

    protected void displayWeatherData() {
        if (dayPanel != null) {
            showSelectedDayPanel();
        }
        else {
            showErrorMessage("Error: Day Panel is Null");
        }
    }

    public void setDayPanel(DayPanel newDayPanel) {
        this.dayPanel = newDayPanel;
    }

    public String getNumDaysText() {
        return numberOfDaysField.getText();
    }

    /**
     * Sets the text field for number of days.
     * @param numDays the number of days to forecast
     */
    public void setNumberOfDaysText(String numDays) {
        this.numberOfDaysField.setText(numDays);
    }

    public boolean getNumberOfDays() {
        selectedDayIndex = 0;
        boolean numDaysFailed = false;
        // Parse the number of days entered by the user (from the text field)
        final String userInput = numberOfDaysField.getText().trim();
        if (userInput.isEmpty()) {
            // Show an error and stop processing
            showErrorMessage("Error: no input for forecast days!");
            numDaysFailed = true;
        }
        // Parse the number of days entered by the user
        try {
            final int numDays = Integer.parseInt(userInput);
            if (numDays > 0 && numDays <= MAX_FORECAST_DAYS) {
                // Validate the range
                this.numberOfDays = numDays;
            }
            else {
                // Invalid range, show error
                showErrorMessage("Error: number of days is out of range (up to 16)!");
                numDaysFailed = true;
            }
        }
        catch (NumberFormatException exception) {
            // Invalid input, show error
            showErrorMessage("Error: invalid input for forecast days!");
            numDaysFailed = true;
        }
        return numDaysFailed;
    }

    private void showSelectedDayPanel() {
        dayPanel.setDate(currentDate.plusDays(selectedDayIndex), selectedDayIndex == 0);
        updateDayPanel(selectedDayIndex);
        dayPanel.setVisible(true);
    }

    private void updateDayPanel(int index) {
        try {
            dayPanel.updateWeatherDataValues(weatherDataDTO, index, currentUnits);
        }
        catch (JSONException exception) {
            showErrorMessage("Error displaying weather data: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
