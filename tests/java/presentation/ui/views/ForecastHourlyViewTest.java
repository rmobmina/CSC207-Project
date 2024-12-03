package presentation.ui.views;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class ForecastHourlyViewTest {

    private ForecastHourlyView forecastHourlyView;
    private GetLocationDataUseCase locationDataUseCase;
    private GetForecastWeatherDataUseCase forecastWeatherDataUseCase;
    private OpenWeatherApiService apiService;
    private WeatherData weatherData;

    @BeforeEach
    void setUp() {
        apiService = new OpenWeatherApiService();
        locationDataUseCase = new GetLocationDataUseCase(apiService);
        forecastWeatherDataUseCase = new GetForecastWeatherDataUseCase(apiService);

        // Creating instance of ForecastHourlyView
        forecastHourlyView = new ForecastHourlyView("Forecast Hourly View",new int[]{800, 600},locationDataUseCase,
                "0e85f616a96a624a0bf65bad89ff68c5", apiService);
    }

    @Test
    void testGetWeatherDataWithRealLocation() {
        // Testing the getWeatherData() method with a real location to make sure
        // it fetches the data and does not return null.
        Location testLocation = new Location("Toronto", "Ontario", "Canada", 43.7, -79.42);

        forecastHourlyView.location = testLocation;
        WeatherData data = forecastHourlyView.getWeather();

        // Assert that the fetched weather data is not null
        assertNotNull(data, "Weather data should not be null");
    }

    @Test
    void testDisplayWeatherData() {
        // Testing the DisplayWeatherData method to see if it shows all the components in the UI
        Location testLocation = new Location("Toronto", "Ontario", "Canada", 43.7, -79.42);
        forecastHourlyView.location = testLocation;

        forecastHourlyView.getWeatherData();
        forecastHourlyView.displayWeatherData();

        // Checking that the forecast panel has components
        JPanel forecastPanel = (JPanel) forecastHourlyView.getContentPane().getComponent(0);
        assertTrue(forecastPanel.getComponentCount() > 0, "Forecast panel should contain components");
    }

    @Test
    void testGetWeatherDataWithEmptyInput() {
        // Testing the GetWeatherData() method with an empty input by not setting a location in ForecastHourlyView
        forecastHourlyView.location = null;

        forecastHourlyView.getWeatherData();

        // Asserting that weather data is still null since no valid location was provided
        assertNull(forecastHourlyView.getWeather(), "Weather data should be null for empty input");
    }

}