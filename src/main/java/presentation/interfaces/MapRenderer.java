package presentation.interfaces;

/**
 * An interface for rendering a map with a marker at a specified location.
 */
public interface MapRenderer {

    /**
     * Renders the map with a marker at the specified coordinates.
     *
     * @param xCoordinate The x-coordinate for the marker location on the map.
     * @param yCoordinate The y-coordinate for the marker location on the map.
     */
    void renderMap(double xCoordinate, double yCoordinate);
}
