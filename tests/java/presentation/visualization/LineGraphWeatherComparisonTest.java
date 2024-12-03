package presentation.visualization;

import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LineGraphWeatherComparison.
 */
public class LineGraphWeatherComparisonTest {

    private LineGraphWeatherComparison lineGraph;

    @BeforeEach
    void setUp() {
        lineGraph = new LineGraphWeatherComparison("Line Graph Test");
    }

    @Test
    void testAddData() throws NoSuchFieldException, IllegalAccessException {
        // Add some test data
        lineGraph.addData("Series1", "2024-12-01", 15.0);
        lineGraph.addData("Series1", "2024-12-02", 18.5);

        // Access the private dataset field using reflection
        Field datasetField = LineGraphWeatherComparison.class.getDeclaredField("dataset");
        datasetField.setAccessible(true);
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) datasetField.get(lineGraph);

        // Check that the data was added correctly
        assertEquals(15.0, dataset.getValue("Series1", "2024-12-01"));
        assertEquals(18.5, dataset.getValue("Series1", "2024-12-02"));
    }

    @Test
    void testReset() throws NoSuchFieldException, IllegalAccessException {
        // Add some test data and then reset the graph
        lineGraph.addData("Series1", "2024-12-01", 15.0);
        lineGraph.reset();

        // Access the private dataset field using reflection
        Field datasetField = LineGraphWeatherComparison.class.getDeclaredField("dataset");
        datasetField.setAccessible(true);
        DefaultCategoryDataset dataset = (DefaultCategoryDataset) datasetField.get(lineGraph);

        // Check that the dataset is empty after reset
        assertTrue(dataset.getRowCount() == 0 && dataset.getColumnCount() == 0);
    }

    @Test
    void testUIComponents() {
        SwingUtilities.invokeLater(() -> {
            lineGraph.display();
            // Check if the JFrame is visible
            assertTrue(lineGraph.isVisible());

            // Check if the JFrame title matches the given title
            assertEquals("Line Graph Test", lineGraph.getTitle());

            // Close the JFrame after the test
            lineGraph.dispose();
        });
    }
}
