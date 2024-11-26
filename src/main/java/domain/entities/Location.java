package domain.entities;

/**
 * A class representing a city.
 * Stores the city, longitude, latitude.
 */
public class Location {
    private String city;
    private String state = "N/A";
    private String country = "N/A";
    private double latitude;
    private double longitude;

    public Location(String city, String state, String country, double latitude, double longitude) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(String city, double latitude, double longitude) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the full name of the location, including the state/province name and country.
     * @return the name of the given location in the form 'CITY_NAME, STATE_NAME, COUNTRY_NAME'
     */
    public String fullLocationName() {
        return city + ", " + state + ", " + country;
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
