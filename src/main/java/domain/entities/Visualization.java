package domain.entities;

import java.util.List;

/**
 * Class for visualizing and comparing weather data trends.
 */
public class Visualization {
    // Type of graph (e.g., line, bar)
    private String graphType;

    // Data points for visualization
    private List<WeatherData> dataPoints;

    // Comparison data points for visualization
    private List<WeatherData> comparisonData;

    /**
     * Constructor to initialize visualization with two data sets and a graph type.
     *
     * @param graphType Type of graph to generate (e.g., line, bar).
     * @param dataPoints List of WeatherData points for the main dataset.
     * @param comparisonData List of WeatherData points for the comparison dataset.
     */
    public Visualization(String graphType, List<WeatherData> dataPoints, List<WeatherData> comparisonData) {
        this.graphType = graphType;
        this.dataPoints = dataPoints;
        this.comparisonData = comparisonData;
    }

    /**
     * Getter for graph type.
     *
     * @return the type of graph.
     */
    public String getGraphType() {
        return graphType;
    }

    /**
     * Setter for graph type.
     *
     * @param graphType the type of graph to set.
     */
    public void setGraphType(String graphType) {
        this.graphType = graphType;
    }

    /**
     * Getter for data points.
     *
     * @return list of main dataset points.
     */
    public List<WeatherData> getDataPoints() {
        return dataPoints;
    }

    /**
     * Setter for data points.
     *
     * @param dataPoints list of main dataset points to set.
     */
    public void setDataPoints(List<WeatherData> dataPoints) {
        this.dataPoints = dataPoints;
    }

    /**
     * Getter for comparison data points.
     *
     * @return list of comparison dataset points.
     */
    public List<WeatherData> getComparisonData() {
        return comparisonData;
    }

    /**
     * Setter for comparison data points.
     *
     * @param comparisonData list of comparison dataset points to set.
     */
    public void setComparisonData(List<WeatherData> comparisonData) {
        this.comparisonData = comparisonData;
    }
}
