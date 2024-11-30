//package presentation.visualization;
//
//import application.dto.WeatherDataDTO;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.data.category.DefaultCategoryDataset;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class BarGraphWeatherComparison extends JFrame {
//
//    public BarGraphWeatherComparison(String city1, WeatherDataDTO data1, String city2, WeatherDataDTO data2) {
//        setTitle("Bar Graph: Weather Comparison");
//        setSize(800, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        DefaultCategoryDataset dataset = createDataset(city1, data1, city2, data2);
//        JFreeChart chart = createChart(dataset);
//
//        ChartPanel chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize(new Dimension(800, 600));
//        setContentPane(chartPanel);
//    }
//
//    private DefaultCategoryDataset createDataset(String city1, WeatherDataDTO data1, String city2, WeatherDataDTO data2) {
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//
//        double avgTempCity1 = data1.temperature.stream().mapToDouble(Double::doubleValue).average().orElse(0);
//        double avgTempCity2 = data2.temperature.stream().mapToDouble(Double::doubleValue).average().orElse(0);
//
//        double avgHumidityCity1 = data1.humidity.stream().mapToDouble(Double::doubleValue).average().orElse(0);
//        double avgHumidityCity2 = data2.humidity.stream().mapToDouble(Double::doubleValue).average().orElse(0);
//
//        dataset.addValue(avgTempCity1, city1, "Average Temperature");
//        dataset.addValue(avgTempCity2, city2, "Average Temperature");
//        dataset.addValue(avgHumidityCity1, city1, "Average Humidity");
//        dataset.addValue(avgHumidityCity2, city2, "Average Humidity");
//
//        return dataset;
//    }
//
//    private JFreeChart createChart(DefaultCategoryDataset dataset) {
//        return ChartFactory.createBarChart(
//                "Weather Comparison",
//                "Metric",
//                "Value",
//                dataset,
//                PlotOrientation.VERTICAL,
//                true,
//                true,
//                false
//        );
//    }
//
//    public static void main(String[] args) {
//        // data for now
//        WeatherDataDTO city1Data = new WeatherDataDTO();
//        city1Data.temperature = List.of(10.0, 12.5, 15.0, 11.0, 14.0);
//        city1Data.humidity = List.of(50.0, 55.0, 60.0, 58.0, 53.0);
//
//        WeatherDataDTO city2Data = new WeatherDataDTO();
//        city2Data.temperature = List.of(9.0, 11.5, 16.0, 13.0, 15.0);
//        city2Data.humidity = List.of(48.0, 54.0, 62.0, 59.0, 55.0);
//
//        SwingUtilities.invokeLater(() -> {
//            BarGraphWeatherComparison graph = new BarGraphWeatherComparison("City1", city1Data, "City2", city2Data);
//            graph.setVisible(true);
//        });
//    }
//}
