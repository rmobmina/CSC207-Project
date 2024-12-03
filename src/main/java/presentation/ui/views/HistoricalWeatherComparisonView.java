package presentation.ui.views;

import application.dto.WeatherDataDTO;
import application.dto.WeatherDataDTOGenerator;
import application.usecases.GetHistoricalWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.DropDownUI;
import presentation.ui.windows.MultipleLocationsWindow;
import presentation.ui.windows.GraphSelectionWindow;
import presentation.visualization.BarGraphWeatherComparison;
import presentation.visualization.LineGraphWeatherComparison;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HistoricalWeatherComparisonView extends MultipleLocationsWindow {

    public static final String OPTION_NAME = "Historical Comparison";

    private final DropDownUI firstCityDropDown;
    private final DropDownUI secondCityDropDown;

    private final JButton fetchWeatherButton = new JButton("Fetch Weather Data");
    private final JButton refreshButton = new JButton("Refresh");
    private final JButton visualizeButton = new JButton("Visualize!");
    private final JButton timeRangeButton = new JButton("Set Time Range");
    private final JButton backButton = new JButton("Back");

    private final JLabel firstCityTemperature = new JLabel("City 1 Temperature: N/A", SwingConstants.LEFT);
    private final JLabel secondCityTemperature = new JLabel("City 2 Temperature: N/A", SwingConstants.LEFT);

    private final JLabel firstCityPrecipitation = new JLabel("City 1 Precipitation: N/A", SwingConstants.LEFT);
    private final JLabel secondCityPrecipitation = new JLabel("City 2 Precipitation: N/A", SwingConstants.LEFT);

    private final JLabel firstCityHumidity = new JLabel("City 1 Humidity: N/A", SwingConstants.LEFT);
    private final JLabel secondCityHumidity = new JLabel("City 2 Humidity: N/A", SwingConstants.LEFT);

    private final JLabel firstCityWindSpeed = new JLabel("City 1 Wind Speed: N/A", SwingConstants.LEFT);
    private final JLabel secondCityWindSpeed = new JLabel("City 2 Wind Speed: N/A", SwingConstants.LEFT);

    private final JTextField startDateField = new JTextField(LocalDate.now().minusDays(7).toString(), 10);
    private final JTextField endDateField = new JTextField(LocalDate.now().toString(), 10);

    private final OpenWeatherApiService apiService;
    private final String apiKey;

    private LocalDate startDate = LocalDate.now().minusDays(7);
    private LocalDate endDate = LocalDate.now();

    private WeatherDataDTO firstCityWeatherData;
    private WeatherDataDTO secondCityWeatherData;

    public HistoricalWeatherComparisonView(String name, int[] dimensions, int numOfLocations,
                                           GetLocationDataUseCase locationDataUseCase, String apiKey,
                                           ApiService apiService) {
        super(name, dimensions, numOfLocations, locationDataUseCase, apiKey, apiService);
        this.apiService = (OpenWeatherApiService) apiService;
        this.apiKey = apiKey;

        this.firstCityDropDown = new DropDownUI(apiKey, locationDataUseCase);
        this.secondCityDropDown = new DropDownUI(apiKey, locationDataUseCase);

        mainPanel.setVisible(false);
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // City Selection Panel
        JPanel citySelectionPanel = new JPanel();
        citySelectionPanel.setLayout(new BoxLayout(citySelectionPanel, BoxLayout.Y_AXIS));
        citySelectionPanel.setBorder(BorderFactory.createTitledBorder("City Selection"));

        citySelectionPanel.add(new JLabel("First City:"));
        citySelectionPanel.add(firstCityDropDown);
        citySelectionPanel.add(Box.createVerticalStrut(10));

        citySelectionPanel.add(new JLabel("Second City:"));
        citySelectionPanel.add(secondCityDropDown);
        citySelectionPanel.add(Box.createVerticalStrut(10));

        citySelectionPanel.add(fetchWeatherButton);
        citySelectionPanel.add(refreshButton);

        // Time Range Panel
        JPanel timeRangePanel = new JPanel();
        timeRangePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        timeRangePanel.setBorder(BorderFactory.createTitledBorder("Select Time Range"));

        timeRangePanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        timeRangePanel.add(startDateField);

        timeRangePanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        timeRangePanel.add(endDateField);

        timeRangePanel.add(timeRangeButton);

        // Weather Data Panel
        JPanel weatherDataPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        weatherDataPanel.setBorder(BorderFactory.createTitledBorder("Weather Data"));

        weatherDataPanel.add(firstCityTemperature);
        weatherDataPanel.add(secondCityTemperature);
        weatherDataPanel.add(firstCityPrecipitation);
        weatherDataPanel.add(secondCityPrecipitation);
        weatherDataPanel.add(firstCityHumidity);
        weatherDataPanel.add(secondCityHumidity);
        weatherDataPanel.add(firstCityWindSpeed);
        weatherDataPanel.add(secondCityWindSpeed);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(visualizeButton);
        buttonPanel.add(backButton);

        // Add sub-panels to the main panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.add(timeRangePanel, BorderLayout.NORTH);
        centerPanel.add(weatherDataPanel, BorderLayout.CENTER);

        mainPanel.add(citySelectionPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event listeners
        fetchWeatherButton.addActionListener(e -> handleFetchWeatherData());
        refreshButton.addActionListener(e -> refreshFields());
        visualizeButton.addActionListener(e -> openVisualization());
        timeRangeButton.addActionListener(e -> handleSetTimeRange());
        backButton.addActionListener(e -> this.dispose());
    }

    private void handleSetTimeRange() {
        try {
            LocalDate newStartDate = LocalDate.parse(startDateField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate newEndDate = LocalDate.parse(endDateField.getText(), DateTimeFormatter.ISO_LOCAL_DATE);

            if (newStartDate.isAfter(newEndDate)) {
                showError("Start date must be before or equal to end date.");
                return;
            }

            startDate = newStartDate;
            endDate = newEndDate;

            showSuccess("Time range updated to: " + startDate + " to " + endDate);
        } catch (DateTimeParseException ex) {
            showError("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    private void handleFetchWeatherData() {
        final Location firstCity = firstCityDropDown.getSelectedLocation();
        final Location secondCity = secondCityDropDown.getSelectedLocation();

        if (firstCity == null || secondCity == null) {
            showError("Both cities must be selected.");
            return;
        }

        System.out.println("Fetching weather data for: " + firstCity.fullLocationName() + " and " + secondCity.fullLocationName());

        final GetHistoricalWeatherDataUseCase weatherUseCase = new GetHistoricalWeatherDataUseCase(apiService);
        final WeatherDataDTOGenerator dtoGenerator = new WeatherDataDTOGenerator();

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    WeatherData firstCityWeather = weatherUseCase.execute(firstCity, startDate, endDate);
                    WeatherData secondCityWeather = weatherUseCase.execute(secondCity, startDate, endDate);

                    firstCityWeatherData = dtoGenerator.createWeatherDataDTO(firstCityWeather, firstCity, startDate, endDate);
                    secondCityWeatherData = dtoGenerator.createWeatherDataDTO(secondCityWeather, secondCity, startDate, endDate);
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> showError("Error fetching weather data: " + ex.getMessage()));
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                if (firstCityWeatherData != null && secondCityWeatherData != null) {
                    updateWeatherDataFields(firstCityWeatherData, secondCityWeatherData);
                }
            }
        };

        worker.execute();
    }

    private void updateWeatherDataFields(WeatherDataDTO firstCityDTO, WeatherDataDTO secondCityDTO) {
        DecimalFormat df = new DecimalFormat("#.##");

        SwingUtilities.invokeLater(() -> {
            firstCityTemperature.setText("City 1 Temperature: " + df.format(firstCityDTO.getAverageWeatherData("temperatureMeanDaily")));
            secondCityTemperature.setText("City 2 Temperature: " + df.format(secondCityDTO.getAverageWeatherData("temperatureMeanDaily")));

            firstCityPrecipitation.setText("City 1 Precipitation: " + df.format(firstCityDTO.getAverageWeatherData("percipitationDaily")));
            secondCityPrecipitation.setText("City 2 Precipitation: " + df.format(secondCityDTO.getAverageWeatherData("percipitationDaily")));

            firstCityHumidity.setText("City 1 Humidity: " + df.format(firstCityDTO.getAverageWeatherData("humidityHourly")));
            secondCityHumidity.setText("City 2 Humidity: " + df.format(secondCityDTO.getAverageWeatherData("humidityHourly")));

            firstCityWindSpeed.setText("City 1 Wind Speed: " + df.format(firstCityDTO.getAverageWeatherData("windSpeedDaily")));
            secondCityWindSpeed.setText("City 2 Wind Speed: " + df.format(secondCityDTO.getAverageWeatherData("windSpeedDaily")));
        });
    }

    private void openVisualization() {
        if (firstCityWeatherData == null || secondCityWeatherData == null) {
            showError("Fetch weather data for both cities first.");
            return;
        }

        GraphSelectionWindow graphSelectionWindow = new GraphSelectionWindow(selectedGraph -> {
            SwingUtilities.invokeLater(() -> {
                switch (selectedGraph) {
                    case "line":
                        LineGraphWeatherComparison lineGraph = new LineGraphWeatherComparison("Two Cities Weather Comparison");
                        firstCityWeatherData.getTemperatureHistory().forEach((date, value) ->
                                lineGraph.addData("City 1 Temperature", date, value)
                        );
                        secondCityWeatherData.getTemperatureHistory().forEach((date, value) ->
                                lineGraph.addData("City 2 Temperature", date, value)
                        );
                        lineGraph.display();
                        break;

                    case "bar":
                        BarGraphWeatherComparison barGraph = new BarGraphWeatherComparison("Two Cities Weather Comparison");
                        barGraph.addData("City 1", "Temperature", firstCityWeatherData.getAverageWeatherData("temperatureMeanDaily"));
                        barGraph.addData("City 2", "Temperature", secondCityWeatherData.getAverageWeatherData("temperatureMeanDaily"));

                        barGraph.addData("City 1", "Precipitation", firstCityWeatherData.getAverageWeatherData("percipitationDaily"));
                        barGraph.addData("City 2", "Precipitation", secondCityWeatherData.getAverageWeatherData("percipitationDaily"));

                        barGraph.addData("City 1", "Humidity", firstCityWeatherData.getAverageWeatherData("humidityHourly"));
                        barGraph.addData("City 2", "Humidity", secondCityWeatherData.getAverageWeatherData("humidityHourly"));

                        barGraph.addData("City 1", "Wind Speed", firstCityWeatherData.getAverageWeatherData("windSpeedDaily"));
                        barGraph.addData("City 2", "Wind Speed", secondCityWeatherData.getAverageWeatherData("windSpeedDaily"));

                        barGraph.display();
                        break;

                    default:
                        showError("Invalid graph type selected.");
                }
            });
        });

        graphSelectionWindow.setVisible(true);
    }

    private void refreshFields() {
        SwingUtilities.invokeLater(() -> {
            firstCityDropDown.resetSelection();
            secondCityDropDown.resetSelection();

            firstCityTemperature.setText("City 1 Temperature: N/A");
            secondCityTemperature.setText("City 2 Temperature: N/A");

            firstCityPrecipitation.setText("City 1 Precipitation: N/A");
            secondCityPrecipitation.setText("City 2 Precipitation: N/A");

            firstCityHumidity.setText("City 1 Humidity: N/A");
            secondCityHumidity.setText("City 2 Humidity: N/A");

            firstCityWindSpeed.setText("City 1 Wind Speed: N/A");
            secondCityWindSpeed.setText("City 2 Wind Speed: N/A");

            startDateField.setText(LocalDate.now().minusDays(7).toString());
            endDateField.setText(LocalDate.now().toString());
        });
    }

    private void showError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String successMessage) {
        JOptionPane.showMessageDialog(this, successMessage, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
