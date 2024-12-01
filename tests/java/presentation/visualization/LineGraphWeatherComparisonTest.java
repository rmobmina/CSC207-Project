package presentation.visualization;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineGraphWeatherComparisonTest {
    private LineGraphWeatherComparison lineGraph;

    @BeforeEach
    void setUp() {
        lineGraph = new LineGraphWeatherComparison("Line Graph Test");
    }

    @Test
    void addData_validInputs_updatesDataset() {
        // Arrange
        String series = "Temperature";
        String category = "2023-12-01";
        double value = 15.2;

        // Act
        lineGraph.addData(series, category, value);
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) lineGraph.getDataset();

        // Assert
        assertEquals(1, dataset.getRowCount());
        assertEquals(1, dataset.getColumnCount());
        assertEquals(value, dataset.getValue(series, category));
    }

    @Test
    void reset_clearsDataset() {
        // Arrange
        lineGraph.addData("Series1", "Category1", 20);

        // Act
        lineGraph.reset();
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) lineGraph.getDataset();

        // Assert
        assertEquals(0, dataset.getRowCount());
        assertEquals(0, dataset.getColumnCount());
    }
}
