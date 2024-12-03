package application.usecases;

import domain.entities.Coordinate;
import utils.UseCaseInteractor;

/**
 * A use case that converts a geographical coordinate (latitude and longitude)
 * to Mercator projection coordinates (x, y) for mapping.
 */
public class ConvertToMercatorUseCase extends UseCaseInteractor {

    // Constants for Mercator projection calculations
    private static final double OFFSET_LONGITUDE = 180;
    private static final double FULL_CIRCLE = 360;
    private static final double PI_OVER_FOUR = Math.PI / 4;

    /**
     * Converts a geographical coordinate (latitude, longitude) to Mercator projection coordinates (x, y).
     *
     * @param coordinate The geographic coordinate to be converted.
     * @param mapWidth The width of the map.
     * @param mapHeight The height of the map.
     * @return The Mercator projection coordinates as a double array [x, y].
     */
    public double[] execute(Coordinate coordinate, double mapWidth, double mapHeight) {
        final double longitude = coordinate.getLongitude();
        final double latitude = coordinate.getLatitude();

        // Normalize x-coordinate
        final double x = (longitude + OFFSET_LONGITUDE) * (mapWidth / FULL_CIRCLE);

        // Normalize y-coordinate
        final double latRad = Math.toRadians(latitude);
        final double mercN = Math.log(Math.tan(PI_OVER_FOUR + latRad / 2));
        final double y = (mapHeight / 2) - (mapWidth * mercN / (2 * Math.PI));

        return new double[] {x, y};
    }
}
