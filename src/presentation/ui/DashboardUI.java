//package presentation.ui;
//
//import application.dto.WeatherDataDTO;
//import application.usecases.GetLocationDataUseCase;
//import application.usecases.GetWeatherDataUseCase;
//import domain.entities.Location;
//import domain.entities.WeatherData;
//import infrastructure.adapters.OpenWeatherApiService;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//import javax.swing.SwingConstants;
//import javax.swing.SwingUtilities;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.text.DecimalFormat;
//import java.time.LocalDate;
//import java.util.*;
//
//public class DashboardUI extends JFrame {
//    // implemented variables, might not end up using all depending
//    static final JLabel locationLabel = new JLabel("Location:");
//    static final JTextField locationField = new JTextField(20);
//    static final JLabel apiKeyLabel = new JLabel("API Key:");
//    static final JTextField apiKeyField = new JTextField(30);
//    static final JLabel temperatureLabel = new JLabel("Temperature:");
//    static final JLabel temperatureValue = new JLabel("N/A"); // Placeholder for temperature
//    static final String temperatureType = "cel";
//    static final JLabel conditionLabel = new JLabel("Condition:");
//    static final JLabel conditionValue = new JLabel("N/A"); // Placeholder for weather condition
//    static final JLabel humidityLabel = new JLabel("Humidity:");
//    static final JLabel humidityValue = new JLabel("N/A"); // Placeholder for humidity
//    static final JLabel windLabel = new JLabel("Wind:");
//    static final JLabel windValue = new JLabel("N/A"); // Placeholder for wind speed
//    static final JButton getInfoButton = new JButton("Get Info");
//    static final JButton refreshButton = new JButton("Refresh");
//    static final JButton rangeOfTimeButton = new JButton("Range of Time");
//    static final JButton simulationButton = new JButton("Simulation");
//    static WeatherDataDTO weatherDataDTO = new WeatherDataDTO();
//    static final DropDownUI menu = new DropDownUI();
//
//
//
//    // A: main dashboard, its messy for now but we'll split them up for clean architecture and for a cleaner look
//    public DashboardUI() {
//        setTitle("Weather Dashboard");
//        setSize(400, 500);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new GridLayout(8, 2, 10, 10)); // B: Will be changed as we add more elements
//
//        // B: main compenents of GUI, most are placeholders
//        panel.add(locationLabel);
//        panel.add(menu);
//
//        panel.add(apiKeyLabel);
//        panel.add(apiKeyField);
//
//        panel.add(temperatureLabel);
//        panel.add(temperatureValue);
//
//        panel.add(conditionLabel);
//        panel.add(conditionValue);
//
//        panel.add(humidityLabel);
//        panel.add(humidityValue);
//
//        panel.add(windLabel);
//        panel.add(windValue);
//
//        panel.add(getInfoButton);
//
//        //panel.add(new JLabel());
//        panel.add(refreshButton);
//
//        // B: buttons for range and simulation
//        panel.add(rangeOfTimeButton);
//        panel.add(simulationButton);
//
//
//        add(panel);
//        //Button that displays weather data given location and api key
//        getInfoButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                displayWeatherData();
//            }
//        });
//
//        refreshButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                refreshAll();
//            }
//        });
//
//        // Buttons that make the menus for range of time and the simulation
//        rangeOfTimeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                openRangeOfTimeWindow();
//            }
//        });
//
//        simulationButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                openSimulationWindow();
//            }
//        });
//
//
//    }
//
//    private void refreshAll() {
//        apiKeyField.setText("");
//        menu.getLocationField().setText("");
//        temperatureValue.setText("N/A");
//        conditionValue.setText("N/A");
//        humidityValue.setText("N/A");
//        windValue.setText("N/A");
//    }
//
//    private void displayWeatherData() {
//        OpenWeatherApiService apiService = new OpenWeatherApiService();
//        GetLocationDataUseCase locationUseCase = new GetLocationDataUseCase(apiService);
//        GetWeatherDataUseCase weatherUseCase = new GetWeatherDataUseCase(apiService);
//        String apiKey = getAPIKeyFieldValue();
//        String city = getLocationFieldValue();
//        System.out.println("city: " + city);
//        Location chosenLocation = locationUseCase.execute(city, apiKey);
//        LocalDate date = LocalDate.of(2020, 1, 12); //To be changed later...
//        if (chosenLocation != null) {
//            WeatherData weatherData = weatherUseCase.execute(chosenLocation, apiKey);
//            extractWeatherData(weatherData, chosenLocation, date);
//            updateTextFields(weatherDataDTO);
//        }
//        else {
//            openInvalidLocationWindow();
//        }
//    }
//
//    private String getAPIKeyFieldValue(){
//        return apiKeyField.getText();
//    }
//
//    private String getLocationFieldValue() {
//        return menu.getLocationField().getText();
//    }
//
//    private WeatherDataDTO createWeatherDataDTO(JSONObject data, Location location, LocalDate date){
//        try {
//            JSONObject mainData = data.getJSONObject("main");
//            return new WeatherDataDTO(
//                    location.getCity(),
//                    date,
//                    mainData.getDouble("temp"),
//                    mainData.getInt("humidity"),
//                    data.getJSONObject("wind").getDouble("speed"),
//                    0.0, new ArrayList<String>(){} ); //NOTE: may not always have percipitation data and alerts
//        }
//        catch (JSONException exception) {
//            exception.printStackTrace();
//            return null;
//        }
//    }
//
//    private void extractWeatherData(WeatherData weatherData, Location location, LocalDate date) {
//        if (weatherData != null) {
//            this.weatherDataDTO = createWeatherDataDTO(weatherData.getWeatherDetails(), location, date);
//        }
//        else {
//            System.out.println("Error: Could not retrieve weather data.");
//        }
//    }
//
//    private void updateTextFields(WeatherDataDTO weatherDataDTO) {
//        DecimalFormat df = new DecimalFormat("#.##");
//        temperatureValue.setText(df.format(weatherDataDTO.getTemperature(temperatureType)) + " °C");
//        humidityValue.setText(weatherDataDTO.humidity + " %");
//        windValue.setText(weatherDataDTO.windSpeed + " m/s");
//    }
//
//    private void openInvalidLocationWindow() {
//        JFrame rangeWindow = new JFrame("Invalid Location");
//        rangeWindow.setSize(400, 200);
//        rangeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        JLabel placeholderLabel = new JLabel("The location you entered is invalid. Please try again!", SwingConstants.CENTER);
//        rangeWindow.add(placeholderLabel);
//        rangeWindow.setVisible(true);
//    }
//
//    // Placeholder window for "Range of Time" feature
//    private void openRangeOfTimeWindow() {
//        JFrame rangeWindow = new JFrame("Range of Time");
//        rangeWindow.setSize(300, 200);
//        rangeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        JLabel placeholderLabel = new JLabel("feature not added yet.", SwingConstants.CENTER);
//        rangeWindow.add(placeholderLabel);
//        rangeWindow.setVisible(true);
//    }
//
//    // Placeholder window for simulation
//    private void openSimulationWindow() {
//        JFrame simulationWindow = new JFrame("Simulation");
//        simulationWindow.setSize(300, 200);
//        simulationWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        JLabel placeholderLabel = new JLabel("feature not added yet.", SwingConstants.CENTER);
//        simulationWindow.add(placeholderLabel);
//        simulationWindow.setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        DashboardUI frame = new DashboardUI();
//
//        SwingUtilities.invokeLater(() -> {
//            frame.setVisible(true);
//        });
//    }
//}
