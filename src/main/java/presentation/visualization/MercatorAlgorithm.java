package presentation.visualization;

public class MercatorProjection {

    // Earth's radius in meters (mean radius)
    private static final double EARTH_RADIUS = 6378137.0;

    /**
     * Converts latitude and longitude to Mercator projection (x, y) coordinates.
     *
     * @param latitude Latitude in degrees.
     * @param longitude Longitude in degrees.
     * @return A double array [x, y] where x and y are Mercator projection coordinates in meters.
     */
    public static double[] toMercator(double latitude, double longitude) {
        // Convert latitude and longitude to radians
        double latRad = Math.toRadians(latitude);
        double lonRad = Math.toRadians(longitude);

        // Calculate x and y using the Mercator formula
        double x = EARTH_RADIUS * lonRad;
        double y = EARTH_RADIUS * Math.log(Math.tan(Math.PI / 4 + latRad / 2));

        return new double[]{x, y};
    }

    /**
     * Converts Mercator (x, y) coordinates back to latitude and longitude.
     *
     * @param x Mercator x coordinate in meters.
     * @param y Mercator y coordinate in meters.
     * @return A double array [latitude, longitude] where latitude and longitude are in degrees.
     */
    public static double[] fromMercator(double x, double y) {
        // Calculate longitude
        double lonRad = x / EARTH_RADIUS;

        // Calculate latitude
        double latRad = 2 * Math.atan(Math.exp(y / EARTH_RADIUS)) - Math.PI / 2;

        // Convert back to degrees
        double latitude = Math.toDegrees(latRad);
        double longitude = Math.toDegrees(lonRad);

        return new double[]{latitude, longitude};
    }
}
