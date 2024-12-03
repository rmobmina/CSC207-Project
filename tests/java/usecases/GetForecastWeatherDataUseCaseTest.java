import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.json.JSONObject;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import application.usecases.GetForecastWeatherDataUseCase;

public class GetForecastWeatherDataUseCaseTest {

    @Test
    public void testFetchWeatherDataSuccess() {
        ApiService mockApiService = mock(ApiService.class);
        GetForecastWeatherDataUseCase useCase = new GetForecastWeatherDataUseCase(mockApiService);

        Location location = new Location("CityName", "Country", "State", 0.0, 0.0);
        JSONObject mockJson = new JSONObject("{\"temperature\": 20, \"condition\": \"Sunny\"}");
        WeatherData mockWeatherData = new WeatherData(mockJson);

        when(mockApiService.fetchForecastWeather(location, 5)).thenReturn(mockWeatherData);

        WeatherData result = useCase.execute(location, 5);

        assertNotNull(result);
        verify(mockApiService).fetchForecastWeather(location, 5);
    }

    @Test
    public void testFetchWeatherDataFailure() {
        ApiService mockApiService = mock(ApiService.class);
        GetForecastWeatherDataUseCase useCase = new GetForecastWeatherDataUseCase(mockApiService);

        Location location = new Location("CityName", "Country", "State", 0.0, 0.0);
        when(mockApiService.fetchForecastWeather(location, 5)).thenReturn(null);

        WeatherData result = useCase.execute(location, 5);

        assertNull(result);
        assertTrue(useCase.isUseCaseFailed());
    }
}
