package domain.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class representing a city.
 * Stores the city, longitude, latitude.
 */
public class Location implements Serializable {

    // for serializable
    private static final long serialVersionUID = 1L;

    private final String city;
    private final String state;
    private final String country;
    private final double latitude;
    private final double longitude;

    /**
     * Constructs a Location with city, state, country, latitude, and longitude.
     *
     * @param city      The name of the city.
     * @param state     The state or province.
     * @param country   The country.
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     */
    public Location(String city, String state, String country, double latitude, double longitude) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Constructs a Location with city, latitude, and longitude.
     * Defaults state and country to "N/A".
     *
     * @param city      The name of the city.
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     */
    public Location(String city, double latitude, double longitude) {
        this(city, "N/A", "N/A", latitude, longitude);
    }

    /**
     * Returns the full name of the location, including the state/province name and country.
     *
     * @return the name of the given location in the form 'CITY_NAME, STATE_NAME, COUNTRY_NAME'
     */
    public String fullLocationName() {
        return city + ", " + state + ", " + country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns the city name.
     *
     * @return the city name.
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the state name.
     *
     * @return the state name.
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the country name.
     *
     * @return the country name.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Checks equality of two Location objects based on city, state, and country.
     *
     * @param obj The object to compare.
     * @return true if the locations are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Location location = (Location) obj;
        return Objects.equals(city, location.city)
                && Objects.equals(state, location.state)
                && Objects.equals(country, location.country);
    }

    /**
     * Computes the hash code for the Location object.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(city, state, country);
    }
}
