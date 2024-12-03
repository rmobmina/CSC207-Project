//package infrastructure.adapters;
//
//import domain.entities.Location;
//import domain.entities.WeatherData;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.MockedStatic;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.List;
//
//import org.mockito.Mockito;
//import utils.HttpUtils;
//
//class OpenWeatherApiServiceTest {
//
//    private OpenWeatherApiService apiService;
//    private String validApiKey;
//
//    @BeforeEach
//    void setUp() {
//        apiService = new OpenWeatherApiService();
//        validApiKey = "2d6d6124e844c3e976842b19833dfa3b";
//    }
//
//    // ---- API Key Validity Tests ----
//    @Test
//    void isApiKeyValid_validApiKey_returnsTrue() {
//        boolean isValid = apiService.isApiKeyValid(validApiKey);
//        Assertions.assertTrue(isValid);
//    }
//
//    @Test
//    void isApiKeyValid_invalidApiKey_returnsFalse() {
//        String invalidApiKey = "InvalidApiKey";
//        boolean isValid = apiService.isApiKeyValid(invalidApiKey);
//        Assertions.assertFalse(isValid);
//    }
//
//    @Test
//    void isApiKeyValid_emptyApiKey_returnsFalse() {
//        boolean isValid = apiService.isApiKeyValid("");
//        Assertions.assertFalse(isValid);
//    }
//
//    @Test
//    void isApiKeyValid_throwsIOException_returnsFalse() {
//        try (MockedStatic<HttpUtils> mockedStatic = Mockito.mockStatic(HttpUtils.class)) {
//            mockedStatic.when(() -> HttpUtils.makeApiCall(ArgumentMatchers.anyString())).thenThrow(new IOException("Simulated IOException"));
//
//            boolean isValid = apiService.isApiKeyValid(validApiKey);
//            Assertions.assertFalse(isValid);
//            mockedStatic.verify(() -> HttpUtils.makeApiCall(ArgumentMatchers.anyString()), Mockito.times(1));
//        }
//    }
//
//    // ---- Fetch Locations Tests ----
//    @Test
//    void fetchLocations_validCity_returnsLocations() {
//        String city = "Toronto";
//        String mockApiResponse = "[{ \"name\": \"Toronto\", \"lat\": 43.7, \"lon\": -79.42, \"country\": \"CA\" }]";
//
//        try (MockedStatic<HttpUtils> mockedStatic = Mockito.mockStatic(HttpUtils.class)) {
//            mockedStatic.when(() -> HttpUtils.makeApiCall(ArgumentMatchers.anyString())).thenReturn(mockApiResponse);
//
//            List<Location> locations = apiService.fetchLocations(city, validApiKey);
//            Assertions.assertNotNull(locations);
//            Assertions.assertEquals(1, locations.size());
//            Assertions.assertEquals("Toronto", locations.get(0).getCity());
//        }
//    }
//
//    @Test
//    void fetchLocations_validCity_invalidKey_returnsEmptyArrayList() {
//        String city = "Toronto";
//        String mockApiResponse = "[{ \"name\": \"Toronto\", \"lat\": 43.7, \"lon\": -79.42, \"country\": \"CA\" }]";
//
//        try (MockedStatic<HttpUtils> mockedStatic = Mockito.mockStatic(HttpUtils.class)) {
//            mockedStatic.when(() -> HttpUtils.makeApiCall(ArgumentMatchers.anyString())).thenReturn(mockApiResponse);
//
//            List<Location> locations = apiService.fetchLocations(city,  null);
//            Assertions.assertNotNull(locations);
//            Assertions.assertEquals(0, locations.size());
//        }
//    }
//
//    @Test
//    void fetchLocations_invalidCity_returnsEmptyList() {
//        String city = "NonExistentCity";
//        try (MockedStatic<HttpUtils> mockedStatic = Mockito.mockStatic(HttpUtils.class)) {
//            mockedStatic.when(() -> HttpUtils.makeApiCall(ArgumentMatchers.anyString())).thenReturn("[]");
//
//            List<Location> locations = apiService.fetchLocations(city, validApiKey);
//            Assertions.assertNotNull(locations);
//            Assertions.assertTrue(locations.isEmpty());
//        }
//    }
//
//    @Test
//    void fetchLocations_emptyCity_returnsEmptyList() {
//        List<Location> locations = apiService.fetchLocations("", validApiKey);
//        Assertions.assertNotNull(locations);
//        Assertions.assertTrue(locations.isEmpty());
//    }
//
//    @Test
//    void fetchLocations_specialCharacterCity_returnsEmptyList() {
//        String city = "!!!";
//        List<Location> locations = apiService.fetchLocations(city, validApiKey);
//        Assertions.assertNotNull(locations);
//        Assertions.assertTrue(locations.isEmpty());
//    }
//
//    @Test
//    void fetchLocations_throwsIOException_returnsEmptyList() {
//        String city = "Toronto";
//
//        try (MockedStatic<HttpUtils> mockedStatic = Mockito.mockStatic(HttpUtils.class)) {
//            mockedStatic.when(() -> HttpUtils.makeApiCall(ArgumentMatchers.anyString())).thenThrow(new IOException("Simulated IOException"));
//
//            List<Location> locations = apiService.fetchLocations(city, validApiKey);
//            Assertions.assertNotNull(locations);
//            Assertions.assertTrue(locations.isEmpty());
//            mockedStatic.verify(() -> HttpUtils.makeApiCall(ArgumentMatchers.anyString()), Mockito.times(1));
//        }
//    }
//
//    @Test
//    void fetchLocations_throwsJSONException_returnsEmptyList() {
//        String city = "Toronto";
//
//        try (MockedStatic<HttpUtils> mockedStatic = Mockito.mockStatic(HttpUtils.class)) {
//            mockedStatic.when(() -> HttpUtils.makeApiCall(ArgumentMatchers.anyString())).thenReturn("{ invalid json }");
//
//            List<Location> locations = apiService.fetchLocations(city, validApiKey);
//            Assertions.assertNotNull(locations);
//            Assertions.assertTrue(locations.isEmpty());
//        }
//    }
//
//    // ---- Fetch Weather Tests ----
//    @Test
//    void fetchWeather_validUrl_returnsWeatherData() {
//        String mockApiResponse = "{ \"temperature_2m\": [20.5, 21.1], \"humidity\": [60, 62] }";
//
//        try (MockedStatic<HttpUtils> mockedStatic = Mockito.mockStatic(HttpUtils.class)) {
//            mockedStatic.when(() -> HttpUtils.makeApiCall(ArgumentMatchers.anyString())).thenReturn(mockApiResponse);
//
//            WeatherData weatherData = apiService.fetchWeather("http://mock-api.com");
//            Assertions.assertNotNull(weatherData);
//        }
//    }
//
//    @Test
//    void fetchWeather_invalidResponse_returnsNull() {
//        String mockApiResponse = "";
//
//        try (MockedStatic<HttpUtils> mockedStatic = Mockito.mockStatic(HttpUtils.class)) {
//            mockedStatic.when(() -> HttpUtils.makeApiCall(ArgumentMatchers.anyString())).thenReturn(mockApiResponse);
//
//            WeatherData weatherData = apiService.fetchWeather("http://mock-api.com");
//            Assertions.assertNull(weatherData);
//        }
//    }
//
//    @Test
//    void fetchWeather_throwsJSONException_returnsNull() {
//        try (MockedStatic<HttpUtils> mockedStatic = Mockito.mockStatic(HttpUtils.class)) {
//            mockedStatic.when(() -> HttpUtils.makeApiCall(ArgumentMatchers.anyString())).thenReturn("{ invalid json }");
//
//            WeatherData weatherData = apiService.fetchWeather("http://mock-api.com");
//            Assertions.assertNull(weatherData);
//        }
//    }
//
//    // ---- Fetch Historical Weather Tests ----
//    @Test
//    void fetchHistoricalWeather_validInputs_returnsWeatherData() {
//        Location location = new Location("Toronto", "ON", "CA", 43.7, -79.42);
//        LocalDate startDate = LocalDate.of(2020, 1, 1);
//        LocalDate endDate = LocalDate.of(2020, 1, 2);
//
//        WeatherData weatherData = apiService.fetchHistoricalWeather(location, startDate, endDate);
//        Assertions.assertNotNull(weatherData);
//    }
//
//    @Test
//    void fetchHistoricalWeather_nullLocation_throwsException() {
//        Assertions.assertThrows(NullPointerException.class, () -> {
//            apiService.fetchHistoricalWeather(null, LocalDate.now(), LocalDate.now());
//        });
//    }
//
//    // ---- Fetch Forecast Weather Tests ----
//    @Test
//    void fetchForecastWeather_validInputs_returnsWeatherData() {
//        Location location = new Location("Toronto", "ON", "CA", 43.7, -79.42);
//
//        WeatherData weatherData = apiService.fetchForecastWeather(location, 7);
//        Assertions.assertNotNull(weatherData);
//    }
//
//    @Test
//    void fetchForecastWeather_invalidForecastLength_returnsEmptyData() {
//        Location location = new Location("Toronto", "ON", "CA", 43.7, -79.42);
//
//        WeatherData weatherData = apiService.fetchForecastWeather(location, -1);  // Invalid forecast length
//        Assertions.assertNull(weatherData);  // Assuming it returns null on invalid length
//    }
//
//}
