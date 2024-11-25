package presentation.ui;

import application.dto.WeatherDataDTO;
import application.dto.WeatherDataDTOGenerator;
import application.usecases.GetForcastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

public class GetAveragesOfTwoCitiesUI extends JFrame {
    private final JLabel city1Label = new JLabel("City 1:");
    private final JLabel city2Label = new JLabel("City 2:");
    private final JLabel averageTemperatureLabel = new JLabel("Average Temperature:");
    private final JLabel averageHumidityLabel = new JLabel("Average Humidity:");
    private final JLabel averagePrecipitationLabel = new JLabel("Average Precipitation:");

    private final JLabel avgTempValue = new JLabel("N/A");
    private final JLabel avgHumidityValue = new JLabel("N/A");
    private final JLabel avgPrecipitationValue = new JLabel("N/A");

    private final DropDownUI city1Dropdown = new DropDownUI();
    private final DropDownUI city2Dropdown = new DropDownUI();
    private final JButton calculateButton = new JButton("Calculate Averages");

    private final OpenWeatherApiService apiService;
    private final GetForcastWeatherDataUseCase forecastWeatherUseCase;
    private final GetLocationDataUseCase locationUseCase;

    private String apiKey;

    public GetAveragesOfTwoCitiesUI(OpenWeatherApiService apiService, String apiKey) {
        this.apiService = apiService;
        this.apiKey = apiKey;
        this.forecastWeatherUseCase = new GetForcastWeatherDataUseCase(apiService);
        this.locationUseCase = new GetLocationDataUseCase(apiService);

        setTitle("Average Weather Between Two Cities");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        city1Dropdown.setApiKey(apiKey);
        city2Dropdown.setApiKey(apiKey);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        panel.add(city1Label);
        panel.add(city1Dropdown);
        panel.add(city2Label);
        panel.add(city2Dropdown);
        panel.add(averageTemperatureLabel);
        panel.add(avgTempValue);
        panel.add(averageHumidityLabel);
        panel.add(avgHumidityValue);
        panel.add(averagePrecipitationLabel);
        panel.add(avgPrecipitationValue);
        panel.add(new JLabel()); // Empty space
        panel.add(calculateButton);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAndDisplayAverages();
            }
        });

        add(panel);
    }

    private void calculateAndDisplayAverages() {
        Location city1 = city1Dropdown.getSelectedLocation();
        Location city2 = city2Dropdown.getSelectedLocation();

        if (city1 == null || city2 == null) {
            showErrorDialog("Please select both cities.");
            return;
        }

        try {
            WeatherData weatherData1 = forecastWeatherUseCase.execute(city1, 7);
            WeatherData weatherData2 = forecastWeatherUseCase.execute(city2, 7);

            if (weatherData1 == null || weatherData2 == null) {
                showErrorDialog("Failed to retrieve weather data for one or both cities.");
                return;
            }

            WeatherDataDTO weatherDataDTO1 = WeatherDataDTOGenerator.createWeatherDataDTO(weatherData1, city1);
            WeatherDataDTO weatherDataDTO2 = WeatherDataDTOGenerator.createWeatherDataDTO(weatherData2, city2);

            updateAverageWeatherDataTextFields(weatherDataDTO1, weatherDataDTO2);

        } catch (Exception ex) {
            showErrorDialog("An error occurred: " + ex.getMessage());
        }
    }

    private void updateAverageWeatherDataTextFields(WeatherDataDTO weatherDataDTO1, WeatherDataDTO weatherDataDTO2) {
        DecimalFormat df = new DecimalFormat("#.##");

        double avgTemp = (weatherDataDTO1.getTemperature("cel", 0) + weatherDataDTO2.getTemperature("cel", 0)) / 2;
        double avgHumidity = (weatherDataDTO1.humidity.get(0) + weatherDataDTO2.humidity.get(0)) / 2;
        double avgPrecipitation = (weatherDataDTO1.precipitation.get(0) + weatherDataDTO2.precipitation.get(0)) / 2;

        avgTempValue.setText(df.format(avgTemp) + " °C");
        avgHumidityValue.setText(df.format(avgHumidity) + " %");
        avgPrecipitationValue.setText(df.format(avgPrecipitation) + " mm");
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        OpenWeatherApiService apiService = new OpenWeatherApiService();
        String apiKey = "2d6d6124e844c3e976842b19833dfa3b";

        SwingUtilities.invokeLater(() -> {
            GetAveragesOfTwoCitiesUI frame = new GetAveragesOfTwoCitiesUI(apiService, apiKey);
            frame.setVisible(true);
        });
    }
}
