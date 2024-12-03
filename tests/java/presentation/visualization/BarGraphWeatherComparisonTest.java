//package presentation.visualization;
//
//import org.jfree.data.category.DefaultCategoryDataset;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.swing.*;
//import java.lang.reflect.Field;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Test class for BarGraphWeatherComparison.
// */
//public class BarGraphWeatherComparisonTest {
//
//    private BarGraphWeatherComparison barGraph;
//
//    @BeforeEach
//    void setUp() {
//        barGraph = new BarGraphWeatherComparison("Bar Graph Test");
//    }
//
//    @Test
//    void testAddData() throws NoSuchFieldException, IllegalAccessException {
//        // Add some test data
//        barGraph.addData("Series1", "Category1", 25.5);
//        barGraph.addData("Series1", "Category2", 30.0);
//
//        // Access the private dataset field using reflection
//        Field datasetField = BarGraphWeatherComparison.class.getDeclaredField("dataset");
//        datasetField.setAccessible(true);
//        DefaultCategoryDataset dataset = (DefaultCategoryDataset) datasetField.get(barGraph);
//
//        // Check that the data was added correctly
//        assertEquals(25.5, dataset.getValue("Series1", "Category1"));
//        assertEquals(30.0, dataset.getValue("Series1", "Category2"));
//    }
//
//    @Test
//    void testReset() throws NoSuchFieldException, IllegalAccessException {
//        // Add some test data and then reset the graph
//        barGraph.addData("Series1", "Category1", 25.5);
//        barGraph.reset();
//
//        // Access the private dataset field using reflection
//        Field datasetField = BarGraphWeatherComparison.class.getDeclaredField("dataset");
//        datasetField.setAccessible(true);
//        DefaultCategoryDataset dataset = (DefaultCategoryDataset) datasetField.get(barGraph);
//
//        // Check that the dataset is empty after reset
//        assertTrue(dataset.getRowCount() == 0 && dataset.getColumnCount() == 0);
//    }
//
//    @Test
//    void testUIComponents() {
//        SwingUtilities.invokeLater(() -> {
//            barGraph.display();
//            // Check if the JFrame is visible
//            assertTrue(barGraph.isVisible());
//
//            // Check if the JFrame title matches the given title
//            assertEquals("Bar Graph Test", barGraph.getTitle());
//
//            // Close the JFrame after the test
//            barGraph.dispose();
//        });
//    }
//}
