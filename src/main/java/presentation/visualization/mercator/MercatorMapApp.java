package presentation.visualization.mercator;

import javax.swing.JFrame;

import application.usecases.ConvertToMercatorUseCase;
import domain.entities.Coordinate;

/**
 * A class to display a coordinate on a Mercator projection map.
 */
public class MercatorMapApp extends JFrame {
    private final SwingMapRenderer renderer;

    /**
     * Constructor to initialize the MercatorMapApp with a SwingMapRenderer.
     *
     * @param renderer The renderer responsible for drawing the map and coordinates.
     */
    public MercatorMapApp(SwingMapRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Displays the given coordinate on the Mercator map by converting the coordinate
     * to Mercator projection and passing it to the renderer.
     *
     * @param coordinate The geographical coordinate (latitude and longitude) to be displayed.
     * @param mapWidth The width of the map.
     * @param mapHeight The height of the map.
     */
    public void displayCoordinate(Coordinate coordinate, double mapWidth, double mapHeight) {
        final ConvertToMercatorUseCase useCase = new ConvertToMercatorUseCase();
        final double[] mercatorCoordinates = useCase.execute(coordinate, mapWidth, mapHeight);

        renderer.renderMap(mercatorCoordinates[0], mercatorCoordinates[1]);
    }
}
