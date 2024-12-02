package domain.entities;

/**
 * A class to represent coordinates of a given location on Earth.
 * Stores the longitude and latitude of said location.
 */
public class Coordinate {
    private final double latitude;
    private final double longitude;

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
