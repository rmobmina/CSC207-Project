package presentation.ui.windows;

import application.dto.WeatherDataDTO;
import application.dto.WeatherDataDTOGenerator;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetHistoricalWeatherDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.visualization.BarGraphWeatherComparison;
import presentation.visualization.LineGraphWeatherComparison;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

import utils.Constants;

public class TwoCitiesWeatherUI extends JFrame {
    private final JTextField firstCityField = new JTextField(20);
    private final JTextField secondCityField = new JTextField(20);
    private final JButton fetchWeatherButton = new JButton("Fetch Weather Data");
    private final JButton refreshButton = new JButton("Refresh");
    private final JButton visualizeButton = new JButton("Visualize!");
    private final JButton backButton = new JButton("Back");

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
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(14, 2, 10, 10));

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
        mainPanel.add(backButton);

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
        GetHistoricalWeatherDataUseCase weatherUseCase = new GetHistoricalWeatherDataUseCase(apiService);
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
            firstCityTemperature.setText("City 1 Temperature: " + df.format(firstCityDTO.temperatureToString("temperatureMeanDaily", 0, Constants.CELCIUS_UNIT_TYPE)));
            secondCityTemperature.setText("City 2 Temperature: " + df.format(secondCityDTO.temperatureToString("temperatureMeanDaily", 0, Constants.CELCIUS_UNIT_TYPE)));

            firstCityPrecipitation.setText("City 1 Precipitation: " + firstCityDTO.dataToString("precipitation", 0));
            secondCityPrecipitation.setText("City 2 Precipitation: " + secondCityDTO.dataToString("precipitation", 0));

            firstCityHumidity.setText("City 1 Humidity: " + firstCityDTO.dataToString("humidityHourly", 0));
            secondCityHumidity.setText("City 2 Humidity: " + secondCityDTO.dataToString("humidityHourly", 0));

            firstCityWindSpeed.setText("City 1 Wind Speed: " + firstCityDTO.dataToString("windSpeed", 0) + firstCityDTO.dataToString("windDirection", 0));
            secondCityWindSpeed.setText("City 2 Wind Speed: " + secondCityDTO.dataToString("windSpeed", 0) + secondCityDTO.dataToString("windDirection", 0));
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

        SwingUtilities.invokeLater(() -> {
            GraphSelectionWindow graphWindow = new GraphSelectionWindow(graphType -> {
                if ("line".equals(graphType)) {
                    LineGraphWeatherComparison lineGraph = new LineGraphWeatherComparison("Two Cities Weather Comparison");
                    firstCityWeatherData.getTemperatureHistory().forEach((date, value) ->
                            lineGraph.addData("City 1 Temperature", date, value)
                    );
                    secondCityWeatherData.getTemperatureHistory().forEach((date, value) ->
                            lineGraph.addData("City 2 Temperature", date, value)
                    );
                    lineGraph.display();
                } else if ("bar".equals(graphType)) {
                    BarGraphWeatherComparison barGraph = new BarGraphWeatherComparison("Two Cities Weather Comparison");
                    barGraph.addData("City 1", "Temperature", firstCityWeatherData.getAverageWeatherData("temperatureMeanDaily"));
                    barGraph.addData("City 1", "Precipitation", firstCityWeatherData.getAverageWeatherData("precipitationDaily"));
                    barGraph.addData("City 1", "Humidity", firstCityWeatherData.getAverageWeatherData("humidityHourly"));
                    barGraph.addData("City 1", "Wind Speed", firstCityWeatherData.getAverageWeatherData("windSpeedDaily"));

                    barGraph.addData("City 2", "Temperature", secondCityWeatherData.getAverageWeatherData("temperatureMeanDaily"));
                    barGraph.addData("City 2", "Precipitation", secondCityWeatherData.getAverageWeatherData("precipitationDaily"));
                    barGraph.addData("City 2", "Humidity", secondCityWeatherData.getAverageWeatherData("humidityHourly"));
                    barGraph.addData("City 2", "Wind Speed", secondCityWeatherData.getAverageWeatherData("windSpeedDaily"));

                    barGraph.display();
                }
            });
            graphWindow.setVisible(true);
        });
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
