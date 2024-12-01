package infrastructure.adapters;

import domain.entities.Location;
import domain.entities.WeatherData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import utils.HttpUtils;

class OpenWeatherApiServiceTest {

    private OpenWeatherApiService apiService;
    private String validApiKey;

    @BeforeEach
    void setUp() {
        apiService = new OpenWeatherApiService();
        validApiKey = "2d6d6124e844c3e976842b19833dfa3b";
    }

    // ---- API Key Validity Tests ----
    @Test
    void isApiKeyValid_validApiKey_returnsTrue() {
        boolean isValid = apiService.isApiKeyValid(validApiKey);
        assertTrue(isValid);
    }

    @Test
    void isApiKeyValid_invalidApiKey_returnsFalse() {
        String invalidApiKey = "InvalidApiKey";
        boolean isValid = apiService.isApiKeyValid(invalidApiKey);
        assertFalse(isValid);
    }

    @Test
    void isApiKeyValid_emptyApiKey_returnsFalse() {
        boolean isValid = apiService.isApiKeyValid("");
        assertFalse(isValid);
    }

    @Test
    void isApiKeyValid_throwsIOException_returnsFalse() {
        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString())).thenThrow(new IOException("Simulated IOException"));

            boolean isValid = apiService.isApiKeyValid(validApiKey);
            assertFalse(isValid);
            mockedStatic.verify(() -> HttpUtils.makeApiCall(anyString()), times(1));
        }
    }

    // ---- Fetch Locations Tests ----
    @Test
    void fetchLocations_validCity_returnsLocations() {
        String city = "Toronto";
        String mockApiResponse = "[{ \"name\": \"Toronto\", \"lat\": 43.7, \"lon\": -79.42, \"country\": \"CA\" }]";

        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString())).thenReturn(mockApiResponse);

            List<Location> locations = apiService.fetchLocations(city, validApiKey);
            assertNotNull(locations);
            assertEquals(1, locations.size());
            assertEquals("Toronto", locations.get(0).getCity());
        }
    }

    @Test
    void fetchLocations_invalidCity_returnsEmptyList() {
        String city = "NonExistentCity";
        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString())).thenReturn("[]");

            List<Location> locations = apiService.fetchLocations(city, validApiKey);
            assertNotNull(locations);
            assertTrue(locations.isEmpty());
        }
    }

    @Test
    void fetchLocations_nullCity_returnsEmptyList() {
        List<Location> locations = apiService.fetchLocations(null, validApiKey);
        assertNotNull(locations);
        assertTrue(locations.isEmpty());
    }

    @Test
    void fetchLocations_throwsIOException_returnsEmptyList() {
        String city = "Toronto";

        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString())).thenThrow(new IOException("Simulated IOException"));

            List<Location> locations = apiService.fetchLocations(city, validApiKey);
            assertNotNull(locations);
            assertTrue(locations.isEmpty());
            mockedStatic.verify(() -> HttpUtils.makeApiCall(anyString()), times(1));
        }
    }

    // ---- Fetch Weather Tests ----
    @Test
    void fetchWeather_validUrl_returnsWeatherData() {
        String mockApiResponse = "{ \"temperature_2m\": [20.5, 21.1], \"humidity\": [60, 62] }";

        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString())).thenReturn(mockApiResponse);

            WeatherData weatherData = apiService.fetchWeather("http://mock-api.com");
            assertNotNull(weatherData);
        }
    }

    @Test
    void fetchWeather_invalidResponse_returnsNull() {
        String mockApiResponse = "";

        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString())).thenReturn(mockApiResponse);

            WeatherData weatherData = apiService.fetchWeather("http://mock-api.com");
            assertNull(weatherData);
        }
    }

    @Test
    void fetchWeather_throwsJSONException_returnsNull() {
        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString())).thenReturn("{ invalid json }");

            WeatherData weatherData = apiService.fetchWeather("http://mock-api.com");
            assertNull(weatherData);
        }
    }

    // ---- Fetch Historical Weather Tests ----
    @Test
    void fetchHistoricalWeather_validInputs_returnsWeatherData() {
        Location location = new Location("Toronto", "ON", "CA", 43.7, -79.42);
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 1, 2);

        WeatherData weatherData = apiService.fetchHistoricalWeather(location, startDate, endDate);
        assertNotNull(weatherData);
    }

    @Test
    void fetchHistoricalWeather_nullLocation_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            apiService.fetchHistoricalWeather(null, LocalDate.now(), LocalDate.now());
        });
    }

    // ---- Fetch Forecast Weather Tests ----
    @Test
    void fetchForecastWeather_validInputs_returnsWeatherData() {
        Location location = new Location("Toronto", "ON", "CA", 43.7, -79.42);

        WeatherData weatherData = apiService.fetchForecastWeather(location, 7);
        assertNotNull(weatherData);
    }

}
