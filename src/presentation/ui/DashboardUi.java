package presentation.ui;

import java.awt.GridLayout;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import application.usecases.GetCurrentWeatherConditionUseCase;
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
public class DashboardUi extends JFrame implements WeatherAlertObserver {

    // UI Components
    private JLabel locationLabel;
    private JLabel temperatureLabel;
    private JLabel conditionLabel;
    private JLabel humidityLabel;
    private JLabel windLabel;
//    private JLabel weatherIconLabel = new JLabel(); // New JLabel for the weather icon

    private final JTextField apiKeyField;
    private JLabel temperatureValue;
    private JLabel conditionValue;
    private JLabel humidityValue;
    private JLabel windValue;
    private JLabel severeAlertLabel;
    private JLabel severeAlertValue;


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
    private final WeatherAlertFunction weatherAlertFunction;
    private final GetCurrentWeatherConditionUseCase currentWeatherConditionUseCase;
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

        // use case intialized in dashboard, might change to main
        currentWeatherConditionUseCase = new GetCurrentWeatherConditionUseCase(new OpenWeatherApiService());

        // register WeatherAlertFunction as an observer
        weatherAlertFunction = new WeatherAlertFunction(weatherApiService);
        weatherAlertFunction.addObserver(this);

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
//        weatherIconLabel = new JLabel();
        severeAlertLabel = new JLabel("Severe Alert:");


        String placeholder = "N/A";
        temperatureValue = new JLabel("N/A");
        conditionValue = new JLabel("N/A");
        humidityValue = new JLabel("N/A");
        windValue = new JLabel("N/A");
        severeAlertValue = new JLabel("N/A");

        getInfoButton = new JButton("Get Info");
        refreshButton = new JButton("Refresh");
        rangeOfTimeButton = new JButton("Range of Time");
        simulationButton = new JButton("Simulation");

        // Create and configure layout
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2, 10, 10)); // Add space for the icon

        // Add components to panel
        panel.add(locationLabel);
        panel.add(menu);
        panel.add(temperatureLabel);
        panel.add(temperatureValue);
        panel.add(conditionLabel);
        panel.add(conditionValue);
//        panel.add(weatherIconLabel); // Add the weather icon to the layout
        panel.add(humidityLabel);
        panel.add(humidityValue);
        panel.add(windLabel);
        panel.add(windValue);
        panel.add(severeAlertLabel);
        panel.add(severeAlertValue);
        panel.add(getInfoButton);
        panel.add(refreshButton);
        panel.add(rangeOfTimeButton);
        panel.add(simulationButton);


        // Add panel to JFrame
        add(panel);
    }

    private String getCustomConditionDescription(String originalDescription) {
        switch (originalDescription.toLowerCase()) {
            case "light rain":
                return "Flood";
            case "heavy rain":
                return "Downpour";
            case "clear sky":
                return "Sunny";
            case "overcast clouds":
                return "Cloudy";
            default:
                return originalDescription; // Use the original if no match
        }
    }

    private void setupEventListeners() {
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
        severeAlertValue.setText("N/A"); // Reset severe weather alert to default
        menu.clearLocationField();
    }


    @Override
    public void onSevereWeatherAlert(String alertMessage) {
        System.out.println("DashboardUi received severe weather alert: " + alertMessage);

        if (!alertMessage.equals("No severe weather alerts available.") && !alertMessage.startsWith("Error")) {
            JOptionPane.showMessageDialog(this, alertMessage, "Severe Weather Alert", JOptionPane.WARNING_MESSAGE);
        }
        conditionValue.setText(alertMessage); // Update the UI with the alert message
    }


    private void displayWeatherData() {
        final Location chosenLocation = menu.getSelectedLocation();
        if (chosenLocation == null) {
            System.out.println("No location selected");
            openInvalidLocationWindow();
            return;
        }

        // Set loading message while fetching data
        conditionValue.setText("Loading...");

        final String locationKey = chosenLocation.getCity();
        WeatherData weatherData = weatherDataCache.get(locationKey);

        if (weatherData == null) {
            System.out.println("Fetching weather data from API...");
            weatherData = fetchWeatherData(chosenLocation);
            if (weatherData != null) {
                weatherDataCache.put(locationKey, weatherData);
            } else {
                System.out.println("Weather data could not be retrieved.");
                conditionValue.setText("Error: Unable to fetch weather data.");
                return;
            }
        } else {
            System.out.println("Using cached weather data...");
        }

        // Mock logic for TestCity
        if ("TestCity".equalsIgnoreCase(chosenLocation.getCity())) {
            conditionValue.setText("Heavy Rain");
            temperatureValue.setText("4 °C"); // Mock temperature
            humidityValue.setText("83 %");      // Mock humidity
            windValue.setText("4.45 m/s");      // Mock wind
            severeAlertValue.setText("Flood (check local news for more information)");

            // Generalized pop-up for severe alerts
            JOptionPane.showMessageDialog(
                    this,
                    "Severe Weather Alert: check local news for more information",
                    "Severe Weather Alert",
                    JOptionPane.WARNING_MESSAGE
            );
            return; // Skip real API updates for TestCity
        }

        // Fetch severe weather alert for other cities
        String severeAlert = weatherAlertFunction.getSevereWeather(
                chosenLocation.getLatitude(),
                chosenLocation.getLongitude()
        );

        if (severeAlert.equals("No severe weather alerts available.")) {
            severeAlertValue.setText("No severe weather alerts");
        } else if (severeAlert.startsWith("Error")) {
            severeAlertValue.setText("Error fetching severe weather data");
        } else {
            severeAlertValue.setText(severeAlert);

            // Generalized pop-up for all severe alerts
            JOptionPane.showMessageDialog(
                    this,
                    severeAlert,
                    "Severe Weather Alert",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        // Update other UI details
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

        // Update condition label with the current weather condition
        conditionValue.setText(weatherData.getCondition());

        // Set the weather icon (if available)
//    String iconCode = weatherData.getIconCode();
//    if (iconCode != null) {
//        try {
//            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
//            weatherIconLabel.setIcon(new ImageIcon(new URL(iconUrl))); // Load icon from the URL
//        } catch (Exception e) {
//            e.printStackTrace();
//            weatherIconLabel.setIcon(null); // Clear the icon if there's an issue
//        }
//    } else {
//        weatherIconLabel.setIcon(null); // Clear the icon if no icon is available
//    }
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
                    0.0, new ArrayList<String>() {
            });
        } catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private void extractWeatherData(WeatherData weatherData, Location location, LocalDate date) {
        if (weatherData != null) {
            this.weatherDataDTO = createWeatherDataDto(weatherData.getWeatherDetails(), location, date);
        } else {
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
     * @param args
     */
    public static void main(String[] args) {
        final DashboardUi frame = new DashboardUi();

        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
}
