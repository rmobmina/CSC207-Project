package presentation.ui;

import application.dto.WeatherDataDTO;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetWeatherDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;
import infrastructure.adapters.WeatherAPIService;
import utils.ApiKeys;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class DashboardUI extends JFrame {

    // UI Components
    private JLabel locationLabel;
    private JLabel temperatureLabel;
    private JLabel conditionLabel;
    private JLabel humidityLabel;
    private JLabel windLabel;

    private JTextField apiKeyField;
    private JLabel temperatureValue;
    private JLabel conditionValue;
    private JLabel humidityValue;
    private JLabel windValue;

    private JButton getInfoButton;
    private JButton refreshButton;
    private JButton rangeOfTimeButton;
    private JButton simulationButton;

    // Data Variables
    static final String temperatureType = "cel"; // Temperature display type
    static WeatherDataDTO weatherDataDTO = new WeatherDataDTO();
    private final Map<String, WeatherData> weatherDataCache = new HashMap<>();
    private final String apiKey; // API key for OpenWeather API

    // Services and Menu
    private final OpenWeatherApiService apiService = new OpenWeatherApiService();
    private final WeatherAPIService weatherAPIService = new WeatherAPIService(ApiKeys.WEATHER_API_KEY);
    private final WeatherAlertFunction weatherAlertFunction = new WeatherAlertFunction(weatherAPIService);
    private final DropDownUI menu;


public DashboardUI() {
        setTitle("Weather Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize API Key and Dropdown Menu
        apiKey = ApiKeys.OPEN_WEATHER_API_KEY; // Use from Config class
        apiKeyField = new JTextField(apiKey, 30);
        menu = new DropDownUI(apiKey);

        // Call setupUI method to configure UI elements
        setupUI();

        // Add Action Listeners for buttons
        setupEventListeners();
    }

    private void setupUI() {
        // Initialize Components
        locationLabel = new JLabel("Location:");
        temperatureLabel = new JLabel("Temperature:");
        conditionLabel = new JLabel("Condition:");
        humidityLabel = new JLabel("Humidity:");
        windLabel = new JLabel("Wind:");

        temperatureValue = new JLabel("N/A");
        conditionValue = new JLabel("N/A");
        humidityValue = new JLabel("N/A");
        windValue = new JLabel("N/A");

        getInfoButton = new JButton("Get Info");
        refreshButton = new JButton("Refresh");
        rangeOfTimeButton = new JButton("Range of Time");
        simulationButton = new JButton("Simulation");

        // Create and configure layout
        JPanel panel = new JPanel();
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
        clearUIFields();
        menu.resetSelection();
    }

    private void clearUIFields() {
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

        updateWeatherUI(weatherData, chosenLocation);

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
    String apiKey = getAPIKeyFieldValue();
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


    private void updateWeatherUI(WeatherData weatherData, Location location) {
    // Extract data and update fields
    LocalDate date = LocalDate.now();
    WeatherDataDTO dto = createWeatherDataDTO(weatherData.getWeatherDetails(), location, date);
    updateTextFields(dto);
    String condition = fetchWeatherAlerts(location); // Fetch weather alerts
    conditionValue.setText(condition);
}

    private String getAPIKeyFieldValue(){
        return apiKeyField.getText();
    }

    private WeatherDataDTO createWeatherDataDTO(JSONObject data, Location location, LocalDate date) {
        try {
            final JSONObject mainData = data.getJSONObject("main");
            final double temperatureInCelcius = mainData.getDouble("temp");
            return new WeatherDataDTO(
                    location.getCity(),
                    date,
                    temperatureInCelcius,
                    mainData.getInt("humidity"),
                    data.getJSONObject("wind").getDouble("speed"),
                    //NOTE: may not always have percipitation data and alerts
                    0.0, new ArrayList<String>(){ });
        }
        catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private void extractWeatherData(WeatherData weatherData, Location location, LocalDate date) {
        if (weatherData != null) {
            this.weatherDataDTO = createWeatherDataDTO(weatherData.getWeatherDetails(), location, date);
        }
        else {
            System.out.println("Error: Could not retrieve weather data.");
        }
    }

    private void updateTextFields(WeatherDataDTO weatherDataDTO) {
        final DecimalFormat df = new DecimalFormat("#.##");
        temperatureValue.setText(df.format(weatherDataDTO.getTemperature(temperatureType)) + " °C");
        humidityValue.setText(weatherDataDTO.humidity + " %");
        windValue.setText(weatherDataDTO.windSpeed + " m/s");
    }

    private void openInvalidLocationWindow() {
        final JFrame rangeWindow = new JFrame("Invalid Location");
        rangeWindow.setSize(400, 200);
        rangeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final JLabel placeholderLabel = new JLabel("The location you entered is invalid. Please try again!", SwingConstants.CENTER);
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

    public static void main(String[] args) {
        final DashboardUI frame = new DashboardUI();

        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
}
