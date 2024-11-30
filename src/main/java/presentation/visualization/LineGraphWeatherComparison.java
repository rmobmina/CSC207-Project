//package presentation.visualization;
//
//import application.dto.WeatherDataDTO;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.axis.NumberAxis;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.chart.plot.XYPlot;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class LineGraphWeatherComparison extends JFrame {
//
//    public LineGraphWeatherComparison(String city1, WeatherDataDTO data1, String city2, WeatherDataDTO data2) {
//        setTitle("Line Graph: Weather Comparison");
//        setSize(800, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        XYSeriesCollection dataset = createDataset(city1, data1, city2, data2);
//        JFreeChart chart = createChart(dataset);
//
//        ChartPanel chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize(new Dimension(800, 600));
//        setContentPane(chartPanel);
//    }
//
//    private XYSeriesCollection createDataset(String city1, WeatherDataDTO data1, String city2, WeatherDataDTO data2) {
//        XYSeries city1Series = new XYSeries(city1);
//        XYSeries city2Series = new XYSeries(city2);
//
//        // Populate data series (e.g., using day index as x-axis, temperature as y-axis)
//        for (int i = 0; i < data1.temperature.size(); i++) {
//            city1Series.add(i + 1, data1.temperature.get(i));
//            city2Series.add(i + 1, data2.temperature.get(i));
//        }
//
//        XYSeriesCollection dataset = new XYSeriesCollection();
//        dataset.addSeries(city1Series);
//        dataset.addSeries(city2Series);
//
//        return dataset;
//    }
//
//    private JFreeChart createChart(XYSeriesCollection dataset) {
//        JFreeChart chart = ChartFactory.createXYLineChart(
//                "Temperature Comparison",
//                "Days",
//                "Temperature (°C)",
//                dataset,
//                PlotOrientation.VERTICAL,
//                true,
//                true,
//                false
//        );
//
//        XYPlot plot = chart.getXYPlot();
//        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
//        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//
//        return chart;
//    }
//
//    public static void main(String[] args) {
//        // random data for now
//        WeatherDataDTO city1Data = new WeatherDataDTO();
//        city1Data.temperature = List.of(10.0, 12.5, 15.0, 11.0, 14.0);
//
//        WeatherDataDTO city2Data = new WeatherDataDTO();
//        city2Data.temperature = List.of(9.0, 11.5, 16.0, 13.0, 15.0);
//
//        SwingUtilities.invokeLater(() -> {
//            LineGraphWeatherComparison graph = new LineGraphWeatherComparison("City1", city1Data, "City2", city2Data);
//            graph.setVisible(true);
//        });
//    }
//}
