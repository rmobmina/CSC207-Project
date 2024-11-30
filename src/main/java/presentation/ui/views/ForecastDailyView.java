package presentation.ui.views;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.windows.LocationsWindow;
import utils.Constants;

import javax.swing.*;
import java.time.LocalDate;

/**
 * Class that handles UI to displays the daily forecast (up to 16 days) showing max and min
 * temperature, percipitation sum, and wind speed (with dominant wind direction).
 */
public class ForecastDailyView extends LocationsWindow {

    // This is the name of the option that generates this window
    public static final String OPTION_NAME = "Forecast Daily";

    private final JLabel numberOfDaysLabel = new JLabel();
    private final JTextField numberOfDaysField = new JTextField(5);

    private final JLabel temperatureMaxLabel = new JLabel("Maximum Temperature (Today)");
    private final JLabel temperatureMaxValue = new JLabel();
    private final JLabel temperatureMinLabel = new JLabel("Minimum Temperature (Today)");
    private final JLabel temperatureMinValue = new JLabel();

    private GetForecastWeatherDataUseCase forecastWeatherDataUseCase;

    public ForecastDailyView(String name, int[] dimensions, GetLocationDataUseCase locationDataUseCase, String apiKey,
                             ApiService apiService) {
        super(name, dimensions, locationDataUseCase, apiKey, apiService);
        forecastWeatherDataUseCase = new GetForecastWeatherDataUseCase(apiService);
        numberOfDaysLabel.setText("Number of Days to Forecast: ");
        panel.add(numberOfDaysLabel);
        panel.add(numberOfDaysField);
        panel.add(temperatureMaxLabel);
        panel.add(temperatureMaxValue);
        panel.add(temperatureMinLabel);
        panel.add(temperatureMinValue);
    }

    protected void getWeatherData() {
        int numberOfDays = Integer.parseInt(numberOfDaysField.getText());
        WeatherData weatherData = forecastWeatherDataUseCase.execute(location, numberOfDays);
        // The date today
        LocalDate currentDate = LocalDate.now();
        // The date {numberOfDays} days later
        LocalDate lastDate = currentDate.plusDays(numberOfDays);
        generateWeatherDataDTO(weatherData, currentDate, lastDate);
    }

    protected void displayWeatherData(){
        temperatureMaxLabel.setText(weatherDataDTO.temperatureToString("temperatureMaxDaily", 0, Constants.CELCIUS_UNIT_TYPE));
    }
}
