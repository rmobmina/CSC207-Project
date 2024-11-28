package presentation.visualization;

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
        x = (longitude + 180) * (width / 360);

        // convert from degrees to radians
        final double latRad = latitude * Math.PI / 180;

        // get y value
        final double mercN = Math.log(Math.tan((Math.PI/4)+(latRad/2)));
        y = (height / 2) - (width * mercN/(2 * Math.PI));
        System.out.println(x);
        System.out.println(y);

        return new double[] {x, y};
    }

    public static void main(String[] args) {
        MercatorAlgorithm mercatorAlgorithm = new MercatorAlgorithm(41.145556, 121.2322);
        mercatorAlgorithm.normalizeCoordinates(100, 200);
    }

}
