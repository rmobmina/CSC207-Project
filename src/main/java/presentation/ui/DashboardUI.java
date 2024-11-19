package presentation.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import application.dto.WeatherDataDTO;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetWeatherDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;
import utils.Constants;

public class DashboardUI extends JFrame {
    // implemented variables, might not end up using all depending
    static final JLabel LOCATION_LABEL = new JLabel("Location:");
    static final JTextField LOCATION_FIELD = new JTextField(20);
    static final JLabel API_KEY_LABEL = new JLabel("API Key:");
    static final JTextField API_KEY_FIELD = new JTextField(30);

    static final JLabel TEMPERATURE_LABEL = new JLabel("Temperature:");

    // Placeholder for temperature
    static final JLabel TEMPERATURE_VALUE = new JLabel("N / A");
    static final String TEMPERATURE_TYPE = "cel";
    static final JLabel PRECIPITATION_LABEL = new JLabel("Percipitation Sum:");

    // Placeholder for weather condition
    static final JLabel PRECIPITATION_VALUE = new JLabel("N / A");
    static final JLabel HUMIDITY_LABEL = new JLabel("Humidity:");

    // Placeholder for humidity
    static final JLabel HUMIDITY_VALUE = new JLabel("N / A");
    static final JLabel WIND_LABEL = new JLabel("Wind:");

    // Placeholder for wind speed
    static final JLabel WIND_VALUE = new JLabel("N / A");

    static final JLabel START_DATE_LABEL = new JLabel("Start Date:");
    static final JTextField START_DATE_FIELD = new JTextField(20);
    static final JLabel END_DATE_LABEL = new JLabel("End Date:");
    static final JTextField END_DATE_FIELD = new JTextField(20);

    static final JButton GET_INFO_BUTTON = new JButton("Get Info");
    static final JButton UPDATE_RANGE_OF_TIME = new JButton("Update");
    static final JButton REFRESH_BUTTON = new JButton("Refresh");
    static final JButton SET_RANGE_TIME_OF_BUTTON = new JButton("Set Range of Time");
    static final JButton SIMULATION_BUTTON = new JButton("Simulation");

    static final JFrame RANGE_WINDOW = new JFrame("Range of Time");

    private static LocalDate startDate = LocalDate.now();
    private static LocalDate endDate = LocalDate.now();
    private static WeatherDataDTO weatherDataDto = new WeatherDataDTO();
    private static final DropDownUI DROP_MENU = new DropDownUI();

