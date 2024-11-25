package presentation.ui;

import application.dto.WeatherDataDTO;
import application.dto.WeatherDataDTOGenerator;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetWeatherDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.visualization.BarGraphVisualization;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

public class TwoCitiesWeatherUI extends JFrame {
    private final JTextField firstCityField = new JTextField(20);
    private final JTextField secondCityField = new JTextField(20);
    private final JButton fetchWeatherButton = new JButton("Fetch Weather Data");
    private final JButton refreshButton = new JButton("Refresh");
    private final JButton visualizeButton = new JButton("Visualize!");

    private final JLabel firstCityTemperature = new JLabel("City 1 Temperature: N/A", SwingConstants.LEFT);
    private final JLabel secondCityTemperature = new JLabel("City 2 Temperature: N/A", SwingConstants.LEFT);

    private final JLabel firstCityPrecipitation = new JLabel("City 1 Precipitation: N/A", SwingConstants.LEFT);
    private final JLabel secondCityPrecipitation = new JLabel("City 2 Precipitation: N/A", SwingConstants.LEFT);

    private final JLabel firstCityHumidity = new JLabel("City 1 Humidity: N/A", SwingConstants.LEFT);
    private final JLabel secondCityHumidity = new JLabel("City 2 Humidity: N/A", SwingConstants.LEFT);

    private final JLabel firstCityWindSpeed = new JLabel("City 1 Wind Speed: N/A", SwingConstants.LEFT);
    private final JLabel secondCityWindSpeed = new JLabel("City 2 Wind Speed: N/A", SwingConstants.LEFT);

    private final OpenWeatherApiService apiService;
    private final String apiKey;

    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now();

    private WeatherDataDTO firstCityWeatherData;
    private WeatherDataDTO secondCityWeatherData;

    public TwoCitiesWeatherUI(OpenWeatherApiService apiService, String apiKey) {
        this.apiService = apiService;
        this.apiKey = apiKey;

        setTitle("Weather Comparison for Two Cities");
        setSize(600, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(13, 2, 10, 10));

        mainPanel.add(new JLabel("First City:"));
        mainPanel.add(firstCityField);

        mainPanel.add(new JLabel("Second City:"));
        mainPanel.add(secondCityField);

        mainPanel.add(fetchWeatherButton);
        mainPanel.add(refreshButton);

        mainPanel.add(firstCityTemperature);
        mainPanel.add(secondCityTemperature);

        mainPanel.add(firstCityPrecipitation);
        mainPanel.add(secondCityPrecipitation);

        mainPanel.add(firstCityHumidity);
        mainPanel.add(secondCityHumidity);

        mainPanel.add(firstCityWindSpeed);
        mainPanel.add(secondCityWindSpeed);

        mainPanel.add(visualizeButton);

        add(mainPanel);

        fetchWeatherButton.addActionListener(e -> handleFetchWeatherData());
        refreshButton.addActionListener(e -> refreshFields());
        visualizeButton.addActionListener(e -> openVisualization());
    }

