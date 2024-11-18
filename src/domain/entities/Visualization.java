package domain.entities;

import java.util.List;

/**
 * An unimplemented class used to visualize weather data.
 * This visualization class is mainly intended for bar graphs and vertex graphs.
 */
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
