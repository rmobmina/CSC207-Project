package presentation.ui;

import application.dto.WeatherDataDTO;
import application.dto.WeatherDataDTOGenerator;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetWeatherDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

public class DashboardUI extends JFrame {
    static final JLabel locationLabel = new JLabel("Location:");
    static String apiKey = "";

    static final JLabel temperatureLabel = new JLabel("Temperature:");
    static final JLabel temperatureValue = new JLabel("N/A");
    static final String temperatureType = "cel";
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
    static final JButton compareTwoCitiesButton = new JButton("Compare Two Cities");

    static final JFrame rangeWindow = new JFrame("Range of Time");

    static LocalDate startDate = LocalDate.now();
    static LocalDate endDate = LocalDate.now();
    WeatherDataDTO weatherDataDTO;
    static OpenWeatherApiService apiService;
    static final DropDownUI menu = new DropDownUI();

    public DashboardUI() {
        setTitle("Weather Dashboard");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2, 10, 10)); // Updated layout for additional components

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
        panel.add(compareTwoCitiesButton);

        initRangeWindowComponents();
        add(panel);

        // Existing buttons' action listeners
        getInfoButton.addActionListener(e -> displayWeatherData());
        refreshButton.addActionListener(e -> refreshAll());
        updateRangeOfTimeButton.addActionListener(e -> updateRangeOfTime());
        setRangeOfTimeButton.addActionListener(e -> openRangeOfTimeWindow());

        // New button for comparing two cities
        compareTwoCitiesButton.addActionListener(e -> openCompareTwoCitiesWindow());
    }

    public void setAPIkey(String newAPIkey) {
        this.apiKey = newAPIkey;
    }

    private void updateRangeOfTime() {
        try {
            LocalDate newStart = LocalDate.parse(startDateField.getText());
            LocalDate newEnd = LocalDate.parse(endDateField.getText());
            startDate = newStart;
            endDate = newEnd;
            rangeWindow.setVisible(false);
        } catch (Exception e) {
            openErrorWindow("Invalid Date entered.");
        }
    }

    private void refreshAll() {
        menu.getLocationField().setText("");
        showWeatherDataFields(false);
    }

    private void displayWeatherData() {
        GetLocationDataUseCase locationUseCase = new GetLocationDataUseCase(apiService);
        GetWeatherDataUseCase weatherUseCase = new GetWeatherDataUseCase(apiService);
        WeatherDataDTOGenerator weatherDataGenerator = new WeatherDataDTOGenerator();
        String city = menu.getLocationField().getText();

        try {
            Location chosenLocation = locationUseCase.execute(city, apiKey).get(0);
            if (chosenLocation != null) {
                showWeatherDataFields(true);
                WeatherData weatherData = weatherUseCase.execute(chosenLocation, startDate, endDate);
                weatherDataDTO = weatherDataGenerator.createWeatherDataDTO(weatherData, chosenLocation, startDate, endDate);
                updateWeatherDataTextFields(weatherDataDTO);
            } else {
                openErrorWindow("Invalid Location entered.");
            }
        } catch (Exception e) {
            openErrorWindow("Error retrieving weather data.");
        }
    }

    private void updateWeatherDataTextFields(WeatherDataDTO weatherDataDTO) {
        DecimalFormat df = new DecimalFormat("#.##");
        temperatureValue.setText(df.format(weatherDataDTO.getTemperature(temperatureType)) + " °C");
        humidityValue.setText(weatherDataDTO.humidity + " %");
        percipitationValue.setText(weatherDataDTO.precipitation + " mm");
        windValue.setText(weatherDataDTO.windSpeed + " km/h " + weatherDataDTO.windDirection + " °");
    }

    private void openRangeOfTimeWindow() {
        rangeWindow.setVisible(true);
    }

    private void initRangeWindowComponents() {
        rangeWindow.setVisible(false);
        rangeWindow.setSize(300, 200);
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

    private void openCompareTwoCitiesWindow() {
        SwingUtilities.invokeLater(() -> {
            TwoCitiesWeatherUI compareTwoCitiesFrame = new TwoCitiesWeatherUI(apiService, apiKey);
            compareTwoCitiesFrame.setVisible(true);
        });
    }

    private void showWeatherDataFields(boolean enable) {
        temperatureValue.setVisible(enable);
        percipitationValue.setVisible(enable);
        humidityValue.setVisible(enable);
        windValue.setVisible(enable);
    }

    private void openErrorWindow(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        OpenWeatherApiService weatherApiService = new OpenWeatherApiService();
        String testApiKey = "YOUR_API_KEY_HERE";

        SwingUtilities.invokeLater(() -> {
            DashboardUI dashboard = new DashboardUI();
            dashboard.setAPIkey(testApiKey);
            dashboard.runJFrame(weatherApiService);
        });
    }

    public void runJFrame(OpenWeatherApiService weatherApiService) {
        apiService = weatherApiService;
        this.showWeatherDataFields(true);
        SwingUtilities.invokeLater(() -> this.setVisible(true));
    }
}
