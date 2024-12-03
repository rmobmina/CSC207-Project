package usecases;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.json.JSONObject;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import application.usecases.GetHistoricalWeatherDataUseCase;

import java.time.LocalDate;

public class GetHistoricalWeatherDataUseCaseTest {

    @Test
    public void testFetchHistoricalWeatherSuccess() {
        ApiService mockApiService = mock(ApiService.class);
        GetHistoricalWeatherDataUseCase useCase = new GetHistoricalWeatherDataUseCase(mockApiService);

        // Use a valid Location constructor
        Location location = new Location("CityName", "Country", "State", 0.0, 0.0);

        // Create a mock JSON object for WeatherData
        JSONObject mockJson = new JSONObject("{\"temperature\": 20, \"condition\": \"Sunny\"}");
        WeatherData mockWeatherData = new WeatherData(mockJson);

        // Define the date range
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 7);

        // Mock the API service
        when(mockApiService.fetchHistoricalWeather(location, startDate, endDate)).thenReturn(mockWeatherData);

        // Execute the use case
        WeatherData result = useCase.execute(location, startDate, endDate);

        // Assertions
        assertNotNull(result);
        verify(mockApiService).fetchHistoricalWeather(location, startDate, endDate);
    }

    @Test
    public void testFetchHistoricalWeatherFailure() {
        ApiService mockApiService = mock(ApiService.class);
        GetHistoricalWeatherDataUseCase useCase = new GetHistoricalWeatherDataUseCase(mockApiService);

        // Use a valid Location constructor
        Location location = new Location("CityName", "Country", "State", 0.0, 0.0);

        // Define the date range
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 7);

        // Mock the API service to return null
        when(mockApiService.fetchHistoricalWeather(location, startDate, endDate)).thenReturn(null);

        // Execute the use case
        WeatherData result = useCase.execute(location, startDate, endDate);

        // Assertions
        assertNull(result);
        assertTrue(useCase.isUseCaseFailed()); // Assuming isUseCaseFailed() is a public getter
    }
}