    // A: main dashboard, its messy for now but we'll split them up for clean architecture and for a cleaner look
    public DashboardUI() {
        setTitle("Weather Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10)); // B: Will be changed as we add more elements

        // B: main compenents of GUI, most are placeholders
        panel.add(LOCATION_LABEL);
        panel.add(DROP_MENU);

        panel.add(API_KEY_LABEL);
        panel.add(API_KEY_FIELD);

        panel.add(TEMPERATURE_LABEL);
        panel.add(TEMPERATURE_VALUE);

        panel.add(PRECIPITATION_LABEL);
        panel.add(PRECIPITATION_VALUE);

        panel.add(HUMIDITY_LABEL);
        panel.add(HUMIDITY_VALUE);

        panel.add(WIND_LABEL);
        panel.add(WIND_VALUE);

        panel.add(GET_INFO_BUTTON);

        // panel.add(new JLabel());
        panel.add(REFRESH_BUTTON);

        // B: buttons for range and simulation
        panel.add(SET_RANGE_TIME_OF_BUTTON);
        panel.add(SIMULATION_BUTTON);

        add(panel);
        // Button that displays weather data given location and api key
        GET_INFO_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayWeatherData();
            }
        });

        // Adding actionListeners to each button
        //      This button that resets all info/visuals and input from the user
        REFRESH_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAll();
            }
        });

        UPDATE_RANGE_OF_TIME.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRangeOfTime();
            }
        });

        // Buttons that make the menus for range of time and the simulation
        SET_RANGE_TIME_OF_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRangeOfTimeWindow();
            }
        });

        SIMULATION_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSimulationWindow();
            }
        });
    }

    private String getAPIKeyFieldValue(){
        return API_KEY_FIELD.getText();
    }

    private String getLocationFieldValue() {
        return DROP_MENU.getLocationField().getText();
    }

    private static void updateRangeOfTime() {
        startDate = LocalDate.parse(START_DATE_FIELD.getText());
        endDate = LocalDate.parse(END_DATE_FIELD.getText());
        RANGE_WINDOW.setVisible(false);
    }

    private void refreshAll() {
        API_KEY_FIELD.setText("");
        DROP_MENU.getLocationField().setText("");
        TEMPERATURE_VALUE.setText("N / A");
        PRECIPITATION_VALUE.setText("N / A");
        HUMIDITY_VALUE.setText("N / A");
        WIND_VALUE.setText("N / A");
    }

    private void displayWeatherData() {
        final OpenWeatherApiService apiService = new OpenWeatherApiService();
        final GetLocationDataUseCase locationUseCase = new GetLocationDataUseCase(apiService);
        final GetWeatherDataUseCase weatherUseCase = new GetWeatherDataUseCase(apiService);
        final String apiKey = getAPIKeyFieldValue();
        final String city = getLocationFieldValue();
        final Location chosenLocation = locationUseCase.execute(city, apiKey);

        if (chosenLocation != null) {
            final WeatherData weatherData = weatherUseCase.execute(chosenLocation, startDate, endDate);
            extractWeatherData(weatherData, chosenLocation, startDate);
            updateWeatherDataTextFields(weatherDataDto);
        }
        else {
            openInvalidLocationWindow();
        }
    }

    private WeatherDataDTO createWeatherDataDTO(JSONObject data, Location location, LocalDate date) {
        try {
            // Change this...
            final JSONObject dailyData = data.getJSONObject("daily");
            final JSONObject hourlyData = data.getJSONObject("hourly");

            // NOTE: Don't have alerts (for now)
            return new WeatherDataDTO(
                    location.getCity(),
                    date,
                    WeatherDataDTO.getAverageData(hourlyData.getJSONArray("temperature_2m")),
                    (int) WeatherDataDTO.getAverageData(hourlyData.getJSONArray("relative_humidity_2m")),
                    dailyData.getJSONArray("wind_speed_10m_max").getDouble(0),
                    dailyData.getJSONArray("precipitation_sum").getInt(0),
                    new ArrayList<String>() { });
        }
        catch (JSONException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private void extractWeatherData(WeatherData weatherData, Location location, LocalDate date) {
        if (weatherData != null) {
            this.weatherDataDto = createWeatherDataDTO(weatherData.getWeatherDetails(), location, date);
        }
        else {
            System.out.println("Error: Could not retrieve weather data.");
        }
    }

    // Updates all the weather data text fields given the current location and dates
    private void updateWeatherDataTextFields(WeatherDataDTO weatherDto) {
        final DecimalFormat df = new DecimalFormat("#.##");
        TEMPERATURE_VALUE.setText(df.format(weatherDto.getTemperature(TEMPERATURE_TYPE)) + " °C");
        HUMIDITY_VALUE.setText(weatherDto.humidity + " %");
        PRECIPITATION_VALUE.setText(weatherDto.precipitation + "mm");
        WIND_VALUE.setText(weatherDto.windSpeed + " km/h");
    }

    private void openInvalidLocationWindow() {
        final JFrame errorWindow = new JFrame("Invalid Location");
        errorWindow.setSize(400, 200);
        errorWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        final JLabel placeholderLabel = new JLabel("The location you entered is invalid. Please try again!", SwingConstants.CENTER);
        errorWindow.add(placeholderLabel);
        errorWindow.setVisible(true);
    }

    // Placeholder window for "Range of Time" feature
    private void openRangeOfTimeWindow() {
        RANGE_WINDOW.setSize(Constants.SIDE_WINDOW_WIDTH, Constants.SIDE_WINDOW_HEIGHT);
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(Constants.GRID_ROWS, Constants.GRID_COLS, Constants.GRID_GAP,
                Constants.GRID_GAP));

        RANGE_WINDOW.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel.add(START_DATE_LABEL);
        panel.add(START_DATE_FIELD);
        panel.add(END_DATE_LABEL);
        panel.add(END_DATE_FIELD);
        panel.add(UPDATE_RANGE_OF_TIME);
        RANGE_WINDOW.add(panel);
        RANGE_WINDOW.setVisible(true);
    }

    // Placeholder window for simulation
    private void openSimulationWindow() {
        final JFrame simulationWindow = new JFrame("Simulation");
        simulationWindow.setSize(Constants.SIDE_WINDOW_WIDTH, Constants.SIDE_WINDOW_HEIGHT);
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
