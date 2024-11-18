package presentation.ui;

import application.dto.WeatherDataDTO;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetWeatherDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;
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
    // implemented variables, might not end up using all depending
    static final JLabel locationLabel = new JLabel("Location:");
    static final JLabel apiKeyLabel = new JLabel("API Key:");
    static final JTextField apiKeyField = new JTextField(30);
    static final JLabel temperatureLabel = new JLabel("Temperature:");
    static final JLabel temperatureValue = new JLabel("N/A"); // Placeholder for temperature
    static final String temperatureType = "cel";
    static final JLabel conditionLabel = new JLabel("Condition:");
    static final JLabel conditionValue = new JLabel("N/A"); // Placeholder for weather condition
    static final JLabel humidityLabel = new JLabel("Humidity:");
    static final JLabel humidityValue = new JLabel("N/A"); // Placeholder for humidity
    static final JLabel windLabel = new JLabel("Wind:");
    static final JLabel windValue = new JLabel("N/A"); // Placeholder for wind speed
    static final JButton getInfoButton = new JButton("Get Info");
    static final JButton refreshButton = new JButton("Refresh");
    static final JButton rangeOfTimeButton = new JButton("Range of Time");
    static final JButton simulationButton = new JButton("Simulation");
    static WeatherDataDTO weatherDataDTO = new WeatherDataDTO();
    private String apiKey;
    private final DropDownUI menu;



    // A: main dashboard, its messy for now but we'll split them up for clean architecture and for a cleaner look
    public DashboardUI() {
        setTitle("Weather Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        apiKey = "0e85f616a96a624a0bf65bad89ff68c5"; // Bader's Key
        apiKeyField.setText(apiKey);
        menu = new DropDownUI(apiKey);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 10, 10));

        // B: main compenents of GUI, most are placeholders
        panel.add(locationLabel);
        panel.add(menu);

        panel.add(apiKeyLabel);
        panel.add(apiKeyField);

        panel.add(temperatureLabel);
        panel.add(temperatureValue);

        panel.add(conditionLabel);
        panel.add(conditionValue);

        panel.add(humidityLabel);
        panel.add(humidityValue);

        panel.add(windLabel);
        panel.add(windValue);

        panel.add(getInfoButton);

        // panel.add(new JLabel());
        panel.add(refreshButton);

        // B: buttons for range and simulation
        panel.add(rangeOfTimeButton);
        panel.add(simulationButton);

        add(panel);
        // Button that displays weather data given location and api key
        getInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayWeatherData();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAll();
            }
        });

        // Buttons that make the menus for range of time and the simulation
        rangeOfTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRangeOfTimeWindow();
            }
        });

        simulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSimulationWindow();
            }
        });

    }

    private void refreshAll() {
        apiKeyField.setText(apiKey);
        menu.getLocationField().setText("");
        temperatureValue.setText("N/A");
        conditionValue.setText("N/A");
        humidityValue.setText("N/A");
        windValue.setText("N/A");
    }

    private void displayWeatherData() {
        // Get selected location
        final Location chosenLocation = menu.getSelectedLocation();
        if (chosenLocation == null) {
            System.out.println("No location selected");
            openInvalidLocationWindow();
            return;
        }

        final OpenWeatherApiService apiService = new OpenWeatherApiService();
        apiKey = getAPIKeyFieldValue();

        if (apiKey == null || apiKey.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "API key cannot be empty. Please enter a valid API key.",
                    "Invalid API Key",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        System.out.println("City: " + chosenLocation.getCity());
        System.out.println("Latitude: " + chosenLocation.getLatitude());
        System.out.println("Longitude: " + chosenLocation.getLongitude());

        final GetWeatherDataUseCase weatherUseCase = new GetWeatherDataUseCase(apiService);
        final WeatherData weatherData = weatherUseCase.execute(chosenLocation, apiKey);

        if (weatherData != null) {
            final LocalDate date = LocalDate.now();
            extractWeatherData(weatherData, chosenLocation, date);
            updateTextFields(weatherDataDTO);
        }
        else {
            System.out.println("Weather data could not be retrieved.");
            temperatureValue.setText("N/A");
        }
    }

    // B: No usages after i worked on the code, either find a usage or can be removed later.
    private Location showLocationSelectionPopup(List<Location> locations) {
        // Convert the list of locations to a string array for display
        String[] locationStrings = locations.stream()
                .map(loc -> loc.getCity()) // e.g., "London, GB"
                .toArray(String[]::new);

        // Show a dropdown using JOptionPane for the user to select a location
        String selectedLocation = (String) JOptionPane.showInputDialog(
                this,
                "Select a location:",
                "Choose Location",
                JOptionPane.QUESTION_MESSAGE,
                null,
                locationStrings,
                locationStrings[0] // Default to the first location
        );

        if (selectedLocation != null) {
            // Find and return the selected Location object
            return locations.stream()
                    .filter(loc -> loc.getCity().equals(selectedLocation))
                    .findFirst()
                    .orElse(null);
        }
        return null; // Return null if the selection is canceled
    }



    private String getAPIKeyFieldValue(){
        return apiKeyField.getText();
    }

    private WeatherDataDTO createWeatherDataDTO(JSONObject data, Location location, LocalDate date) {
        try {
            final JSONObject mainData = data.getJSONObject("main");
            final double temperatureInCelsius = mainData.getDouble("temp");
            return new WeatherDataDTO(
                    location.getCity(),
                    date,
                    temperatureInCelsius,
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
