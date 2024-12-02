package infrastructure.frameworks;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import application.usecases.LoadMapImageUseCase;
import domain.entities.Coordinate;
import presentation.visualization.MercatorMapApp;
import presentation.visualization.SwingMapRenderer;
import utils.Constants;

/**
 * Entry point for the Mercator Display Application.
 * This application demonstrates displaying a Mercator map and handling user interactions.
 */
public class MercatorDisplayApp {

    /**
     * Main method to start the Mercator Display application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        try {
            // Dependency: LoadMapImageUseCase
            final LoadMapImageUseCase loadMapImageUseCase = new LoadMapImageUseCase();

            // Load the map image
            final String mapFilePath = new File("").getAbsolutePath() + Constants.MERCATOR_IMAGE_PATH;
            System.out.println(mapFilePath);

            // Combine the paths to get the full path
            File file = new File(mapFilePath);

            // Check if the file exists
            if (file.exists()) {
                System.out.println("File found: " + file.getAbsolutePath());
            } else {
                System.out.println("File not found " + file.getAbsolutePath());
            }

            final Image mapImage = loadMapImageUseCase.execute(mapFilePath);
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

            // Example: Display a specific coordinate
            final Coordinate coordinate = new Coordinate(43.6532, -79.3832);
            app.displayCoordinate(coordinate, mapImage.getWidth(null), mapImage.getHeight(null));

        } catch (IOException exception) {
            System.err.println("Failed to load the map image: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
