package presentation.visualization;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * A class for creating and displaying a line graph to compare weather data.
 */
public class LineGraphWeatherComparison extends JFrame {
    private static final int CHART_WIDTH = 800;
    private static final int CHART_HEIGHT = 600;
    private final DefaultCategoryDataset dataset;

    /**
     * Constructor for the LineGraphWeatherComparison class.
     *
     * @param title The title of the line graph window. Must not be null.
     */
    public LineGraphWeatherComparison(String title) {
        super(title);
        dataset = new DefaultCategoryDataset();

        final JFreeChart lineChart = ChartFactory.createLineChart(
                title,
                "Date",
                "°C",
                dataset
        );

        // Create a ChartPanel to display the chart in the JFrame
        final ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
        setContentPane(chartPanel);
    }

    /**
     * Adds a data point to the line graph dataset.
     *
     * @param series   The series name (e.g., "City 1 Temperature"). Must not be null.
     * @param category The category or date (e.g., "2024-12-01"). Must not be null.
     * @param value    The numeric value to plot (e.g., temperature value).
     */
    public void addData(String series, String category, double value) {
        dataset.addValue(value, series, category);
    }

    /**
     * Displays the line graph in a window.
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
