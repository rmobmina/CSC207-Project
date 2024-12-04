package presentation.ui.views;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import infrastructure.adapters.OpenWeatherApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForecastDailyViewTest {

    private ForecastDailyView forecastDailyView;

    @BeforeEach
    public void setUp() {
        OpenWeatherApiService apiService = new OpenWeatherApiService();
        Location location = new Location("TestCity", 43.4, -79.2);
        GetForecastWeatherDataUseCase forecastWeatherDataUseCase = new GetForecastWeatherDataUseCase(apiService);
        GetLocationDataUseCase locationDataUseCase = new GetLocationDataUseCase(apiService);
        int[] viewDimensions = {500, 500};

        forecastDailyView = new ForecastDailyView("Test Forecast Daily View", viewDimensions,
                location, forecastWeatherDataUseCase, locationDataUseCase, "testApiKey", apiService);
    }

    @Test
    void testNumberOfDaysInvalid() {
        // Tests for empty input
        forecastDailyView.setNumberOfDaysText("");
        boolean isNumDaysInvalid = forecastDailyView.getNumberOfDays();
        assertTrue(isNumDaysInvalid, "Empty input entered for number of days.");

        // Tests for below the minimum (0)
        forecastDailyView.setNumberOfDaysText("-1");
        isNumDaysInvalid = forecastDailyView.getNumberOfDays();
        assertTrue(isNumDaysInvalid, "Number of days should not be negative.");

        // Tests for above the maximum (16)
        forecastDailyView.setNumberOfDaysText("20");
        isNumDaysInvalid = forecastDailyView.getNumberOfDays();
        assertTrue(isNumDaysInvalid,
                "Number of days should not exceed the maximum allowed forecast days.");
    }

    @Test
    void testNumberOfDaysNonNumeric() {
        forecastDailyView.setNumberOfDaysText("notANumber");
        String numberOfDaysText = forecastDailyView.getNumDaysText();
        assertThrows(NumberFormatException.class, () -> {
            Integer.parseInt(numberOfDaysText);
        });
        boolean isNumDaysInvalid = forecastDailyView.getNumberOfDays();
        assertTrue(isNumDaysInvalid, "Non-numeric input should throw a NumberFormatException.");
    }

    @Test
    void testNumberOfDaysValid() {
        forecastDailyView.setNumberOfDaysText("10");
        boolean isNumDaysInvalid = forecastDailyView.getNumberOfDays();
        assertFalse(isNumDaysInvalid,
                "Valid number of days should be within the allowed range.");
    }

    @Test
    void testGetWeatherDataNull() {
        forecastDailyView.location = null;
        forecastDailyView.getWeatherData();
        assertTrue(forecastDailyView.isWeatherDataUseCaseFailed());
    }

    @Test
    void testGetWeatherDataValid() {
        forecastDailyView.setNumberOfDaysText("3");
        forecastDailyView.location = new Location("TestCity", 43.4, -79.2);
        forecastDailyView.getWeatherData();
        boolean isNumDaysInvalid = forecastDailyView.getNumberOfDays();
        assertFalse(isNumDaysInvalid);
        assertFalse(forecastDailyView.isWeatherDataUseCaseFailed());
    }

    @Test
    void testShowDayPanelNull() {
        forecastDailyView.setDayPanel(null);
        forecastDailyView.getWeatherData();
        assertFalse(forecastDailyView.isWeatherDataUseCaseFailed());
        forecastDailyView.displayWeatherData();
        assertTrue(forecastDailyView.didErrorOccur());
    }
}
