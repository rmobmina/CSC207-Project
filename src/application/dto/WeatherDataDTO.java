package application.dto;

import java.time.LocalDate;
import java.util.List;

public class WeatherDataDTO {
    public String location;
    public LocalDate date;
    public double temperature;
    public int humidity;
    public double windSpeed;
    public double precipitation;
    public List<String> alerts;

    // Constructor for mapping from domain entities
    public WeatherDataDTO(String location, LocalDate date, double temperature, int humidity, double windSpeed,
                          double precipitation, List<String> alerts) {
        this.location = location;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.precipitation = precipitation;
        this.alerts = alerts;
    }
}
