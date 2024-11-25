package presentation.visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class BarGraphVisualization extends JFrame {
    private final DefaultCategoryDataset dataset;

    public BarGraphVisualization(String title) {
        super(title);
        dataset = new DefaultCategoryDataset();
        JFreeChart barChart = ChartFactory.createBarChart(
                "Weather Comparison",
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
}
