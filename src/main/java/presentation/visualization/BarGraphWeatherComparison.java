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
                "Category",
                "Value",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    public void addData(String series, String category, double value) {
        dataset.addValue(value, series, category);
    }

    public void display() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

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