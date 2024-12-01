package presentation.visualization;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BarGraphWeatherComparisonTest {
    private BarGraphWeatherComparison barGraph;

    @BeforeEach
    void setUp() {
        barGraph = new BarGraphWeatherComparison("Bar Graph Test");
    }

    @Test
    void addData_validInputs_updatesDataset() {
        // Arrange
        String series = "Temperature";
        String category = "City A";
        double value = 25.5;

        // Act
        barGraph.addData(series, category, value);
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) barGraph.getDataset();

        // Assert
        assertEquals(1, dataset.getRowCount());
        assertEquals(1, dataset.getColumnCount());
        assertEquals(value, dataset.getValue(series, category));
    }

    @Test
    void reset_clearsDataset() {
        // Arrange
        barGraph.addData("Series1", "Category1", 10);

        // Act
        barGraph.reset();
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) barGraph.getDataset();

        // Assert
        assertEquals(0, dataset.getRowCount());
        assertEquals(0, dataset.getColumnCount());
    }
}
