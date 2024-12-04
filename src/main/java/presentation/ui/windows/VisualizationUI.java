package presentation.ui.windows;

import javax.swing.*;

import application.dto.WeatherDataDTO;
import presentation.visualization.BarGraphWeatherComparison;
import presentation.visualization.LineGraphWeatherComparison;

/**
 * This class handles the visualization of weather data for one or two cities.
 */
public class VisualizationUI {

    private WeatherDataDTO firstCityWeatherData;
    private WeatherDataDTO secondCityWeatherData;

    private final int numOfCities;
    private final String graphName;
    private final JPanel mainPanel;

    /**
     * Constructor for VisualizationUI.
     *
     * @param numOfCities The number of cities (1 or 2) being visualized.
     * @param mainPanel   The main panel for displaying error messages or visualizations.
     */
    public VisualizationUI(int numOfCities, JPanel mainPanel) {
        this.numOfCities = numOfCities;
        this.mainPanel = mainPanel;

        // Set the graph name based on the number of cities
        if (numOfCities == 1) {
            graphName = "City Historical Data";
        }
        else {
            graphName = "Two Cities Weather Comparison";
        }
    }

    /**
     * Opens the visualization window for the selected data type.
     */
    public void openVisualization() {
        if (firstCityWeatherData == null || (numOfCities == 2 && secondCityWeatherData == null)) {
            showError("Fetch weather data for the selected city or cities first.");
        }
        else {
            SwingUtilities.invokeLater(() -> {
                GraphSelectionWindow graphWindow = new GraphSelectionWindow(graphType -> {
                    if ("line".equals(graphType)) {
                        visualizeLineGraph();
                    }
                    else if ("bar".equals(graphType)) {
                        visualizeBarGraph();
                    }
                });
                graphWindow.display();
            });
        }
    }

    /**
     * Visualizes the data as a line graph.
     */
    private void visualizeLineGraph() {
        LineGraphWeatherComparison lineGraph = new LineGraphWeatherComparison(graphName);

        // Add data for the first city
        firstCityWeatherData.getTemperatureHistory().forEach((date, value) ->
                lineGraph.addData("City 1 Temperature", date, value)
        );

        // If visualizing two cities, add data for the second city
        if (numOfCities == 2) {
            secondCityWeatherData.getTemperatureHistory().forEach((date, value) ->
                    lineGraph.addData("City 2 Temperature", date, value)
            );
        }

        lineGraph.display();
    }

    /**
     * Visualizes the data as a bar graph.
     */
    private void visualizeBarGraph() {
        BarGraphWeatherComparison barGraph = new BarGraphWeatherComparison(graphName);

        // Add data for the first city
        barGraph.addData("City 1", "Temperature",
                firstCityWeatherData.getAverageWeatherData("temperatureMeanDaily"));
        barGraph.addData("City 1", "Precipitation",
                firstCityWeatherData.getAverageWeatherData("precipitationDaily"));
        barGraph.addData("City 1", "Humidity",
                firstCityWeatherData.getAverageWeatherData("humidityHourly"));
        barGraph.addData("City 1", "Wind Speed",
                firstCityWeatherData.getAverageWeatherData("windSpeedDaily"));

        // If visualizing two cities, add data for the second city
        if (numOfCities == 2) {
            barGraph.addData("City 2", "Temperature",
                    secondCityWeatherData.getAverageWeatherData("temperatureMeanDaily"));
            barGraph.addData("City 2", "Precipitation",
                    secondCityWeatherData.getAverageWeatherData("precipitationDaily"));
            barGraph.addData("City 2", "Humidity",
                    secondCityWeatherData.getAverageWeatherData("humidityHourly"));
            barGraph.addData("City 2", "Wind Speed",
                    secondCityWeatherData.getAverageWeatherData("windSpeedDaily"));
        }

        barGraph.display();
    }

    /**
     * Displays an error message in a dialog box.
     *
     * @param errorMessage The error message to display.
     */
    private void showError(String errorMessage) {
        JOptionPane.showMessageDialog(mainPanel, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
