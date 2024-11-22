package domain.entities;

import java.util.List;

/**
 * Class for visualizing and comparing weather data trends.
 */
public class Visualization {
    private String graphType; // Type of graph (e.g., line, bar)
    private List<WeatherData> dataPoints; // Data points for visualization
    private List<WeatherData> comparisonData; // Comparison data points for visualization

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
     * Compares temperature trends between two data sets and outputs the differences.
     *
     * @param city1Trends List of temperature trends for the first city.
     * @param city2Trends List of temperature trends for the second city.
     */
    public void displayComparison(List<Double> city1Trends, List<Double> city2Trends) {
        System.out.println("Comparing temperature trends:");
        for (int i = 0; i < Math.min(city1Trends.size(), city2Trends.size()); i++) {
            System.out.printf("Day %d: City1=%.2f°C, City2=%.2f°C, Difference=%.2f°C%n",
                    i + 1, city1Trends.get(i), city2Trends.get(i),
                    Math.abs(city1Trends.get(i) - city2Trends.get(i)));
        }
        // add graphical visualization here not sure what library lmao
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
