package presentation.ui.views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import application.dto.WeatherDataDTO;
import application.dto.WeatherDataDTOGenerator;
import application.usecases.GetForecastWeatherDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.services.MercatorAlgorithm;
import infrastructure.adapters.OpenWeatherApiService;
import utils.Constants;

/**
 * A view that extends JPanel.
 * Used to draw a Mercator Projection Map.
 *      Comes with methods to draw a point on the map and retrieve weather details in a seperate window,
 *       either on user click or on user selection.
 */
public class SwingMapView extends JPanel {

    private final Image mapImage;
    private double originalX = -1;
    private double originalY = -1;
    private double currentX = -1;
    private double currentY = -1;
    private final GetForecastWeatherDataUseCase getWeatherUseCase;
    private JFrame weatherFrame;
    private JLabel weatherLabel;

    /**
     * Constructor to initialize the map renderer with a given image and weather API service.
     *
     * @param mapImage The image of the map to be displayed.
     */
    public SwingMapView(Image mapImage) {
        this.mapImage = mapImage;
        this.getWeatherUseCase = new GetForecastWeatherDataUseCase(new OpenWeatherApiService());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateScaledCoordinates();
                repaint();
            }
        });

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
     * @param xCoordinate The x-coordinate to place the marker on the map.
     * @param yCoordinate The y-coordinate to place the marker on the map.
     */
    public void renderMap(double xCoordinate, double yCoordinate) {
        this.originalX = xCoordinate;
        this.originalY = yCoordinate;
        updateScaledCoordinates();
        repaint();
        fetchWeatherData(originalX, originalY);
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

            final double mapWidth = mapImage.getWidth(null);
            final double mapHeight = mapImage.getHeight(null);

            final double[] latLon = MercatorAlgorithm.reverseCoordinates(originalX, originalY, mapWidth, mapHeight);
            if (latLon != null && latLon.length == 2) {
                fetchWeatherData(latLon[1], latLon[0]);
            }
        }
    }

    /**
     * Handles clicks on the map, converting the clicked position into latitude and longitude.
     *
     * @param mouseX The x-coordinate of the mouse click.
     * @param mouseY The y-coordinate of the mouse click.
     */
    private void handleMapClick(int mouseX, int mouseY) {
        final double widthRatio = (double) getWidth() / mapImage.getWidth(null);
        final double heightRatio = (double) getHeight() / mapImage.getHeight(null);

        this.originalX = mouseX / widthRatio;
        this.originalY = mouseY / heightRatio;

        final double mapWidth = mapImage.getWidth(null);
        final double mapHeight = mapImage.getHeight(null);

        final double[] latLon = MercatorAlgorithm.reverseCoordinates(originalX, originalY, mapWidth, mapHeight);
        if (latLon != null && latLon.length == 2) {
            fetchWeatherData(latLon[1], latLon[0]);
        }

        updateScaledCoordinates();
        repaint();
    }

    /**
     * Fetch weather data using the GetWeatherUseCase.
     *
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     */
    private void fetchWeatherData(double latitude, double longitude) {
        final Location currentLocation = new Location("", "", "", latitude, longitude);
        final WeatherData weatherData = getWeatherUseCase.execute(currentLocation, 1);
        final WeatherDataDTO weatherDataDto = WeatherDataDTOGenerator.createWeatherDataDTO(
                weatherData, currentLocation, LocalDate.now(), LocalDate.now());

        if (weatherFrame == null) {
            createWeatherFrame();
        }

        if (weatherLabel != null) {
            final DayPanel dayPanel = new DayPanel(LocalDate.now(), true);
            dayPanel.updateWeatherDataValues(weatherDataDto, 0, Constants.CELCIUS_UNIT_TYPE);
            weatherFrame.add(dayPanel);
            weatherFrame.setVisible(true);
        }
    }

    /**
     * Creates the weather information frame.
     */
    private void createWeatherFrame() {
        weatherFrame = new JFrame("Weather Information");
        weatherFrame.setSize(Constants.weatherFrameWidth, Constants.weatherFrameHeight);
        weatherFrame.setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()
                - Constants.weatherFrameWidth, 0);
        weatherFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        weatherLabel = new JLabel("Weather details will be shown here.");
    }

    /**
     * Disposes of the weather information frame if it is open.
     */
    public void disposeWeatherFrame() {
        if (weatherFrame != null) {
            weatherFrame.dispose();
            weatherFrame = null;
        }
    }

    /**
     * Paints the component by drawing the map and the marker if coordinates are set.
     *
     * @param g The Graphics object used to draw on the panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);

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
