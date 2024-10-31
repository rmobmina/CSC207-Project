package domain.services;

import domain.entities.Visualization;
import domain.entities.WeatherData;

import java.util.List;

public interface VisualizationService {
    Visualization generateGraph(String graphType, List<WeatherData> dataPoints, List<WeatherData> comparisonData);
}
