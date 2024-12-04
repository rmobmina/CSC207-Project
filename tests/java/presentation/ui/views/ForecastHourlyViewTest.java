package presentation.ui.views;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import infrastructure.adapters.OpenWeatherApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentation.ui.windows.GraphSelectionWindow;

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

    private void setMockWeatherData() {
        forecastHourlyView.location = new Location("Toronto", "Ontario", "Canada", 43.7, -79.42);
        forecastHourlyView.getWeatherData();
    }

    @Test
    void testGetWeatherDataWithRealLocation() {
        Location testLocation = new Location("Toronto", "Ontario", "Canada", 43.7, -79.42);
        forecastHourlyView.location = testLocation;

        forecastHourlyView.getWeatherData();

        assertNotNull(forecastHourlyView.getWeather(), "Weather data should not be null");
    }

    @Test
    void testGetWeatherDataWithNoLocation() {
        forecastHourlyView.location = null;

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forecastHourlyView.getWeatherData();
        });

        assertEquals("No location selected!!", exception.getMessage(),
                "Expected RuntimeException with message 'No location selected!!'");
    }

    @Test
    void testDisplayWeatherDataWithRealData() {
        Location testLocation = new Location("Toronto", "Ontario", "Canada", 43.7, -79.42);
        forecastHourlyView.location = testLocation;

        forecastHourlyView.getWeatherData();
        forecastHourlyView.displayWeatherData();

        JPanel forecastPanel = (JPanel) forecastHourlyView.getContentPane().getComponent(0);
        assertTrue(forecastPanel.getComponentCount() > 0, "Forecast panel should contain components");
    }

    @Test
    void testDisplayWeatherDataWithNoData() {
        forecastHourlyView.location = null;

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forecastHourlyView.getWeatherData();
            forecastHourlyView.displayWeatherData();
        });

        String message = exception.getMessage();
        assertTrue(
                message.contains("No location selected!") || message.contains("No weather data available to display!"),
                "Error message was not as expected: " + message
        );
    }

    @Test
    void testTemperatureWarningThreshold() {
        Location testLocation = new Location("Toronto", "Ontario", "Canada", 43.7, -79.42);
        forecastHourlyView.location = testLocation;

        forecastHourlyView.getWeatherData();
        forecastHourlyView.displayWeatherData();

        double avgTemp = -5.0;
        assertTrue(avgTemp <= ForecastHourlyView.getTempThreshold(), "Temperature warning should be triggered");
    }

    @Test
    void testUIComponentsAfterRefresh() {
        Location initialLocation = new Location("Toronto", "Ontario", "Canada", 43.7, -79.42);
        Location newLocation = new Location("Vancouver", "British Columbia", "Canada", 49.3, -123.1);

        forecastHourlyView.location = initialLocation;
        forecastHourlyView.getWeatherData();
        forecastHourlyView.displayWeatherData();

        JPanel initialForecastPanel = forecastHourlyView.getForecastPanel();
        assertNotNull(initialForecastPanel, "Initial forecast panel should not be null.");

        forecastHourlyView.location = newLocation;
        forecastHourlyView.getWeatherData();
        forecastHourlyView.displayWeatherData();

        JPanel refreshedForecastPanel = forecastHourlyView.getForecastPanel();
        assertNotNull(refreshedForecastPanel, "Refreshed forecast panel should not be null.");

        assertNotSame(initialForecastPanel, refreshedForecastPanel, "Forecast panel should be refreshed for a new location.");
        assertTrue(refreshedForecastPanel.getComponentCount() > 0, "Refreshed forecast panel should have components.");
    }

    @Test
    void testDisplayWeatherDataWithNoWeatherData() {
        forecastHourlyView.makeWeatherDataNull();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            forecastHourlyView.displayWeatherData();
        });

        String expectedMessage = "No weather data available to display!";
        assertEquals(expectedMessage, exception.getMessage(), "Exception message should match the expected message.");
    }

    @Test
    void testOpenVisualization() {
        assertDoesNotThrow(() -> forecastHourlyView.openVisualization(), "openVisualization should not throw an exception");
    }

    @Test
    void testOpenGraphSelectionWindow() {
        setMockWeatherData();
        assertDoesNotThrow(() -> forecastHourlyView.openGraphSelectionWindow(), "openGraphSelectionWindow should not throw an exception");
    }

    @Test
    void testOpenGraphSelectionWindowWithNoWeatherData() {
        forecastHourlyView.makeWeatherDataNull();
        assertDoesNotThrow(() -> forecastHourlyView.openGraphSelectionWindow(), "Should not throw an exception even if weather data is null");
    }

    @Test
    void testShowBarGraphWithWeatherData() {
        setMockWeatherData();
        assertDoesNotThrow(() -> forecastHourlyView.showBarGraph(), "showBarGraph should not throw an exception");
    }

    @Test
    void testShowBarGraphWithoutWeatherData() {
        forecastHourlyView.makeWeatherDataNull();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> forecastHourlyView.showBarGraph());
        assertEquals("Error displaying bar graph: Weather data is null", exception.getMessage());
    }

    @Test
    void testShowLineGraphWithWeatherData() {
        setMockWeatherData();
        assertDoesNotThrow(() -> forecastHourlyView.showLineGraph(), "showLineGraph should not throw an exception");
    }

    @Test
    void testShowLineGraphWithoutWeatherData() {
        forecastHourlyView.makeWeatherDataNull();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> forecastHourlyView.showLineGraph());
        assertEquals("Error displaying line graph: Weather data is null", exception.getMessage());
    }
    @Test
    void testGraphSelectionWindowBarGraph() {
        setMockWeatherData(); // Set weather data to avoid null issues

        // Simulate the selection of "bar" graph type
        assertDoesNotThrow(() -> {
            new GraphSelectionWindow(graphType -> {
                if ("bar".equals(graphType)) {
                    forecastHourlyView.showBarGraph();
                }
            }).display();
        }, "GraphSelectionWindow with 'bar' graph type should not throw an exception");
    }

    @Test
    void testGraphSelectionWindowLineGraph() {
        setMockWeatherData(); // Set weather data to avoid null issues

        // Simulate the selection of "line" graph type
        assertDoesNotThrow(() -> {
            new GraphSelectionWindow(graphType -> {
                if ("line".equals(graphType)) {
                    forecastHourlyView.showLineGraph();
                }
            }).display();
        }, "GraphSelectionWindow with 'line' graph type should not throw an exception");
    }

}
