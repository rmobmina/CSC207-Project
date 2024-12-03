package application.usecases;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import domain.entities.Location;
import domain.interfaces.ApiService;

public class GetLocationDataUseCaseTest {

    @Test
    public void testFetchLocationSuccess() {
        // Mock the ApiService dependency
        ApiService mockApiService = mock(ApiService.class);
        GetLocationDataUseCase useCase = new GetLocationDataUseCase(mockApiService);

        // Create a mock list of locations
        Location mockLocation = new Location("CityName", "Country", "State", 0.0, 0.0);
        List<Location> mockLocations = Arrays.asList(mockLocation);

        // Mock the API service behavior
        String city = "CityName";
        String apiKey = "validApiKey";
        when(mockApiService.fetchLocations(city, apiKey)).thenReturn(mockLocations);

        // Execute the use case
        List<Location> result = useCase.execute(city, apiKey);

        // Assertions
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("CityName", result.get(0).getCity());
        verify(mockApiService).fetchLocations(city, apiKey);
        assertFalse(useCase.isUseCaseFailed());
    }

    @Test
    public void testFetchLocationEmptyResult() {
        // Mock the ApiService dependency
        ApiService mockApiService = mock(ApiService.class);
        GetLocationDataUseCase useCase = new GetLocationDataUseCase(mockApiService);

        // Mock the API service behavior
        String city = "UnknownCity";
        String apiKey = "validApiKey";
        when(mockApiService.fetchLocations(city, apiKey)).thenReturn(Collections.emptyList());

        // Execute the use case
        List<Location> result = useCase.execute(city, apiKey);

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockApiService).fetchLocations(city, apiKey);
        assertTrue(useCase.isUseCaseFailed());
    }


    @Test
    public void testFetchLocationNullResult() {
        // Mock the ApiService dependency
        ApiService mockApiService = mock(ApiService.class);
        GetLocationDataUseCase useCase = new GetLocationDataUseCase(mockApiService);

        // Mock the API service behavior
        String city = "InvalidCity";
        String apiKey = "validApiKey";
        when(mockApiService.fetchLocations(city, apiKey)).thenReturn(null);

        // Execute the use case
        List<Location> result = useCase.execute(city, apiKey);

        // Assertions
        assertNull(result);
        verify(mockApiService).fetchLocations(city, apiKey);
        assertTrue(useCase.isUseCaseFailed());
    }

    @Test
    public void testFetchLocationInvalidApiKey() {
        // Mock the ApiService dependency
        ApiService mockApiService = mock(ApiService.class);
        GetLocationDataUseCase useCase = new GetLocationDataUseCase(mockApiService);

        // Mock the API service behavior
        String city = "CityName";
        String apiKey = "invalidApiKey";
        when(mockApiService.fetchLocations(city, apiKey)).thenThrow(new RuntimeException("Invalid API Key"));

        // Execute the use case and handle exception
        try {
            useCase.execute(city, apiKey);
            fail("Expected RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Invalid API Key", e.getMessage());
        }

        // Verify behavior
        verify(mockApiService).fetchLocations(city, apiKey);
        assertTrue(useCase.isUseCaseFailed());
    }

    @Test
    public void testFetchLocationApiServiceThrowsException() {
        // Mock the ApiService dependency
        ApiService mockApiService = mock(ApiService.class);
        GetLocationDataUseCase useCase = new GetLocationDataUseCase(mockApiService);

        // Mock the API service behavior
        String city = "CityName";
        String apiKey = "validApiKey";
        when(mockApiService.fetchLocations(city, apiKey)).thenThrow(new RuntimeException("Service Unavailable"));

        // Execute the use case and handle exception
        try {
            useCase.execute(city, apiKey);
            fail("Expected RuntimeException not thrown");
        } catch (RuntimeException e) {
            assertEquals("Service Unavailable", e.getMessage());
        }

        // Verify behavior
        verify(mockApiService).fetchLocations(city, apiKey);
        assertTrue(useCase.isUseCaseFailed());
    }
}
