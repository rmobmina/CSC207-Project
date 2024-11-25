package presentation.ui;

import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import application.dto.WeatherDataDTO;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;
import infrastructure.adapters.WeatherAPIService;
import utils.ApiKeys;

/**
 * DashboardUI That runs the interface to use the program with its features.
 * Initialized variables are seperated based on type and usage.
 */
public class DashboardUi extends JFrame {

    // UI Components
    private JLabel locationLabel;
    private JLabel temperatureLabel;
    private JLabel conditionLabel;
    private JLabel humidityLabel;
    private JLabel windLabel;

    private final JTextField apiKeyField;
    private JLabel temperatureValue;
    private JLabel conditionValue;
    private JLabel humidityValue;
    private JLabel windValue;

    private JButton getInfoButton;
    private JButton refreshButton;
    private JButton rangeOfTimeButton;
    private JButton simulationButton;

    // Data Variables
    static WeatherDataDTO weatherDataDTO = new WeatherDataDTO();
    private static final String temperatureType = "cel";
    private final Map<String, WeatherData> weatherDataCache = new HashMap<>();
    private final String apiKey;

    // Services and Menu
    private final OpenWeatherApiService apiService = new OpenWeatherApiService();
    private final WeatherAPIService weatherApiService = new WeatherAPIService(ApiKeys.WEATHER_API_KEY);
    private final WeatherAlertFunction weatherAlertFunction = new WeatherAlertFunction(weatherApiService);
    private final DropDownUI menu;

    // Window Measurements
    final int height = 200;
    final int width = 400;

    /**
     * DashboardUI That runs the interface to use the program with its features.
     */
    public DashboardUi() {
        setTitle("Weather Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize API Key and Dropdown Menu
        apiKey = ApiKeys.OPEN_WEATHER_API_KEY;
        apiKeyField = new JTextField(apiKey, 30);
        menu = new DropDownUI(apiKey);

        // Call setupUI method to configure UI elements
        setupUi();

        // Add Action Listeners for buttons
        setupEventListeners();
        }

    private void setupUi() {
        // Initialize Components
        locationLabel = new JLabel("Location:");
        temperatureLabel = new JLabel("Temperature:");
        conditionLabel = new JLabel("Condition:");
        humidityLabel = new JLabel("Humidity:");
        windLabel = new JLabel("Wind:");

        String placeholder = "N/A";
        temperatureValue = new JLabel("N/A");
        conditionValue = new JLabel("N/A");
        humidityValue = new JLabel("N/A");
        windValue = new JLabel("N/A");

        getInfoButton = new JButton("Get Info");
        refreshButton = new JButton("Refresh");
        rangeOfTimeButton = new JButton("Range of Time");
        simulationButton = new JButton("Simulation");

        // Create and configure layout
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 10, 10));

        // Add components to panel
        panel.add(locationLabel);
        panel.add(menu);
        panel.add(temperatureLabel);
        panel.add(temperatureValue);
        panel.add(conditionLabel);
        panel.add(conditionValue);
        panel.add(humidityLabel);
        panel.add(humidityValue);
        panel.add(windLabel);
        panel.add(windValue);
        panel.add(getInfoButton);
        panel.add(refreshButton);
        panel.add(rangeOfTimeButton);
        panel.add(simulationButton);

