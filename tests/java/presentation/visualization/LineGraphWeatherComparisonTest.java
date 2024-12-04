//package presentation.visualization;
//
//import org.jfree.data.category.DefaultCategoryDataset;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
///**
// * Unit tests for the LineGraphWeatherComparison class.
// */
//public class LineGraphWeatherComparisonTest {
//
//    private LineGraphWeatherComparison lineGraph;
//
//    /**
//     * Sets up a new LineGraphWeatherComparison instance before each test.
//     */
//    @Before
//    public void setUp() {
//        lineGraph = new LineGraphWeatherComparison("Test Line Graph");
//    }
//
//    /**
//     * Tests the addData method.
//     */
//    @Test
//    public void testAddData() {
//        // Arrange
//        String series = "City A";
//        String category = "2024-12-01";
//        double value = 15.0;
//
//        // Act
//        lineGraph.addData(series, category, value);
//
//        // Assert
//        DefaultCategoryDataset dataset = lineGraph.getDataset();
//        assertEquals(15.0, dataset.getValue(series, category).doubleValue(), 0.0001);
//    }
//
//    /**
//     * Tests the reset method.
//     */
//    @Test
//    public void testReset() {
//        // Arrange
//        lineGraph.addData("City A", "2024-12-01", 15.0);
//
//        // Act
//        lineGraph.reset();
//
//        // Assert
//        DefaultCategoryDataset dataset = lineGraph.getDataset();
//        assertTrue(dataset.getRowCount() == 0 && dataset.getColumnCount() == 0);
//    }
//
//    /**
//     * Tests adding multiple data points.
//     */
//    @Test
//    public void testMultipleDataEntries() {
//        // Arrange & Act
//        lineGraph.addData("City A", "2024-12-01", 15.0);
//        lineGraph.addData("City B", "2024-12-01", 18.0);
//        lineGraph.addData("City A", "2024-12-02", 16.0);
//
//        // Assert
//        DefaultCategoryDataset dataset = lineGraph.getDataset();
//        assertEquals(15.0, dataset.getValue("City A", "2024-12-01").doubleValue(), 0.0001);
//        assertEquals(18.0, dataset.getValue("City B", "2024-12-01").doubleValue(), 0.0001);
//        assertEquals(16.0, dataset.getValue("City A", "2024-12-02").doubleValue(), 0.0001);
//    }
//
//    /**
//     * Tests that the dataset is empty upon initialization.
//     */
//    @Test
//    public void testEmptyGraph() {
//        // Act
//        DefaultCategoryDataset dataset = lineGraph.getDataset();
//
//        // Assert
//        assertTrue(dataset.getRowCount() == 0 && dataset.getColumnCount() == 0);
//    }
//}
