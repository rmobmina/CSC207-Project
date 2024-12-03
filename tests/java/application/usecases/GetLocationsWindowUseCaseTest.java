package application.usecases;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import domain.entities.Location;
import domain.interfaces.ApiService;
import presentation.ui.dashboard.NewDashBoardUi;
import presentation.ui.windows.ErrorLocationsWindow;
import presentation.ui.windows.LocationsWindow;

import java.awt.Window;
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

        // Mock NewDashBoardUi
        NewDashBoardUi mockDashboard = mock(NewDashBoardUi.class);

        // Define the expected type
        Window.Type expectedWindowType = Window.Type.NORMAL;

        // Create an instance of the use case
        GetLocationsWindowUseCase useCase = new GetLocationsWindowUseCase();
        int[] dimensions = {800, 600};

        // Test the execute method
        LocationsWindow window = useCase.execute(expectedWindowType.toString(), dimensions, 5, locationDataUseCase, "dummyApiKey", mockApiService, mockDashboard);

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

        // Mock NewDashBoardUi
        NewDashBoardUi mockDashboard = mock(NewDashBoardUi.class);

        // Create an instance of the use case
        GetLocationsWindowUseCase useCase = new GetLocationsWindowUseCase();
        int[] dimensions = {800, 600};

        // Test the execute method
        LocationsWindow window = useCase.execute("default", dimensions, 5, locationDataUseCase, "dummyApiKey", mockApiService, mockDashboard);

        // Assert the result
        assertNotNull(window);
        assertTrue(window instanceof ErrorLocationsWindow);
    }
}
