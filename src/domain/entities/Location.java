package domain.entities;

/**
 * A class representing a city.
 * Stores the city, longitude, latitude.
 */
public class Location {
    private String city;
    private double latitude;
    private double longitude;

    public Location(String city, double latitude, double longitude) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return Double.toString(latitude);
    }

    public String getLongitude() {
        return Double.toString(longitude);
    }

    public String getCity() {
        return city;
    }

}
