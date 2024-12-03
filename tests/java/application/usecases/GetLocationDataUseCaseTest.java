package application.usecases;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import java.util.Arrays;
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
        assertEquals("CityName", result.get(0).getCity()); // Use getCity instead of getCityName
        verify(mockApiService).fetchLocations(city, apiKey);
    }
}