    private void handleFetchWeatherData() {
        String firstCity = firstCityField.getText();
        String secondCity = secondCityField.getText();

        if (firstCity.isEmpty() || secondCity.isEmpty()) {
            showError("Both city fields must be filled.");
            return;
        }

        GetLocationDataUseCase locationUseCase = new GetLocationDataUseCase(apiService);
        GetWeatherDataUseCase weatherUseCase = new GetWeatherDataUseCase(apiService);
        WeatherDataDTOGenerator dtoGenerator = new WeatherDataDTOGenerator();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    List<Location> firstCityLocations = locationUseCase.execute(firstCity, apiKey);
                    List<Location> secondCityLocations = locationUseCase.execute(secondCity, apiKey);

                    if (firstCityLocations.isEmpty() || secondCityLocations.isEmpty()) {
                        showError("Invalid city entered. Please try again.");
                        return null;
                    }

                    Location firstLocation = firstCityLocations.get(0);
                    Location secondLocation = secondCityLocations.get(0);

                    WeatherData firstCityWeather = weatherUseCase.execute(firstLocation, startDate, endDate);
                    WeatherData secondCityWeather = weatherUseCase.execute(secondLocation, startDate, endDate);

                    firstCityWeatherData = dtoGenerator.createWeatherDataDTO(firstCityWeather, firstLocation, startDate, endDate);
                    secondCityWeatherData = dtoGenerator.createWeatherDataDTO(secondCityWeather, secondLocation, startDate, endDate);

                    updateWeatherDataFields(firstCityWeatherData, secondCityWeatherData);
                } catch (Exception ex) {
                    showError("Error fetching weather data: " + ex.getMessage());
                }
                return null;
            }
        };

        worker.execute();
    }

    private void updateWeatherDataFields(WeatherDataDTO firstCityDTO, WeatherDataDTO secondCityDTO) {
        DecimalFormat df = new DecimalFormat("#.##");

        SwingUtilities.invokeLater(() -> {
            firstCityTemperature.setText("City 1 Temperature: " + df.format(firstCityDTO.getTemperature("cel")) + " °C");
            secondCityTemperature.setText("City 2 Temperature: " + df.format(secondCityDTO.getTemperature("cel")) + " °C");

            firstCityPrecipitation.setText("City 1 Precipitation: " + firstCityDTO.precipitation + " mm");
            secondCityPrecipitation.setText("City 2 Precipitation: " + secondCityDTO.precipitation + " mm");

            firstCityHumidity.setText("City 1 Humidity: " + firstCityDTO.humidity + " %");
            secondCityHumidity.setText("City 2 Humidity: " + secondCityDTO.humidity + " %");

            firstCityWindSpeed.setText("City 1 Wind Speed: " + firstCityDTO.windSpeed + " km/h " + firstCityDTO.windDirection + "°");
            secondCityWindSpeed.setText("City 2 Wind Speed: " + secondCityDTO.windSpeed + " km/h " + secondCityDTO.windDirection + "°");
        });
    }

    private void refreshFields() {
        SwingUtilities.invokeLater(() -> {
            firstCityField.setText("");
            secondCityField.setText("");

            firstCityTemperature.setText("City 1 Temperature: N/A");
            secondCityTemperature.setText("City 2 Temperature: N/A");

            firstCityPrecipitation.setText("City 1 Precipitation: N/A");
            secondCityPrecipitation.setText("City 2 Precipitation: N/A");

            firstCityHumidity.setText("City 1 Humidity: N/A");
            secondCityHumidity.setText("City 2 Humidity: N/A");

            firstCityWindSpeed.setText("City 1 Wind Speed: N/A");
            secondCityWindSpeed.setText("City 2 Wind Speed: N/A");
        });
    }

    private void openVisualization() {
        if (firstCityWeatherData == null || secondCityWeatherData == null) {
            showError("Fetch weather data for both cities first.");
            return;
        }

        BarGraphVisualization barGraph = new BarGraphVisualization("Two Cities Weather Comparison");

        barGraph.addData("City 1", "Temperature", firstCityWeatherData.getTemperature("cel"));
        barGraph.addData("City 2", "Temperature", secondCityWeatherData.getTemperature("cel"));

        barGraph.addData("City 1", "Precipitation", firstCityWeatherData.precipitation);
        barGraph.addData("City 2", "Precipitation", secondCityWeatherData.precipitation);

        barGraph.addData("City 1", "Humidity", firstCityWeatherData.humidity);
        barGraph.addData("City 2", "Humidity", secondCityWeatherData.humidity);

        barGraph.addData("City 1", "Wind Speed", firstCityWeatherData.windSpeed);
        barGraph.addData("City 2", "Wind Speed", secondCityWeatherData.windSpeed);

        barGraph.display();
    }

    private void showError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        OpenWeatherApiService apiService = new OpenWeatherApiService();
        String apiKey = "YOUR_API_KEY_HERE";

        SwingUtilities.invokeLater(() -> {
            TwoCitiesWeatherUI frame = new TwoCitiesWeatherUI(apiService, apiKey);
            frame.setVisible(true);
        });
    }
}
