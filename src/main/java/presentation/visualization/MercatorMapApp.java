package presentation.visualization;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
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

        catch (IOException e) {
            throw new RuntimeException("Error loading map image: " + e.getMessage(), e);
        }

        // Calculate the Mercator coordinates
        MercatorAlgorithm mercatorAlgorithm = new MercatorAlgorithm(latitude, longitude);
        double[] coordinates = mercatorAlgorithm.normalizeCoordinates(
                mapImage.getHeight(null), mapImage.getWidth(null)
        );

        // Store the calculated point
        this.mercatorPoint = new Point2D.Double(coordinates[0], coordinates[1]);
    }

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

        public MapPanel(Image mapImage, Point2D point) {
            this.mapImage = mapImage;
            this.point = point;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the map image
            g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);

            // Draw the dot
            g.setColor(Color.RED);
            System.out.println(point.getY());
            g.fillOval((int) (point.getX()) - 5, (int) (point.getY()) - 5, 10, 10);
        }
    }

    // Main method to test the application
    public static void main(String[] args) {
        // Coordinates for Toronto
        MercatorMapApp app = new MercatorMapApp(52.1579, -106.6702);
        app.displayMercatorMap();
    }
}
