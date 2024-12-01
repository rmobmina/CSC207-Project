package presentation.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import presentation.interfaces.MapRenderer;
import utils.Constants;

/**
 * SwingMapRenderer is responsible for rendering a map with a marker at specified coordinates.
 */
public class SwingMapRenderer extends JPanel implements MapRenderer {

    private final Image mapImage;
    private double xCoordinate = -1;
    private double yCoordinate = -1;

    /**
     * Constructor to initialize the map renderer with a given image.
     *
     * @param mapImage The image of the map to be displayed.
     */
    public SwingMapRenderer(Image mapImage) {
        this.mapImage = mapImage;
    }

    /**
     * Renders the map and a marker at the specified coordinates.
     *
     * @param x The x-coordinate to place the marker on the map.
     * @param y The y-coordinate to place the marker on the map.
     */
    @Override
    public void renderMap(double x, double y) {
        this.xCoordinate = x;
        this.yCoordinate = y;
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
        if (xCoordinate >= 0 && yCoordinate >= 0) {
            g.setColor(Color.RED);
            g.fillOval(
                        (int) (xCoordinate - Constants.MERCATOR_POINT_RADIUS / 2.0),
                    (int) (yCoordinate - Constants.MERCATOR_POINT_RADIUS / 2.0),
                    Constants.MERCATOR_POINT_RADIUS,
                    Constants.MERCATOR_POINT_RADIUS);
            System.out.println("Marker drawn at: (" + xCoordinate + ", " + yCoordinate + ")");
        }

        else {
            System.out.println("No marker to render.");
        }
    }
}
