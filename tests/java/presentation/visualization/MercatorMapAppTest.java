//package presentation.visualization;
//
//import application.usecases.ConvertToMercatorUseCase;
//import domain.entities.Coordinate;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for MercatorMapApp.
// */
//class MercatorMapAppTest {
//
//    private SwingMapRenderer mockRenderer;
//    private MercatorMapApp app;
//
//    @BeforeEach
//    void setUp() {
//        // Mock the renderer
//        mockRenderer = Mockito.mock(SwingMapRenderer.class);
//
//        // Initialize the MercatorMapApp with the mocked renderer
//        app = new MercatorMapApp(mockRenderer);
//    }
//
//    @Test
//    void testConstructorInitializesRenderer() {
//        // Verify the renderer is set correctly in the constructor
//        assertNotNull(app, "MercatorMapApp should be initialized correctly.");
//    }
//
//    @Test
//    void testDisplayCoordinateInvokesRenderer() {
//        // Prepare test data
//        Coordinate coordinate = new Coordinate(43.6532, -79.3832); // Toronto
//        double mapWidth = 1200.0;
//        double mapHeight = 800.0;
//
//        // Execute the method under test
//        app.displayCoordinate(coordinate, mapWidth, mapHeight);
//
//        // Capture the arguments passed to the renderer
//        ArgumentCaptor<Double> xCaptor = ArgumentCaptor.forClass(Double.class);
//        ArgumentCaptor<Double> yCaptor = ArgumentCaptor.forClass(Double.class);
//        verify(mockRenderer, times(1)).renderMap(xCaptor.capture(), yCaptor.capture());
//
//        // Validate the coordinates passed to the renderer
//        ConvertToMercatorUseCase useCase = new ConvertToMercatorUseCase();
//        double[] expectedCoordinates = useCase.execute(coordinate, mapWidth, mapHeight);
//
//        assertEquals(expectedCoordinates[0], xCaptor.getValue(), 0.001, "The x-coordinate should match the expected Mercator x.");
//        assertEquals(expectedCoordinates[1], yCaptor.getValue(), 0.001, "The y-coordinate should match the expected Mercator y.");
//    }
//
//    @Test
//    void testDisplayCoordinateWithEdgeCase() {
//        // Prepare test data: coordinate at (0, 0)
//        Coordinate coordinate = new Coordinate(0.0, 0.0);
//        double mapWidth = 1200.0;
//        double mapHeight = 800.0;
//
//        // Execute the method under test
//        app.displayCoordinate(coordinate, mapWidth, mapHeight);
//
//        // Capture the arguments passed to the renderer
//        ArgumentCaptor<Double> xCaptor = ArgumentCaptor.forClass(Double.class);
//        ArgumentCaptor<Double> yCaptor = ArgumentCaptor.forClass(Double.class);
//        verify(mockRenderer, times(1)).renderMap(xCaptor.capture(), yCaptor.capture());
//
//        // Validate the coordinates passed to the renderer
//        ConvertToMercatorUseCase useCase = new ConvertToMercatorUseCase();
//        double[] expectedCoordinates = useCase.execute(coordinate, mapWidth, mapHeight);
//
//        assertEquals(expectedCoordinates[0], xCaptor.getValue(), 0.001, "The x-coordinate should match the expected Mercator x for (0, 0).");
//        assertEquals(expectedCoordinates[1], yCaptor.getValue(), 0.001, "The y-coordinate should match the expected Mercator y for (0, 0).");
//    }
//}
