
package presentation.visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class LineGraphWeatherComparison extends JFrame {
    private final DefaultCategoryDataset dataset;

    /**
     * Constructor for the LineGraphWeatherComparison class.
     *
     * @param title The title of the line graph window.
     */
    public LineGraphWeatherComparison(String title) {
        super(title);
        dataset = new DefaultCategoryDataset();

        // Create the line chart using JFreeChart
        JFreeChart lineChart = ChartFactory.createLineChart(
                title,
                "Date",        // X-axis Label
                "°C",       // Y-axis Label
                dataset        // Dataset for the chart
        );

        // Create a ChartPanel to display the chart in the JFrame
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    /**
     * Adds a data point to the line graph dataset.
     *
     * @param series The series name (e.g., "City 1 Temperature").
     * @param category The category or date (e.g., "2024-12-01").
     * @param value The numeric value to plot (e.g., temperature value).
     */
    public void addData(String series, String category, double value) {
        dataset.addValue(value, series, category); // Add data to the dataset
    }

    /**
     * Displays the line graph in a window.
     */
    public void display() {
        pack(); // Adjust window size to fit content
        setLocationRelativeTo(null); // Center the window
        setVisible(true); // Make the window visible
    }

    /**
     * Resets the graph by clearing all data from the dataset.
     */
    public void reset() {
        dataset.clear(); // Clear the dataset to reset the graph
    }
}
