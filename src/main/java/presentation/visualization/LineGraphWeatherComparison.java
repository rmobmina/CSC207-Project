package presentation.visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

/**
 * A class for creating a line graph to compare weather data.
 */
public class LineGraphWeatherComparison extends JFrame {
    private final DefaultCategoryDataset dataset;

    public LineGraphWeatherComparison(String title) {
        super(title);
        dataset = new DefaultCategoryDataset();
        JFreeChart lineChart = ChartFactory.createLineChart(
                title,
                "Date",
                "Value",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    /**
     * Adds a data point to the dataset.
     *
     * @param series   the series name
     * @param category the category name (e.g., date)
     * @param value    the value to add
     */
    public void addData(String series, String category, double value) {
        dataset.addValue(value, series, category);
    }

    /**
     * Resets (clears) the dataset.
     */
    public void reset() {
        dataset.clear();
    }

    /**
     * Returns the dataset for testing purposes.
     *
     * @return the dataset
     */
    public DefaultCategoryDataset getDataset() {
        return dataset;
    }
}
