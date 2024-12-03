package presentation.visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class BarGraphWeatherComparison extends JFrame {
    private final DefaultCategoryDataset dataset;

    public BarGraphWeatherComparison(String title) {
        super(title);
        dataset = new DefaultCategoryDataset();
        JFreeChart barChart = ChartFactory.createBarChart(
                title,
                "Category",    // X-axis Label
                "Value",       // Y-axis Label
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    public void addData(String series, String category, double value) {
        dataset.addValue(value, series, category); // Add data to the dataset
    }

    public void display() {
        pack(); // Adjust window size to fit content
        setLocationRelativeTo(null); // Center the window
        setVisible(true); // Make the window visible
    }

    public void reset() {
        dataset.clear(); // Clear the dataset to reset the graph
    }
}