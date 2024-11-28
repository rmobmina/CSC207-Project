package infrastructure.adapters;

import domain.entities.Location;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import utils.HttpUtils;

class OpenWeatherApiServiceTest {
    private OpenWeatherApiService apiService;
    private String validApiKey;

    @BeforeEach
    void setUp() {
        apiService = new OpenWeatherApiService();
        validApiKey = "2d6d6124e844c3e976842b19833dfa3b";
    }

    @Test
    void isApiKeyValid_validApiKey_returnsTrue() {

        // Act
        boolean isValid = apiService.isApiKeyValid(validApiKey);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isApiKeyValid_invalidApiKey_returnsFalse() {
        // Arrange
        String apiKey = "Th1sK3y1sN0tVal1d";

        // Act
        boolean isValid = apiService.isApiKeyValid(apiKey);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void isApiKeyValid_throwsIOException_returnsFalse() {
        // Simulate IOException being thrown
        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString()))
                    .thenThrow(new IOException("Simulated IOException"));

            // Act
            boolean isValid = apiService.isApiKeyValid(validApiKey);

            // Assert
            assertFalse(isValid);
            mockedStatic.verify(() -> HttpUtils.makeApiCall(anyString()), times(1));
        }

    }

    @Test
    void fetchLocation_validCity_validKey() {
        // Arrange
        String city = "Toronto";
        String mockApiResponse = "[{ \"name\": \"Toronto\", \"lat\": 43.7, \"lon\": -79.42, \"country\": \"CA\" }]";

        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString())).thenReturn(mockApiResponse);

            // Act
            List<Location> locations = apiService.fetchLocations(city, validApiKey);

            // Assert
            assertNotNull(locations);
            assertFalse(locations.isEmpty());
            assertEquals(1, locations.size());
            assertEquals("Toronto", locations.get(0).getCity());
        }
    }

    @Test
    void fetchLocation_invalidCity_invalidKey() {
        // Arrange
        String city = "InvalidCity";


        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString())).thenReturn("[]");

            // Act
            List<Location> locations = apiService.fetchLocations(city, "Th1sK3y1sN0tVal1d");

            // Assert
            assertNotNull(locations);
            assertTrue(locations.isEmpty()); // No locations returned for invalid city and key
        }
    }

    @Test
    void fetchLocation_invalidCity_validKey() {
        // Arrange
        String city = "NonExistentCity";


        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString())).thenReturn("[]");

            // Act
            List<Location> locations = apiService.fetchLocations(city, validApiKey);

            // Assert
            assertNotNull(locations);
            assertTrue(locations.isEmpty()); // No locations should be returned for a non-existent city
        }
    }

    @Test
    void fetchLocation_validCity_invalidKey() {
        // Arrange
        String city = "Toronto";


        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString())).thenReturn(null);

            // Act
            List<Location> locations = apiService.fetchLocations(city, "Th1sK3y1sN0tVal1d");

            // Assert
            assertNotNull(locations);
            assertTrue(locations.isEmpty()); // No locations should be returned for an invalid API key
        }
    }

    @Test
    void fetchLocation_multipleValidCities_validKey() {
        // Arrange
        String city = "Springfield";
        String mockApiResponse = "["
                + "{ \"name\": \"Springfield\", \"lat\": 39.8, \"lon\": -89.65, \"country\": \"US\" },"
                + "{ \"name\": \"Springfield\", \"lat\": 44.0, \"lon\": -123.02, \"country\": \"US\" }"
                + "]";


        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString())).thenReturn(mockApiResponse);

            // Act
            List<Location> locations = apiService.fetchLocations(city, validApiKey);

            // Assert
            assertNotNull(locations);


        }
    }

    @Test
    void fetchLocation_throwsIOException_returnsEmptyList() {
        // Arrange
        String city = "Toronto";

        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString()))
                    .thenThrow(new IOException("Simulated IOException"));

            // Act
            List<Location> locations = apiService.fetchLocations(city, validApiKey);

            // Assert
            assertNotNull(locations); // The list should not be null
            assertTrue(locations.isEmpty()); // The list should be empty if an exception occurs
            mockedStatic.verify(() -> HttpUtils.makeApiCall(anyString()), times(1));
        }
    }

    @Test

    // TODO: Complete this test
    void isApiKeyValid_throwsJSONException_returnsFalse() {

        try (MockedStatic<HttpUtils> mockedStatic = mockStatic(HttpUtils.class)) {
            mockedStatic.when(() -> HttpUtils.makeApiCall(anyString()))
                    .thenReturn(new JSONException("Simulated JSONException")); // Simulates invalid JSON response

            // Act
            boolean isValid = apiService.isApiKeyValid(validApiKey);

            // Assert
            assertFalse(isValid); // Should return false if JSONException occurs
            mockedStatic.verify(() -> HttpUtils.makeApiCall(anyString()), times(1));
        }
    }

}

