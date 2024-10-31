package domain.entities;

public class Location {
    private String city;
    private double latitude;
    private double longitude;
    private String country;

    // Constructor, Getters, and Setters

    public Location(String city, double latitude, double longitude, String country) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // Getters and Setters
    // ...
}
