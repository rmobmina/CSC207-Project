package presentation.ui.windows;

import application.dto.WeatherDataDTO;
import domain.entities.WeatherData;
import presentation.visualization.BarGraphWeatherComparison;
import presentation.visualization.LineGraphWeatherComparison;

import javax.swing.*;

public class VisualizationUI {

    WeatherDataDTO firstCityWeatherData;
    WeatherDataDTO secondCityWeatherData;

    private int numOfCities;
    private String graphName;
    private JPanel mainPanel;

    public VisualizationUI(int numOfCities, JPanel mainPanel) {
        this.numOfCities = numOfCities;
        // Sets this as the main panel for the visualization (and error messages to pop up from)
        this.mainPanel = mainPanel;
        if (numOfCities == 1) {
            graphName = "City Historical Data";
        }
        else {
            graphName = "Two Cities Weather Comparison";
        }
    }

    public void openVisualization(String temperatureCategory) {
        if ((firstCityWeatherData == null || secondCityWeatherData == null) ) {
            showError("Fetch weather data for both cities first.");
        }
        else {

            SwingUtilities.invokeLater(() -> {
                GraphSelectionWindow graphWindow = new GraphSelectionWindow(graphType -> {
                    if ("line".equals(graphType)) {
                        LineGraphWeatherComparison lineGraph = new LineGraphWeatherComparison(graphName);
                        firstCityWeatherData.getTemperatureHistory(temperatureCategory).forEach((date, value) ->
                                lineGraph.addData("City 1 Temperature", date, value)
                        );
                        // If the number of cities is not 2, then ignore the second graph
                        if (numOfCities == 2) {
                            secondCityWeatherData.getTemperatureHistory(temperatureCategory).forEach((date, value) ->
                                    lineGraph.addData("City 2 Temperature", date, value)
                            );
                        }
                        lineGraph.display();
                    } else if ("bar".equals(graphType)) {
                        BarGraphWeatherComparison barGraph = new BarGraphWeatherComparison(graphName);
                        barGraph.addData("City 1", "Temperature", firstCityWeatherData.getAverageWeatherData("temperatureMeanDaily"));
                        barGraph.addData("City 1", "Precipitation", firstCityWeatherData.getAverageWeatherData("precipitationDaily"));
                        barGraph.addData("City 1", "Humidity", firstCityWeatherData.getAverageWeatherData("humidityHourly"));
                        barGraph.addData("City 1", "Wind Speed", firstCityWeatherData.getAverageWeatherData("windSpeedDaily"));
                        // If the number of cities is not 2, then ignore the second graph
                        if (numOfCities == 2) {
                            barGraph.addData("City 2", "Temperature", secondCityWeatherData.getAverageWeatherData("temperatureMeanDaily"));
                            barGraph.addData("City 2", "Precipitation", secondCityWeatherData.getAverageWeatherData("precipitationDaily"));
                            barGraph.addData("City 2", "Humidity", secondCityWeatherData.getAverageWeatherData("humidityHourly"));
                            barGraph.addData("City 2", "Wind Speed", secondCityWeatherData.getAverageWeatherData("windSpeedDaily"));
                        }
                        barGraph.display();
                    }
                });
                graphWindow.setVisible(true);
            });
        }
    }

    private void showError(String errorMessage) {
        JOptionPane.showMessageDialog(mainPanel, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