        // Add panel to JFrame
        add(panel);
    }

    private void setupEventListeners() {
        // Add Action Listeners for buttons
        getInfoButton.addActionListener(e -> displayWeatherData());
        refreshButton.addActionListener(e -> refreshAll());
        rangeOfTimeButton.addActionListener(e -> openRangeOfTimeWindow());
        simulationButton.addActionListener(e -> openSimulationWindow());
    }

    private void refreshAll() {
        // Clears all the UI components
        clearUiFields();
        menu.resetSelection();
    }

    private void clearUiFields() {
        apiKeyField.setText(apiKey);
        temperatureValue.setText("N/A");
        conditionValue.setText("N/A");
        humidityValue.setText("N/A");
        windValue.setText("N/A");
    }

    private void displayWeatherData() {
        final Location chosenLocation = menu.getSelectedLocation();
        if (chosenLocation == null) {
            System.out.println("No location selected");
            openInvalidLocationWindow();
            return;
        }

        final String locationKey = chosenLocation.getCity();
        WeatherData weatherData = weatherDataCache.get(locationKey);

        if (weatherData == null) {
            System.out.println("Fetching weather data from API...");
            weatherData = fetchWeatherData(chosenLocation);
            if (weatherData != null) {
                weatherDataCache.put(locationKey, weatherData);
            } else {
                System.out.println("Weather data could not be retrieved.");
                temperatureValue.setText("N/A");
                return;
            }
        } else {
            System.out.println("Using cached weather data...");
        }

        updateWeatherUi(weatherData, chosenLocation);

    }

    private String fetchWeatherAlerts(Location location) {
        try {
            return weatherAlertFunction.getSevereWeather(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching weather alert data.";
        }
    }


    private WeatherData fetchWeatherData(Location location) {
        final String apiKey = getApiKeyFieldValue();
        if (apiKey == null || apiKey.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "API key cannot be empty. Please enter a valid API key.",
                "Invalid API Key",
                JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        try {
            return apiService.fetchWeather(location, apiKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void updateWeatherUi(WeatherData weatherData, Location location) {
        final LocalDate date = LocalDate.now();
        final WeatherDataDTO dto = createWeatherDataDto(weatherData.getWeatherDetails(), location, date);
        updateTextFields(dto);
        final String condition = fetchWeatherAlerts(location);
        conditionValue.setText(condition);
    }

    private String getApiKeyFieldValue() {
        return apiKeyField.getText();
    }

    private WeatherDataDTO createWeatherDataDto(JSONObject data, Location location, LocalDate date) {
        try {
            final JSONObject mainData = data.getJSONObject("main");
            final double temperatureInCelcius = mainData.getDouble("temp");
            return new WeatherDataDTO(
                    location.getCity(),
                    date,
                    temperatureInCelcius,
                    mainData.getInt("humidity"),
                    data.getJSONObject("wind").getDouble("speed"),
                    // NOTE: may not always have percipitation data and alerts
                    0.0, new ArrayList<String>() { });
        }
        catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private void extractWeatherData(WeatherData weatherData, Location location, LocalDate date) {
        if (weatherData != null) {
            this.weatherDataDTO = createWeatherDataDto(weatherData.getWeatherDetails(), location, date);
        }
        else {
            System.out.println("Error: Could not retrieve weather data.");
        }
    }

    private void updateTextFields(WeatherDataDTO weatherDataDto) {
        final DecimalFormat df = new DecimalFormat("#.##");
        temperatureValue.setText(df.format(weatherDataDto.getTemperature(temperatureType)) + " °C");
        humidityValue.setText(weatherDataDto.humidity + " %");
        windValue.setText(weatherDataDto.windSpeed + " m/s");
    }

    private void openInvalidLocationWindow() {
        final JFrame rangeWindow = new JFrame("Invalid Location");
        rangeWindow.setSize(width, height);
        rangeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final JLabel placeholderLabel = new JLabel("The location you entered is invalid. Please try again!",
                SwingConstants.CENTER);
        rangeWindow.add(placeholderLabel);
        rangeWindow.setVisible(true);
    }

    // Placeholder window for "Range of Time" feature
    private void openRangeOfTimeWindow() {
        final JFrame rangeWindow = new JFrame("Range of Time");
        rangeWindow.setSize(300, 200);
        rangeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final JLabel placeholderLabel = new JLabel("feature not added yet.", SwingConstants.CENTER);
        rangeWindow.add(placeholderLabel);
        rangeWindow.setVisible(true);
    }

    // Placeholder window for simulation
    private void openSimulationWindow() {
        final JFrame simulationWindow = new JFrame("Simulation");
        simulationWindow.setSize(300, 200);
        simulationWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final JLabel placeholderLabel = new JLabel("feature not added yet.", SwingConstants.CENTER);
        simulationWindow.add(placeholderLabel);
        simulationWindow.setVisible(true);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        final DashboardUi frame = new DashboardUi();

        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
}
