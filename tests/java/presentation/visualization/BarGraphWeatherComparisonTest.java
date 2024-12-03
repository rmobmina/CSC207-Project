package presentation.visualization;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the BarGraphWeatherComparison class.
 */
public class BarGraphWeatherComparisonTest {

    private BarGraphWeatherComparison barGraph;

    /**
     * Sets up a new BarGraphWeatherComparison instance before each test.
     */
    @Before
    public void setUp() {
        barGraph = new BarGraphWeatherComparison("Test Bar Graph");
    }

    /**
     * Tests the addData method.
     */
    @Test
    public void testAddData() {
        // Arrange
        String series = "City A";
        String category = "2024-12-01";
        double value = 15.0;

        // Act
        barGraph.addData(series, category, value);

        // Assert
        DefaultCategoryDataset dataset = barGraph.getDataset();
        assertEquals(15.0, dataset.getValue(series, category).doubleValue(), 0.0001);
    }

    /**
     * Tests the reset method.
     */
    @Test
    public void testReset() {
        // Arrange
        barGraph.addData("City A", "2024-12-01", 15.0);

        // Act
        barGraph.reset();

        // Assert
        DefaultCategoryDataset dataset = barGraph.getDataset();
        assertTrue(dataset.getRowCount() == 0 && dataset.getColumnCount() == 0);
    }

    /**
     * Tests adding multiple data points.
     */
    @Test
    public void testMultipleDataEntries() {
        // Arrange & Act
        barGraph.addData("City A", "2024-12-01", 15.0);
        barGraph.addData("City B", "2024-12-01", 18.0);
        barGraph.addData("City A", "2024-12-02", 16.0);

        // Assert
        DefaultCategoryDataset dataset = barGraph.getDataset();
        assertEquals(15.0, dataset.getValue("City A", "2024-12-01").doubleValue(), 0.0001);
        assertEquals(18.0, dataset.getValue("City B", "2024-12-01").doubleValue(), 0.0001);
        assertEquals(16.0, dataset.getValue("City A", "2024-12-02").doubleValue(), 0.0001);
    }

    /**
     * Tests that the dataset is empty upon initialization.
     */
    @Test
    public void testEmptyGraph() {
        // Act
        DefaultCategoryDataset dataset = barGraph.getDataset();

        // Assert
        assertTrue(dataset.getRowCount() == 0 && dataset.getColumnCount() == 0);
    }
}
