package domain.entities;

import java.util.List;

public class Visualization {
    private String graphType;
    private List<WeatherData> dataPoints;
    private List<WeatherData> comparisonData;

    // Constructor, Getters, and Setters

    public Visualization(String graphType, List<WeatherData> dataPoints, List<WeatherData> comparisonData) {
        this.graphType = graphType;
        this.dataPoints = dataPoints;
        this.comparisonData = comparisonData;
    }

    // Getters and Setters
    // ...
}
