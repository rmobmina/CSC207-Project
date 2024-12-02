package presentation.ui.views;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import presentation.ui.windows.LocationsWindow;
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
            final WeatherData weatherData = forecastWeatherDataUseCase.execute(location, 16);

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
            // Update temperature values
            dayPanel.setMaxTemperatureValue(
                    weatherDataDTO.temperatureToString("temperatureMaxDaily", index, CELCIUS_UNIT_TYPE));
            dayPanel.setMinTemperatureValue(
                    weatherDataDTO.temperatureToString("temperatureMinDaily", index, CELCIUS_UNIT_TYPE));

            // Update precipitation value
            dayPanel.setPercipitationValue(
                    weatherDataDTO.dataToString("percipitationDaily", index));

            // Optionally, update additional fields like wind speed and direction
            String windSpeed = weatherDataDTO.dataToString("windSpeedDaily", index);
            String windDirection = weatherDataDTO.dataToString("windDirectionDaily", index);

            System.out.println("Wind Speed: " + windSpeed + ", Wind Direction: " + windDirection); // Debugging purposes

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

    // This is class that constructs a panel for a specific day (with weather details)
    private class DayPanel extends JPanel {
        JLabel dateLabel = new JLabel();
        JLabel maxTemperatureLabel = new JLabel();
        JLabel maxTemperatureValue = new JLabel();
        JLabel minTemperatureLabel = new JLabel();
        JLabel minTemperatureValue = new JLabel();
        JLabel percipitationLabel = new JLabel();
        JLabel percipitationValue = new JLabel();

        DayPanel(LocalDate date, boolean dayIsCurrent) {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setDate(date, dayIsCurrent);
            this.setBorder(BorderFactory.createTitledBorder(""));
            maxTemperatureLabel.setText("Max Temperature: ");
            minTemperatureLabel.setText("Min Temperature: ");
            percipitationLabel.setText("Percipitation: ");
            initComponents();
        }

        private void initComponents() {
            this.add(dateLabel);
            final JPanel weatherPanel = new JPanel();
            weatherPanel.setLayout(new GridLayout(3, 1, 1, 1));
            weatherPanel.add(maxTemperatureLabel);
            weatherPanel.add(maxTemperatureValue);
            weatherPanel.add(minTemperatureLabel);
            weatherPanel.add(minTemperatureValue);
            weatherPanel.add(percipitationLabel);
            weatherPanel.add(percipitationValue);
            this.add(weatherPanel);
        }

        public void setDate(LocalDate date, boolean dayIsCurrent) {
            if (dayIsCurrent) {
                dateLabel.setText("Today");
            }
            else {
                final DayOfWeek dayOfWeek = date.getDayOfWeek();
                dateLabel.setText(dayOfWeek.toString() + " " + date);
            }
        }

        public void setMaxTemperatureValue(String maxTemperature) {
            this.maxTemperatureValue.setText(maxTemperature);
        }

        public void setMinTemperatureValue(String minTemperature) {
            this.minTemperatureValue.setText(minTemperature);
        }

        public void setPercipitationValue(String percipitation) {
            this.percipitationValue.setText(percipitation);
        }
    }

}
