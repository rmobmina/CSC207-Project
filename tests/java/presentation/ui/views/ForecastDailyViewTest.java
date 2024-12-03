//package presentation.ui.views;
//
//import application.usecases.GetForecastWeatherDataUseCase;
//import application.usecases.GetLocationDataUseCase;
//import domain.entities.Location;
//import infrastructure.adapters.OpenWeatherApiService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import presentation.ui.views.ForecastDailyView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class ForecastDailyViewTest {
//    ForecastDailyView forecastDailyView;
//
//    @BeforeEach
//    public void setUp() {
//        OpenWeatherApiService apiService = new OpenWeatherApiService();
//        Location location = new Location("testCity", 43.4, -79.2);
//        GetForecastWeatherDataUseCase forecastWeatherDataUseCase = new GetForecastWeatherDataUseCase(apiService);
//        GetLocationDataUseCase locationDataUseCase = new GetLocationDataUseCase(apiService); // Remove this later
//        int[] viewDimensions = {500, 500};
//        // This will be changed when all changes on dashboard and LocationsWindow have been finalized (Akram can take care of this)
//        forecastDailyView = new ForecastDailyView("Test Forecast Daily View", viewDimensions,
//                location, forecastWeatherDataUseCase, locationDataUseCase, "", apiService);
//    }
//
//    @Test
//    public void testNumberOfDaysInvalid() {
//        String numberOfDays = "-1";
//        forecastDailyView.setNumberOfDaysField(numberOfDays);
//        Assertions.assertTrue(forecastDailyView.getNumOfDaysUseCase());
//    }
//
//    @Test
//    public void testNumberOfDaysOutOfRange() {
//        String numberOfDays = "20";
//        forecastDailyView.setNumberOfDaysField(numberOfDays);
//        Assertions.assertTrue(forecastDailyView.getNumOfDaysUseCase());
//    }
//
//    @Test
//    public void testNumberOfDaysNumberException() {
//        String numberOfDays = "notANumber";
//        forecastDailyView.setNumberOfDaysField(numberOfDays);
//        boolean useCaseFailed = forecastDailyView.getNumOfDaysUseCase();
//        //Assertions.assertThrows(NumberFormatException.class, () -> { forecastDailyView.getNumOfDaysUseCase(); });
//        Assertions.assertTrue(useCaseFailed);
//    }
//
//    @Test
//    public void testNumberDaysValid() {
//        String numberOfDays = "10";
//        forecastDailyView.setNumberOfDaysField(numberOfDays);
//        // Test if use case fails
//        Assertions.assertFalse(forecastDailyView.getNumOfDaysUseCase());
//    }
//
//}
