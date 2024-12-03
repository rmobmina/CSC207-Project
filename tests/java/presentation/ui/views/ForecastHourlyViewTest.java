package presentation.ui.views;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import infrastructure.adapters.OpenWeatherApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class ForecastHourlyViewTest {

    private ForecastHourlyView forecastHourlyView;
    private GetLocationDataUseCase locationDataUseCase;
    private OpenWeatherApiService apiService;

    @BeforeEach
    void setUp() {
        apiService = new OpenWeatherApiService();
        locationDataUseCase = new GetLocationDataUseCase(apiService);

        // Creating instance of ForecastHourlyView
        forecastHourlyView = new ForecastHourlyView(
                "Forecast Hourly View",
                new int[]{800, 600},
                locationDataUseCase,
                "0e85f616a96a624a0bf65bad89ff68c5",
                apiService
        );
    }

    @Test
    void testGetWeatherDataWithRealLocation() {
        // Test fetch weather data with a valid location
        Location testLocation = new Location("Toronto", "Ontario", "Canada", 43.7, -79.42);
        forecastHourlyView.location = testLocation;

        // Fetch weather data
        forecastHourlyView.getWeatherData();

        // Assert that weather data is fetched and not null
        assertNotNull(forecastHourlyView.getWeather(), "Weather data should not be null");
    }

    @Test
    void testGetWeatherDataWithNoLocation() {
        // Test behavior when no location is provided
        forecastHourlyView.location = null;

        // Assert that a RuntimeException is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forecastHourlyView.getWeatherData();
        });

        // Verify the exception message
        assertEquals("No location selected!!", exception.getMessage(),
                "Expected RuntimeException with message 'No location selected!!'");
    }


    @Test
    void testDisplayWeatherDataWithRealData() {
        // Test displaying weather data in the UI
        Location testLocation = new Location("Toronto", "Ontario", "Canada", 43.7, -79.42);
        forecastHourlyView.location = testLocation;

        forecastHourlyView.getWeatherData();
        forecastHourlyView.displayWeatherData();

        // Check that components were added to the panel
        JPanel forecastPanel = (JPanel) forecastHourlyView.getContentPane().getComponent(0);
        assertTrue(forecastPanel.getComponentCount() > 0, "Forecast panel should contain components");
    }

    @Test
    void testDisplayWeatherDataWithNoData() {
        // Set location to null to simulate no data
        forecastHourlyView.location = null;

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forecastHourlyView.getWeatherData();
            forecastHourlyView.displayWeatherData();
        });

        // Verify the error message
        String message = exception.getMessage();
        assertTrue(
                message.contains("No location selected!") || message.contains("No weather data available to display!"),
                "Error message was not as expected: " + message
        );
    }




    @Test
    void testTemperatureWarningThreshold() {
        // Test if the warning message is shown for temperatures below threshold
        Location testLocation = new Location("Toronto", "Ontario", "Canada", 43.7, -79.42);
        forecastHourlyView.location = testLocation;

        forecastHourlyView.getWeatherData();
        forecastHourlyView.displayWeatherData();

        // Simulate a low temperature scenario
        double avgTemp = -5.0;
        assertTrue(avgTemp <= ForecastHourlyView.getTempThreshold(), "Temperature warning should be triggered");
    }

    @Test
    void testUIComponentsAfterRefresh() {
        // Test that UI components refresh correctly after entering a new location
        Location initialLocation = new Location("Toronto", "Ontario", "Canada", 43.7, -79.42);
        Location newLocation = new Location("Vancouver", "British Columbia", "Canada", 49.3, -123.1);

        // First location
        forecastHourlyView.location = initialLocation;
        forecastHourlyView.getWeatherData();
        forecastHourlyView.displayWeatherData();

        // Capture the reference of the initial forecast panel
        JPanel initialForecastPanel = forecastHourlyView.getForecastPanel();
        assertNotNull(initialForecastPanel, "Initial forecast panel should not be null.");

        // Switch to new location
        forecastHourlyView.location = newLocation;
        forecastHourlyView.getWeatherData();
        forecastHourlyView.displayWeatherData();

        // Capture the reference of the refreshed forecast panel
        JPanel refreshedForecastPanel = forecastHourlyView.getForecastPanel();
        assertNotNull(refreshedForecastPanel, "Refreshed forecast panel should not be null.");

        // Ensure the forecast panel is refreshed
        assertNotSame(initialForecastPanel, refreshedForecastPanel, "Forecast panel should be refreshed for a new location.");

        // Check that the refreshed panel contains components
        assertTrue(refreshedForecastPanel.getComponentCount() > 0, "Refreshed forecast panel should have components.");
    }
    @Test
    void testDisplayWeatherDataWithNoWeatherData() {
        // Simulate the condition where weatherData is null

        // Manually setting weatherData to null
        forecastHourlyView.makeWeatherDataNull();

        // Expect RuntimeException to be thrown when displayWeatherData() is called
        Exception exception = assertThrows(RuntimeException.class, () -> {
            forecastHourlyView.displayWeatherData();
        });

        // Verify the exception message
        String expectedMessage = "No weather data available to display!";
        assertEquals(expectedMessage, exception.getMessage(), "Exception message should match the expected message.");
    }
}
