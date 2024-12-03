package presentation.ui.views;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import presentation.ui.windows.LocationsWindow;
import utils.Constants;

import static utils.Constants.MAX_FORECAST_DAYS;
import static utils.Constants.CELCIUS_UNIT_TYPE;
import java.time.DayOfWeek;
import java.time.LocalDate;

import java.awt.*;

import javax.swing.*;

/**
 * Class that handles UI to displays the daily forecast (up to 16 days) showing max and min
 * temperature, percipitation sum, and wind speed (with dominant wind direction).
 */
public class ForecastDailyView extends LocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Forecast Daily";

    private boolean getNumOfDaysUseCaseFailed = false;

    private final JLabel numberOfDaysLabel = new JLabel();
    private final JTextField numberOfDaysField = new JTextField(5);

    private final JButton nextDayButton = new JButton("Next Day");
    private final JButton previousDayButton = new JButton("Previous Day");

    private final JLabel temperatureUnits = new JLabel();
//    private final JButton changeUnits = new JButton("Change Units");

    private String currenUnits = Constants.CELCIUS_UNIT_TYPE;

    private int numberOfDays;

    private LocalDate currentDate = LocalDate.now();

    private GetForecastWeatherDataUseCase forecastWeatherDataUseCase;

    private int selectedDayIndex = 0;

    private DayPanel dayPanel;

    // This is here temporarily for my test (DO NOT REMOVE)
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
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        numberOfDays = MAX_FORECAST_DAYS;
        dayPanel = new DayPanel(currentDate, true);

        dayPanel.setVisible(true);
        mainPanel.add(dayPanel);

        nextDayButton.addActionListener(eListener -> showNextDay());
        previousDayButton.addActionListener(eListener -> showPrevDay());
    }

    public ForecastDailyView(String name, int[] dimensions,
                             GetLocationDataUseCase locationDataUseCase,
                             String apiKey, ApiService apiService) {
        super(name, dimensions, locationDataUseCase, apiKey, apiService);
        this.forecastWeatherDataUseCase = new GetForecastWeatherDataUseCase(apiService);
        numberOfDaysLabel.setText("Number of Days to Forecast: ");
        setNumberOfDaysField(String.valueOf(MAX_FORECAST_DAYS));
        inputPanel.add(numberOfDaysLabel);
        inputPanel.add(numberOfDaysField);
        inputPanel.add(temperatureUnits);
        inputPanel.add(nextDayButton);
        inputPanel.add(previousDayButton);
        // inputPanel.add(changeUnits);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        numberOfDays = MAX_FORECAST_DAYS;
        dayPanel = new DayPanel(currentDate, true);
        dayPanel.setVisible(true);
        mainPanel.add(dayPanel);

        nextDayButton.addActionListener(eListener -> showNextDay());
        previousDayButton.addActionListener(eListener -> showPrevDay());
        // This button will allow the user to change the temperature units (not yet implemented)
        //changeUnits.addActionListener(eListenr -> changeUnitsView.showPanel());
    }

    private void updateTemperatureUnits(String temperatureCategory) {
        temperatureUnits.setText("Temperature Units: " + weatherDataDTO.getTemperatureUnit(temperatureCategory, currenUnits));
    }

    // Selects the next day from the current selected day
    private void showNextDay() {
        selectedDayIndex = Math.min(selectedDayIndex + 1, numberOfDays - 1);
        showSelectedDayPanel();
    }

    // Selects the previous day from the current selected day
    private void showPrevDay() {
        selectedDayIndex = Math.max(selectedDayIndex - 1, 0);
        showSelectedDayPanel();
    }

    @Override
    protected void getWeatherData() {
        try {
            // Fetch weather data for up to 16 days
            final WeatherData weatherData = forecastWeatherDataUseCase.execute(location, MAX_FORECAST_DAYS);

            if (forecastWeatherDataUseCase.isUseCaseFailed()) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Error: Could not fetch weather data for the selected location.",
                        "Weather Data Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate the WeatherDataDTO object for UI updates
            final LocalDate startDate = currentDate;
            final LocalDate endDate = startDate.plusDays(MAX_FORECAST_DAYS);
            generateWeatherDataDTO(weatherData, startDate, endDate);

            getNumOfDaysUseCaseFailed = getNumOfDaysUseCase();

        }
        catch (Exception exception) {
            JOptionPane.showMessageDialog(mainPanel,
                    "An unexpected error occurred while fetching weather data: " + exception.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            // Log the error for debugging
            exception.printStackTrace();
        }
    }

    protected void displayWeatherData() {
        if (dayPanel != null) {
            showSelectedDayPanel();
        }
    }

    // Shows the panel of the selected day
    private void showSelectedDayPanel() {
        dayPanel.setDate(currentDate.plusDays(selectedDayIndex), selectedDayIndex == 0);
        updateDayPanel(selectedDayIndex);
        dayPanel.setVisible(true);
    }

    private void showNumberOfDaysError() {
        JOptionPane.showMessageDialog(mainPanel, "ERROR: Invalid number of days entered.");
    }

    public boolean getNumOfDaysUseCase() {
        boolean useCaseFailed = false;
        selectedDayIndex = 0;
        // Parse the number of days entered by the user (from the text field)
        final String userInput = numberOfDaysField.getText().trim();
        if (userInput.isEmpty()) {
            // Show an error and stop processing
            showNumberOfDaysError();
            useCaseFailed = true;
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
                showNumberOfDaysError();
                useCaseFailed = true;
            }
        }
        catch (NumberFormatException exception) {
            // Invalid input, show error
            showNumberOfDaysError();
            useCaseFailed = true;
        }
        return useCaseFailed;
    }

    private void updateDayPanel(int index) {
        try {
            dayPanel.updateWeatherDataValues(weatherDataDTO, index, CELCIUS_UNIT_TYPE);
        }
        catch (Exception exception) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Error displaying weather data: " + exception.getMessage(),
                    "Data Error",
                    JOptionPane.ERROR_MESSAGE);
            exception.printStackTrace();
        }
    }

    // added this method for usage in FavoriteView
    public void setLocation(Location location) {
        this.location = location;
        // Fetch weather data immediately
        getWeatherData();
        // Display the fetched data
        displayWeatherData();
    }

    /**
     * Sets the number of days in a text field.
     * @param numDays the number of days of forecast the user wants
     */
    public void setNumberOfDaysField(String numDays) {
        this.numberOfDaysField.setText(numDays);
    }

}
