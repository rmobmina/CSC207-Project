package presentation.visualization;

import utils.Constants;

/**
 * A class used to convert a location's longitude and latitude into Mercator Projection coordinates.
 *  FINISH LATER
 */
public class MercatorAlgorithm {
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
     * @param height a double representing the height of the image in pixels. Represented with doubles to avoid Java's
     *               rounding of int division.
     * @param width a double representing the width of the image in pixels. Represented with doubles to avoid Java's
     *              rounding of int division.
     * @return A double array [x, y] where x and y are Mercator projection coordinates in meters.
     */
    public double[] normalizeCoordinates(double height, double width) {
        // get x value
        x = (longitude + Constants.MERCATOR_MAX_DEGREE / 2.0) * (width / Constants.MERCATOR_MAX_DEGREE);

        // convert from degrees to radians
        final double latRad = latitude * Math.PI / 180.0;

        // get y value
        final double mercN = Math.log(Math.tan((Math.PI / 4.0) + (latRad / 2.0)));
        y = (height / 2.0) - (width * mercN / (2.0 * Math.PI));

        return new double[] {x, y};
    }

    /**
     * Converts Mercator projection (x, y) coordinates to longitude and latitude.
     * @param x a double representing the x-coordinate in pixels (relative to image width).
     * @param y a double representing the y-coordinate in pixels (relative to image height).
     * @param width a double representing the width of the image in pixels.
     * @param height a double representing the height of the image in pixels.
     * @return A double array [longitude, latitude] where longitude and latitude are in degrees.
     */
    public static double[] reverseCoordinates(double x, double y, double width, double height)  {
        // Reversing Longitude
        final double lambda = (x / width) * Constants.MERCATOR_MAX_DEGREE - (Constants.MERCATOR_MAX_DEGREE / 2.0);

        // Reversing Latitude
        final double mercN = ((height / 2.0) - y) * (2.0 * Math.PI / width);
        final double latitude = Math.toDegrees(Math.atan(Math.sinh(mercN)));

        return new double[] {lambda, latitude};

    }

}
