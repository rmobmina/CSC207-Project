package presentation.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import presentation.interfaces.MapRenderer;
import utils.Constants;

/**
 * SwingMapRenderer is responsible for rendering a map with a marker at specified coordinates.
 */
public class SwingMapRenderer extends JPanel implements MapRenderer {

    private final Image mapImage;
    private double originalX = -1;
    private double originalY = -1;
    private double currentX = -1;
    private double currentY = -1;
    private final String comma = ", ";
    private final String closeBracket = ")";

    /**
     * Constructor to initialize the map renderer with a given image.
     *
     * @param mapImage The image of the map to be displayed.
     */
    public SwingMapRenderer(Image mapImage) {
        this.mapImage = mapImage;

        // Add a listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateScaledCoordinates();
                repaint();
            }
        });

        // Add a listener to handle mouse clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMapClick(e.getX(), e.getY());
            }
        });
    }

    /**
     * Renders the map and a marker at the specified coordinates.
     *
     * @param x The original x-coordinate to place the marker on the map.
     * @param y The original y-coordinate to place the marker on the map.
     */
    @Override
    public void renderMap(double x, double y) {
        this.originalX = x;
        this.originalY = y;
        updateScaledCoordinates();
        repaint();
    }

    /**
     * Updates the scaled coordinates of the marker based on the panel's current size.
     */
    private void updateScaledCoordinates() {
        if (originalX >= 0 && originalY >= 0) {
            final double widthRatio = (double) getWidth() / mapImage.getWidth(null);
            final double heightRatio = (double) getHeight() / mapImage.getHeight(null);

            this.currentX = originalX * widthRatio;
            this.currentY = originalY * heightRatio;

            System.out.println("Updated Scaled Coordinates: (" + currentX + comma + currentY + closeBracket);
        }
    }

    /**
     * Handles clicks on the map, converting the clicked position into latitude and longitude.
     *
     * @param mouseX The x-coordinate of the mouse click.
     * @param mouseY The y-coordinate of the mouse click.
     */
    private void handleMapClick(int mouseX, int mouseY) {
        // Convert from screen coordinates to original Mercator coordinates
        final double widthRatio = (double) getWidth() / mapImage.getWidth(null);
        final double heightRatio = (double) getHeight() / mapImage.getHeight(null);

        this.originalX = mouseX / widthRatio;
        this.originalY = mouseY / heightRatio;

        System.out.println("Screen Coordinates Clicked: (" + mouseX + comma + mouseY + closeBracket);
        System.out.println("Scaled Mercator Coordinates: (" + originalX + comma + originalY + closeBracket);

        // Convert Mercator coordinates to latitude/longitude
        final double mapWidth = mapImage.getWidth(null);
        final double mapHeight = mapImage.getHeight(null);

        final double[] latLon = MercatorAlgorithm.reverseCoordinates(originalX, originalY, mapWidth, mapHeight);
        if (latLon != null && latLon.length == 2) {
            final double latitude = latLon[1];
            final double longitude = latLon[0];
            System.out.println("Latitude/Longitude: (" + latitude + comma + longitude + closeBracket);
        }

        else {
            System.err.println("Error: Unable to convert to latitude/longitude.");
        }

        // Update and repaint
        updateScaledCoordinates();
        repaint();
    }

    /**
     * Paints the component by drawing the map and the marker if coordinates are set.
     *
     * @param g The Graphics object used to draw on the panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the map
        g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);

        // Draw the marker if coordinates are set
        if (currentX >= 0 && currentY >= 0) {
            g.setColor(Color.RED);
            g.fillOval(
                    (int) (currentX - Constants.MERCATOR_POINT_RADIUS / 2.0),
                    (int) (currentY - Constants.MERCATOR_POINT_RADIUS / 2.0),
                    Constants.MERCATOR_POINT_RADIUS,
                    Constants.MERCATOR_POINT_RADIUS
            );
        }
    }
}
