package presentation.ui;

import application.dto.WeatherDataDTO;
import application.dto.WeatherDataDTOGenerator;
import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetHistoricalWeatherDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;
import utils.Constants;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

// Old dashboardUI class just in case (to be replaced with DashBoardUi)
public class DashboardUI extends JFrame {
    static final JLabel locationLabel = new JLabel("Location:");

    static String apiKey = "";

    static final JLabel temperatureLabel = new JLabel("Temperature:");
    static final JLabel temperatureValue = new JLabel("N/A");
    static final String TEMPERATURE_TYPE = "cel";
    static final JLabel percipitationLabel = new JLabel("Percipitation Sum:");
    static final JLabel percipitationValue = new JLabel("N/A");
    static final JLabel humidityLabel = new JLabel("Humidity:");
    static final JLabel humidityValue = new JLabel("N/A");
    static final JLabel windLabel = new JLabel("Wind Speed (and Dominant Direction):");
    static final JLabel windValue = new JLabel("N/A");

    static final JLabel startDateLabel = new JLabel("Start Date:");
    static final JTextField startDateField = new JTextField(20);
    static final JLabel endDateLabel = new JLabel("End Date:");
    static final JTextField endDateField = new JTextField(20);

    static final JButton getInfoButton = new JButton("Get Info");
    static final JButton updateRangeOfTimeButton = new JButton("Update");
    static final JButton refreshButton = new JButton("Refresh");
    static final JButton setRangeOfTimeButton = new JButton("Set Range of Time");

    static final JFrame rangeWindow = new JFrame("Range of Time");

    static final JPanel historicalPanel = new JPanel();
    static final JPanel forcastPanel = new JPanel();

    static LocalDate startDate = LocalDate.of(2021, 1, 1);
    static LocalDate endDate = LocalDate.of(2021, 1, 2);
    static String userOption = "Historical";
    WeatherDataDTO weatherDataDTO;
    static OpenWeatherApiService apiService;
    static final DropDownUI menu = new DropDownUI(apiKey, new GetLocationDataUseCase(apiService));

