package usecases;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import domain.entities.Location;
import domain.interfaces.ApiService;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetLocationsWindowUseCase;
import presentation.ui.windows.ErrorLocationsWindow;
import presentation.ui.windows.LocationsWindow;

import java.util.Arrays;

public class GetLocationsWindowUseCaseTest {

    @Test
    public void testGenerateLocationsWindowSuccess() {
        // Mock ApiService
        ApiService mockApiService = mock(ApiService.class);
        when(mockApiService.fetchLocations(anyString(), anyString()))
                .thenReturn(Arrays.asList(new Location("TestCity", "TestCountry", "TestState", 10.0, 20.0)));

        // Create GetLocationDataUseCase with the mocked ApiService
        GetLocationDataUseCase locationDataUseCase = new GetLocationDataUseCase(mockApiService);

        // Define the expected type
        String expectedWindowType = "NORMAL";

        // Create an instance of the use case
        GetLocationsWindowUseCase useCase = new GetLocationsWindowUseCase();
        int[] dimensions = {800, 600};

        // Test the execute method
        LocationsWindow window = useCase.execute(expectedWindowType, dimensions, 5, locationDataUseCase, "dummyApiKey", mockApiService);

        // Assert the result
        assertNotNull(window);
        assertEquals(expectedWindowType, window.getType());
    }

    @Test
    public void testGenerateLocationsWindowFailure() {
        // Mock ApiService
        ApiService mockApiService = mock(ApiService.class);
        when(mockApiService.fetchLocations(anyString(), anyString())).thenReturn(null);

        // Create GetLocationDataUseCase with the mocked ApiService
        GetLocationDataUseCase locationDataUseCase = new GetLocationDataUseCase(mockApiService);

        // Create an instance of the use case
        GetLocationsWindowUseCase useCase = new GetLocationsWindowUseCase();
        int[] dimensions = {800, 600};

        // Test the execute method
        LocationsWindow window = useCase.execute("default", dimensions, 5, locationDataUseCase, "dummyApiKey", mockApiService);

        // Assert the result
        assertNotNull(window);
        assertTrue(window instanceof ErrorLocationsWindow);
    }
}
