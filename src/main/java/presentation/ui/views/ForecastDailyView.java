package presentation.ui.views;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import presentation.ui.windows.LocationsWindow;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Class that handles UI to displays the daily forecast (up to 16 days) showing max and min
 * temperature, percipitation sum, and wind speed (with dominant wind direction).
 */
public class ForecastDailyView extends LocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Forecast Daily";

    private final JLabel numberOfDaysLabel = new JLabel();
    private final JTextField numberOfDaysField = new JTextField(5);

    private final JButton nextDayButton = new JButton("Next Day");
    private final JButton previousDayButton = new JButton("Previous Day");

    private int numberOfDays;

    private LocalDate currentDate = LocalDate.now();

    private GetForecastWeatherDataUseCase forecastWeatherDataUseCase;

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
            JPanel weatherPanel = new JPanel();
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
            } else {
                DayOfWeek dayOfWeek = date.getDayOfWeek();
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

    private int selectedDayIndex = 0;

    private DayPanel dayPanel;

    public ForecastDailyView(String name, int[] dimensions, GetLocationDataUseCase locationDataUseCase, String apiKey,
                             ApiService apiService) {
        super(name, dimensions, locationDataUseCase, apiKey, apiService);
        forecastWeatherDataUseCase = new GetForecastWeatherDataUseCase(apiService);
        numberOfDaysLabel.setText("Number of Days to Forecast: ");
        inputPanel.add(numberOfDaysLabel);
        inputPanel.add(numberOfDaysField);
        inputPanel.add(nextDayButton);
        inputPanel.add(previousDayButton);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        numberOfDays = 16;
        dayPanel = new DayPanel(currentDate, true);
        dayPanel.setVisible(true);
        mainPanel.add(dayPanel);

        nextDayButton.addActionListener(e -> showNextDay());
        previousDayButton.addActionListener(e -> showPrevDay());
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
            WeatherData weatherData = forecastWeatherDataUseCase.execute(location, 16);

            if (weatherData == null) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Error: Could not fetch weather data for the selected location.",
                        "Weather Data Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate the WeatherDataDTO object for UI updates
            LocalDate startDate = currentDate;
            LocalDate endDate = startDate.plusDays(16); // 16-day range
            generateWeatherDataDTO(weatherData, startDate, endDate);

            // Parse the number of days entered by the user (from the text field)
            String userInput = numberOfDaysField.getText().trim();
            if (userInput.isEmpty()) {
                showNumberOfDaysError(); // Show an error and stop processing
                return;
            }

            // Parse the number of days entered by the user
            try {
                int numDays = Integer.parseInt(userInput);
                if (numDays > 0 && numDays <= 16) { // Validate the range
                    this.numberOfDays = numDays;
                } else {
                    showNumberOfDaysError(); // Invalid range, show error
                }
            } catch (NumberFormatException e) {
                showNumberOfDaysError(); // Invalid input, show error
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel,
                    "An unexpected error occurred while fetching weather data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Log the error for debugging
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

    private void updateDayPanel(int index) {
        try {
            // Update temperature values
            dayPanel.setMaxTemperatureValue(
                    weatherDataDTO.temperatureToString("temperatureMaxDaily", index, Constants.CELCIUS_UNIT_TYPE));
            dayPanel.setMinTemperatureValue(
                    weatherDataDTO.temperatureToString("temperatureMinDaily", index, Constants.CELCIUS_UNIT_TYPE));

            // Update precipitation value
            dayPanel.setPercipitationValue(
                    weatherDataDTO.dataToString("percipitationDaily", index));

            // Optionally, update additional fields like wind speed and direction
            String windSpeed = weatherDataDTO.dataToString("windSpeedDaily", index);
            String windDirection = weatherDataDTO.dataToString("windDirectionDaily", index);

            System.out.println("Wind Speed: " + windSpeed + ", Wind Direction: " + windDirection); // Debugging purposes

        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Error displaying weather data: " + e.getMessage(),
                    "Data Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    // added this method for usage in FavoriteView
    public void setLocation(Location location) {
        this.location = location;
        getWeatherData(); // Fetch weather data immediately
        displayWeatherData(); // Display the fetched data
    }

}