    // A: main dashboard, its messy for now but we'll split them up for clean architecture and for a cleaner look
    public DashboardUI() {
        setTitle("Weather Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initRangeWindowComponents();

        addForcastPanel();

        addHistoricalPanel();

        // Button that displays weather data given location and api key
        getInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayWeatherData();
            }
        });

        // Adding actionListeners to each button

        // Button that resets all info/visuals and input from the user
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAll();
            }
        });

        updateRangeOfTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRangeOfTime();
            }
        });

        // Buttons that make the menus for range of time and the simulation
        setRangeOfTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRangeOfTimeWindow();
            }
        });
    }

    private void addForcastPanel(){
        int number_of_hours = 8;
        forcastPanel.setLayout(new GridLayout(8, 2, 10, 10));
        addMainComponents(forcastPanel);

        String city = getLocationFieldValue();
        if (city == null || city.trim().isEmpty()) {
            return;    // exit if no city is entered
        }
        // adding column headers
        forcastPanel.add(new JLabel("Hour", SwingConstants.CENTER));
        forcastPanel.add(new JLabel("Temperature (Celcius)", SwingConstants.CENTER));

        // displaying only 8 hours of forecast
        for (int i = 0; i < number_of_hours; i++) {
            forcastPanel.add(new JLabel("Hour" + (i + 1), SwingConstants.CENTER));
            forcastPanel.add(new JLabel(String.format("%.1f",weatherDataDTO.getWeatherDataValue("temperatureHourly", i)), SwingConstants.CENTER));
        }

        add(forcastPanel);
    }

    private void addHistoricalPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));
        addMainComponents(panel);

        // To be completed later by Akram

        // Later, include visualization for Historical weather data

        add(panel);
    }

    private void addMainComponents(JPanel panel){
        panel.add(locationLabel);
        panel.add(menu);

        panel.add(temperatureLabel);
        panel.add(temperatureValue);

        panel.add(percipitationLabel);
        panel.add(percipitationValue);

        panel.add(humidityLabel);
        panel.add(humidityValue);

        panel.add(windLabel);
        panel.add(windValue);

        panel.add(getInfoButton);

        panel.add(refreshButton);

        panel.add(setRangeOfTimeButton);
    }

    public void setAPIkey(String newAPIkey){
        this.apiKey = newAPIkey;
    }

    private String getLocationFieldValue() {
        return menu.getLocationField().getText();
    }

    private void updateRangeOfTime() {
        LocalDate newStart = startDate;
        LocalDate newEnd = endDate;
        // In case the user enters a date that is invalid
        try {
            newStart = LocalDate.parse(startDateField.getText());
            newEnd = LocalDate.parse(endDateField.getText());
        }
        catch (DateTimeException e) {
            openErrorWindow("Invalid Date entered.");
        }
        startDate = newStart;
        endDate = newEnd;
        rangeWindow.setVisible(false);
    }

    public void showWeatherDataFields(boolean enable) {
        temperatureValue.setVisible(enable);
        percipitationValue.setVisible(enable);
        humidityValue.setVisible(enable);
        windValue.setVisible(enable);
    }

    private void refreshAll() {
        menu.getLocationField().setText("");
        showWeatherDataFields(false);
    }

    private void displayWeatherData() {
        GetLocationDataUseCase locationUseCase = new GetLocationDataUseCase(apiService);
        GetHistoricalWeatherDataUseCase historicalWeatherDataUseCase = new GetHistoricalWeatherDataUseCase(apiService);
        GetForecastWeatherDataUseCase forcastWeatherDataUseCase = new GetForecastWeatherDataUseCase(apiService);
        String city = getLocationFieldValue();

        // TODO: implement user choice
        List<Location> possibleLocations = locationUseCase.execute(city, apiKey);
        if (!possibleLocations.isEmpty()) {
            Location chosenLocation = possibleLocations.get(0);
            showWeatherDataFields(true);
            WeatherData weatherData;

            // To be changed later to adhere to CA
            if (userOption.equals("Forcast")) {
                weatherData = forcastWeatherDataUseCase.execute(chosenLocation, 7);
            }
            else{
                weatherData = historicalWeatherDataUseCase.execute(chosenLocation, startDate, endDate);
            }
            weatherDataDTO = WeatherDataDTOGenerator.createWeatherDataDTO(weatherData, chosenLocation, startDate, endDate);
            updateWeatherDataTextFields(weatherDataDTO);
        }
        else
        {
            openErrorWindow("Invalid Location entered. ");
        }
    }

    // Updates all the weather data text fields given the current location and dates
    private void updateWeatherDataTextFields(WeatherDataDTO weatherDataDTO) {
        DecimalFormat df = new DecimalFormat("#.##");
        temperatureValue.setText(weatherDataDTO.temperatureToString("temperatureHourly", 0, Constants.CELCIUS_UNIT_TYPE));
//        humidityValue.setText(weatherDataDTO.getWeatherData("humidityHourly").get(0) + " %");
//        percipitationValue.setText(weatherDataDTO.precipitation.get(0) + " mm");
//        windValue.setText(weatherDataDTO.windSpeed.get(0) + " km/h " + weatherDataDTO.windDirection.get(0) + " °");
    }

    private void openErrorWindow(String errorMessage) {
        JFrame errorWindow = new JFrame("Error Window");
        errorWindow.setSize(400, 200);
        errorWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Default error message to be displayed when no input is given
        if (errorMessage.isEmpty()) {
            errorMessage = "Invalid information entered.";
        }
        JLabel placeholderLabel = new JLabel("ERROR: " + errorMessage + " Please try again!", SwingConstants.CENTER);
        errorWindow.add(placeholderLabel);
        errorWindow.setVisible(true);
    }

    private void openRangeOfTimeWindow() {
        rangeWindow.setVisible(true);
    }

    // Initializes the components
    // checkstyle says that you must add a space when starting comments.
    private void initRangeWindowComponents() {
        rangeWindow.setVisible(false);
        rangeWindow.setSize(300, 500);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        rangeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel.add(startDateLabel);
        panel.add(startDateField);
        panel.add(endDateLabel);
        panel.add(endDateField);
        panel.add(updateRangeOfTimeButton);
        rangeWindow.add(panel);
    }

    public void runJFrame(OpenWeatherApiService weatherApiService) {
        apiService = weatherApiService;
        this.showWeatherDataFields(true);
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }

    public static void main(String[] args) {
        DashboardUI frame = new DashboardUI();
        frame.refreshAll();
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
}
