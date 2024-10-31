package domain.entities;

import java.time.LocalDate;
import java.util.List;

public class WeatherData {
    private String location;
    private LocalDate date;
    private double temperature;
    private int humidity;
    private double windSpeed;
    private double precipitation;
    private List<String> alerts;

    // Constructor, Getters, and Setters

    public WeatherData(String location, LocalDate date, double temperature, int humidity, double windSpeed,
                       double precipitation, List<String> alerts) {
        this.location = location;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.precipitation = precipitation;
        this.alerts = alerts;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public List<String> getAlerts() {
        return alerts;
    }

}
