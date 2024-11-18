package domain.entities;

/**
 * A class representing a city.
 * Stores the city, longitude, latitude.
 */
public class Location {
    private final String city;
    private final String state;
    private final String country;
    private final double latitude;
    private final double longitude;

    public Location(String city, String state, String country, double latitude, double longitude) {
        this.city = city;
        this.state = state;
        this.country = country;
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

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

}
