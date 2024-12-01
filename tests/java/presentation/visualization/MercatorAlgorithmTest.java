package presentation.visualization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.Constants;

public class MercatorAlgorithmTest {
    private MercatorAlgorithm mercatorAlgorithm;

    @BeforeEach
    void setUp() {
        // Initialize with a sample latitude and longitude
        mercatorAlgorithm = new MercatorAlgorithm(43.6532, -79.3832); // Toronto
    }

    @Test
    void testNormalizeCoordinates() {
        double width = 1200.0;
        double height = 800.0;

        // Normalize coordinates
        double[] result = mercatorAlgorithm.normalizeCoordinates(height, width);

        // Validate x and y
        double expectedX = (-79.3832 + Constants.MERCATOR_MAX_DEGREE / 2.0) * (width / Constants.MERCATOR_MAX_DEGREE);
        double latRad = Math.toRadians(43.6532);
        double mercN = Math.log(Math.tan((Math.PI / 4.0) + (latRad / 2.0)));
        double expectedY = (height / 2.0) - (width * mercN / (2.0 * Math.PI));

        assertEquals(expectedX, result[0], 0.001, "The x-coordinate should match the expected value.");
        assertEquals(expectedY, result[1], 0.001, "The y-coordinate should match the expected value.");
    }

    @Test
    void testReverseCoordinates() {
        double width = 1200.0;
        double height = 800.0;

        // Coordinates to reverse
        double xVal = 400.0;
        double yVal = 300.0;

        // Reverse coordinates
        double[] result = MercatorAlgorithm.reverseCoordinates(xVal, yVal, width, height);

        // Calculate expected longitude and latitude
        double expectedLongitude = (xVal / width) * Constants.MERCATOR_MAX_DEGREE - (Constants.MERCATOR_MAX_DEGREE / 2.0);
        double mercN = ((height / 2.0) - yVal) * (2.0 * Math.PI / width);
        double expectedLatitude = Math.toDegrees(Math.atan(Math.sinh(mercN)));

        assertEquals(expectedLongitude, result[0], 0.001, "The longitude should match the expected value.");
        assertEquals(expectedLatitude, result[1], 0.001, "The latitude should match the expected value.");
    }

    @Test
    void testNormalizeCoordinatesWithEdgeCase() {
        double width = 1200.0;
        double height = 800.0;

        // Test at (0, 0)
        mercatorAlgorithm = new MercatorAlgorithm(0, 0);
        double[] result = mercatorAlgorithm.normalizeCoordinates(height, width);

        // Expected coordinates at the center of the map
        double expectedX = width / 2.0;
        double expectedY = height / 2.0;

        assertEquals(expectedX, result[0], 0.001, "The x-coordinate for (0, 0) should be the center.");
        assertEquals(expectedY, result[1], 0.001, "The y-coordinate for (0, 0) should be the center.");
    }

    @Test
    void testReverseCoordinatesWithEdgeCase() {
        double width = 1200.0;
        double height = 800.0;

        // Test at map center
        double xVal = width / 2.0;
        double yVal = height / 2.0;

        double[] result = MercatorAlgorithm.reverseCoordinates(xVal, yVal, width, height);

        // Expected longitude and latitude for center
        assertEquals(0, result[0], 0.001, "The longitude at the center should be 0.");
        assertEquals(0, result[1], 0.001, "The latitude at the center should be 0.");
    }

    @Test
    void testNormalizeAndReverseCoordinatesConsistency() {
        double width = 1200.0;
        double height = 800.0;

        // Normalize coordinates
        double[] normalized = mercatorAlgorithm.normalizeCoordinates(height, width);

        // Reverse coordinates
        double[] reversed = MercatorAlgorithm.reverseCoordinates(normalized[0], normalized[1], width, height);

        // Check if the reversed coordinates match the original ones
        assertEquals(-79.3832, reversed[0], 0.001, "The reversed longitude should match the original.");
        assertEquals(43.6532, reversed[1], 0.001, "The reversed latitude should match the original.");
    }
}
