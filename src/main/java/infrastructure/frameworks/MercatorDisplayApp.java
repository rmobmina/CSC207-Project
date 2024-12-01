package infrastructure.frameworks;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import domain.entities.Coordinate;
import presentation.visualization.MercatorMapApp;
import presentation.visualization.SwingMapRenderer;

/**
 * MercatorDisplayApp entry point for the application. Sets up and displays the map with coordinates.
 */
public class MercatorDisplayApp {

    /**
     * MercatorDisplayApp method to start the application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            // Load the map image
            final String mapPath = new File("").getAbsolutePath() + "\\src\\main\\java\\presentation\\visualization"
                    + "\\1207px-Mercator_projection_SW.0.png";
            final Image mapImage = ImageIO.read(new File(mapPath));

            if (mapImage == null) {
                throw new RuntimeException("Map image could not be loaded. Check the file path.");
            }

            // Create the renderer
            final SwingMapRenderer renderer = new SwingMapRenderer(mapImage);

            // Set up the application
            final MercatorMapApp app = new MercatorMapApp(renderer);
            app.setSize(mapImage.getWidth(null), mapImage.getHeight(null));
            app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            app.add(renderer);
            app.setVisible(true);

            // Display a coordinate
            final Coordinate coordinate = new Coordinate(43.6532, -79.3832);
            app.displayCoordinate(coordinate, mapImage.getWidth(null), mapImage.getHeight(null));

        }

        catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Failed to load the map image: " + exception.getMessage());
        }
    }
}
