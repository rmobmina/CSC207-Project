package presentation.visualization;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * A class for creating and displaying a bar graph to compare weather data.
 */
public class BarGraphWeatherComparison extends JFrame {
    private static final int CHART_WIDTH = 800;
    private static final int CHART_HEIGHT = 600;
    private final DefaultCategoryDataset dataset;

    /**
     * Constructor for the BarGraphWeatherComparison class.
     *
     * @param title The title of the bar graph window. Must not be null.
     */
    public BarGraphWeatherComparison(String title) {
        super(title);
        dataset = new DefaultCategoryDataset();

        // Create the bar chart using JFreeChart
        final JFreeChart barChart = ChartFactory.createBarChart(
                title,
                "Category",
                "Value",
                dataset
        );

        // Create a ChartPanel to display the chart in the JFrame
        final ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
        setContentPane(chartPanel);
    }

    /**
     * Adds a data point to the bar graph dataset.
     *
     * @param series   The series name (e.g., "City 1"). Must not be null.
     * @param category The category or label (e.g., "Temperature"). Must not be null.
     * @param value    The numeric value to display (e.g., temperature value).
     */
    public void addData(String series, String category, double value) {
        dataset.addValue(value, series, category);
    }

    /**
     * Displays the bar graph in a window.
     */
    public void display() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Resets the graph by clearing all data from the dataset.
     */
    public void reset() {
        dataset.clear();
    }
}
