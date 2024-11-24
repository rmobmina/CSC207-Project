package presentation.visualization;

import java.awt.geom.Point2D;

/**
 * A class used to convert a location's longitude and latitude into Mercator Projection coordinates.
 *  FINISH LATER
 */
public class MercatorAlgorithm {
    // Earth's radius in meters (mean radius)
    private static final double EARTH_RADIUS = 6378137.0;

    private final double latitude;
    private final double longitude;
    private double x;
    private double y;

    public MercatorAlgorithm(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Converts latitude and longitude to Mercator projection (x, y) coordinates.
     * @return A double array [x, y] where x and y are Mercator projection coordinates in meters.
     */
    public void toMercator() {
        // Calculate x and y using the Mercator formula
        this.x = EARTH_RADIUS * Math.toRadians(longitude);
        this.y = EARTH_RADIUS * Math.log(Math.tan(Math.PI / 4 + Math.toRadians(latitude) / 2));

    }

    /**
     * Converts Mercator (x, y) coordinates back to latitude and longitude.
     * @return A double array [latitude, longitude] where latitude and longitude are in degrees.
     */
    public double[] fromMercator() {
        // Calculate longitude
        final double lonRad = x / EARTH_RADIUS;

        // Calculate latitude
        final double latRad = 2 * Math.atan(Math.exp(y / EARTH_RADIUS)) - Math.PI / 2;

        // Convert back to degrees
        final double lat = Math.toDegrees(latRad);
        final double lon = Math.toDegrees(lonRad);

        return new double[]{lat, lon};
    }

    /**
     * TODO: Normalizing function
     *  Given an image's length and width in pixels, return x and y values of a mercator point
     *      determined by our previous calculations.
     *      - Assume that the image given is a correct Mercator Map.
     */

}
