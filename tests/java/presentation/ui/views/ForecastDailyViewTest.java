package presentation.ui.views;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import infrastructure.adapters.OpenWeatherApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presentation.ui.views.ForecastDailyView;
import utils.Constants;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class ForecastDailyViewTest {

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
    public void testNumberOfDaysInvalidNegative() {
        forecastDailyView.setNumberOfDaysField("-1");
        String numberOfDaysText = forecastDailyView.numberOfDaysField.getText();
        int numberOfDays = parseNumberOfDays(numberOfDaysText);
        assertTrue(numberOfDays < 0, "Number of days should not be negative.");
    }

    @Test
    public void testNumberOfDaysExceedsMax() {
        forecastDailyView.setNumberOfDaysField("20");
        String numberOfDaysText = forecastDailyView.numberOfDaysField.getText();
        int numberOfDays = parseNumberOfDays(numberOfDaysText);
        assertTrue(numberOfDays > Constants.MAX_FORECAST_DAYS,
                "Number of days should not exceed the maximum allowed forecast days.");
    }

    @Test
    public void testNumberOfDaysNonNumeric() {
        forecastDailyView.setNumberOfDaysField("notANumber");
        String numberOfDaysText = forecastDailyView.numberOfDaysField.getText();
        assertThrows(NumberFormatException.class, () -> {
            Integer.parseInt(numberOfDaysText);
        }, "Non-numeric input should throw a NumberFormatException.");
    }

    @Test
    public void testNumberOfDaysValid() {
        forecastDailyView.setNumberOfDaysField("10");
        String numberOfDaysText = forecastDailyView.numberOfDaysField.getText();
        int numberOfDays = parseNumberOfDays(numberOfDaysText);
        assertTrue(numberOfDays > 0 && numberOfDays <= Constants.MAX_FORECAST_DAYS,
                "Valid number of days should be within the allowed range.");
    }

    private int parseNumberOfDays(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1; // Return invalid value if parsing fails
        }
    }
}
