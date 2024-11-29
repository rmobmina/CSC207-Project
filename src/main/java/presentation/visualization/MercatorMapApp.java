package presentation.visualization;

import utils.Constants;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.io.File;
import java.io.IOException;

/**
 * A class that displays a position on the world map relative to its longitude and latitude.
 * The location is displayed as a dot on a background.
 */
public class MercatorMapApp extends JFrame {

    private final Image mapImage;
    private final Point2D mercatorPoint;

    public MercatorMapApp(double latitude, double longitude) {
        // Path to the map image
        final String mapPath = new File("").getAbsolutePath() + "\\src\\main\\java\\presentation\\visualization"
                + "\\1207px-Mercator_projection_SW.0.png";

        // Load the map image
        try {
            mapImage = ImageIO.read(new File(mapPath));
            if (mapImage == null) {
                throw new IOException("Image could not be loaded.");
            }
        }

        catch (IOException exception) {
            throw new RuntimeException("Error loading map image: " + exception.getMessage(), exception);
        }

        // Calculate the Mercator coordinates
        final MercatorAlgorithm mercatorAlgorithm = new MercatorAlgorithm(latitude, longitude);
        final double[] coordinates = mercatorAlgorithm.normalizeCoordinates(
                mapImage.getHeight(null), mapImage.getWidth(null)
        );

        // Store the calculated point
        this.mercatorPoint = new Point2D.Double(coordinates[0], coordinates[1]);
    }

    /**
     * A method to display a Mercator Map with points representing cities drawn on said map.
     */
    public void displayMercatorMap() {
        // Set up the JFrame
        setTitle("Mercator Map");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(mapImage.getWidth(null), mapImage.getHeight(null));
        setLocationRelativeTo(null);

        // Add the custom panel
        add(new MapPanel(mapImage, mercatorPoint));
        setVisible(true);
    }

    /**
     * A custom panel to draw the map image and overlay the dot.
     */
    static class MapPanel extends JPanel {
        private final Image mapImage;
        private final Point2D point;

        MapPanel(Image mapImage, Point2D point) {
            this.mapImage = mapImage;
            this.point = point;
        }

        protected void paintComponent(Graphics graphicsObject) {
            super.paintComponent(graphicsObject);

            // Draw the map image
            graphicsObject.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);

            // Draw the dot
            graphicsObject.setColor(Color.RED);
            graphicsObject.fillOval(
                    (int) Math.round(point.getX() - Constants.MERCATOR_POINT_RADIUS / 2.0),
                    (int) Math.round(point.getY() - Constants.MERCATOR_POINT_RADIUS / 2.0),
                    Constants.MERCATOR_POINT_RADIUS,
                    Constants.MERCATOR_POINT_RADIUS);
        }
    }

    // Main method to test the application
    public static void main(String[] args) {
        // Coordinates for Toronto
        final MercatorMapApp app = new MercatorMapApp(43.6532, -79.3832);
        app.displayMercatorMap();
    }
}
