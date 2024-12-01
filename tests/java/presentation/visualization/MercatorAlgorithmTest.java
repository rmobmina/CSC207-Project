package presentation.visualization;

import org.junit.jupiter.api.Test;
import utils.Constants;

import static org.junit.jupiter.api.Assertions.*;

class MercatorAlgorithmTest {

    @Test
    void normalizeCoordinates_centerPoint_mapsToCenter() {
        // Arrange
        double latitude = 0.0; // Equator
        double longitude = 0.0; // Prime Meridian
        double height = 500.0;
        double width = 1000.0;

        MercatorAlgorithm mercator = new MercatorAlgorithm(latitude, longitude);

        // Act
        double[] result = mercator.normalizeCoordinates(height, width);

        // Assert
        assertEquals(width / 2.0, result[0], 0.0001, "X should map to the center of the image width");
        assertEquals(height / 2.0, result[1], 0.0001, "Y should map to the center of the image height");
    }

    @Test
    void normalizeCoordinates_maxLongitude_mapsToRightEdge() {
        // Arrange
        double latitude = 0.0;
        double longitude = Constants.MERCATOR_MAX_DEGREE / 2.0; // Maximum longitude
        double height = 500.0;
        double width = 1000.0;

        MercatorAlgorithm mercator = new MercatorAlgorithm(latitude, longitude);

        // Act
        double[] result = mercator.normalizeCoordinates(height, width);

        // Assert
        assertEquals(width, result[0], 0.0001, "X should map to the right edge of the image width");
    }

    @Test
    void normalizeCoordinates_minLongitude_mapsToLeftEdge() {
        // Arrange
        double latitude = 0.0;
        double longitude = -Constants.MERCATOR_MAX_DEGREE / 2.0; // Minimum longitude
        double height = 500.0;
        double width = 1000.0;

        MercatorAlgorithm mercator = new MercatorAlgorithm(latitude, longitude);

        // Act
        double[] result = mercator.normalizeCoordinates(height, width);

        // Assert
        assertEquals(0.0, result[0], 0.0001, "X should map to the left edge of the image width");
    }

    @Test
    void normalizeCoordinates_highLatitude_clampsToValidY() {
        // Arrange
        double latitude = 85.0; // Near North Pole
        double longitude = 0.0;
        double height = 500.0;
        double width = 1000.0;

        MercatorAlgorithm mercator = new MercatorAlgorithm(latitude, longitude);

        // Act
        double[] result = mercator.normalizeCoordinates(height, width);

        // Assert
        assertTrue(result[1] < height / 2.0, "Y should be higher (closer to the top) for high latitudes");
    }

    @Test
    void normalizeCoordinates_lowLatitude_clampsToValidY() {
        // Arrange
        double latitude = -85.0; // Near South Pole
        double longitude = 0.0;
        double height = 500.0;
        double width = 1000.0;

        MercatorAlgorithm mercator = new MercatorAlgorithm(latitude, longitude);

        // Act
        double[] result = mercator.normalizeCoordinates(height, width);

        // Assert
        assertTrue(result[1] > height / 2.0, "Y should be lower (closer to the bottom) for low latitudes");
    }

    @Test
    void normalizeCoordinates_randomCoordinates_returnsWithinBounds() {
        // Arrange
        double latitude = 45.0; // Mid-latitude
        double longitude = 90.0; // Mid-longitude
        double height = 600.0;
        double width = 1200.0;

        MercatorAlgorithm mercator = new MercatorAlgorithm(latitude, longitude);

        // Act
        double[] result = mercator.normalizeCoordinates(height, width);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.length);
        assertTrue(result[0] >= 0 && result[0] <= width, "X should be within image width");
        assertTrue(result[1] >= 0 && result[1] <= height, "Y should be within image height");
    }

}
