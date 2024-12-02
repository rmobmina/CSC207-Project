package infrastructure.frameworks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import application.usecases.GetLocationDataUseCase;
import application.usecases.LoadMapImageUseCase;
import domain.entities.Location;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.DropDownUI;
import presentation.ui.views.SwingMapView;
import presentation.visualization.mercator.MercatorAlgorithm;
import presentation.visualization.mercator.MercatorMapApp;
import utils.Constants;

/**
 * Entry point for the Mercator Display Application.
 * This application demonstrates dynamically loading and displaying a Mercator map based on user location input.
 */
public class MercatorDisplayApp {

    /**
     * Launches the Mercator Map with user input for location.
     *
     * @param apiKey               The API key for OpenWeatherMap.
     * @param locationDataUseCase  Use case for location-related functionality.
     * @param apiService           Service to fetch weather data.
     */
    public void startMercatorMap(String apiKey, GetLocationDataUseCase locationDataUseCase,
                                 OpenWeatherApiService apiService) {
        try {
            final LoadMapImageUseCase loadMapImageUseCase = new LoadMapImageUseCase();

            final String mapFilePath = new File("").getAbsolutePath() + Constants.MERCATOR_IMAGE_PATH;
            final File file = new File(mapFilePath);

            if (!file.exists()) {
                throw new RuntimeException("Map image file not found at: " + mapFilePath);
            }

            final Image initialMapImage = loadMapImageUseCase.execute(mapFilePath);
            if (initialMapImage == null) {
                throw new RuntimeException("Map image could not be loaded. Check the file path.");
            }

            final SwingMapView renderer = new SwingMapView(initialMapImage);
            final MercatorMapApp app = new MercatorMapApp(renderer);

            setupApplicationWindow(app, initialMapImage, renderer, locationDataUseCase, apiKey);

        }

        catch (IOException ex) {
            System.err.println("Failed to load the map image: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void setupApplicationWindow(MercatorMapApp app, Image initialMapImage, SwingMapView renderer,
                                        GetLocationDataUseCase locationDataUseCase, String apiKey) {
        app.setSize(initialMapImage.getWidth(null), initialMapImage.getHeight(null));
        app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        app.setLayout(new BorderLayout());

        final DropDownUI dropDownUi = new DropDownUI(apiKey, locationDataUseCase);
        final JButton submitButton = new JButton("Find Location");
        final JLabel messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);

        final JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(dropDownUi);
        inputPanel.add(submitButton);

        app.add(inputPanel, BorderLayout.NORTH);
        app.add(messageLabel, BorderLayout.SOUTH);
        app.add(renderer, BorderLayout.CENTER);

        submitButton.addActionListener(actionEvent -> {
            handleLocationSelection(
                    dropDownUi.getSelectedLocation(),
                    renderer,
                    initialMapImage,
                    messageLabel
            );
        });

        app.setVisible(true);
    }

    private void handleLocationSelection(Location selectedLocation, SwingMapView renderer,
                                         Image initialMapImage, JLabel messageLabel) {
        if (selectedLocation == null) {
            messageLabel.setText("No valid location selected!");
        }

        else {
            final double mapWidth = initialMapImage.getWidth(null);
            final double mapHeight = initialMapImage.getHeight(null);
            final MercatorAlgorithm mercator = new MercatorAlgorithm(
                    selectedLocation.getLatitude(), selectedLocation.getLongitude()
            );
            final double[] mercatorCoordinates = mercator.normalizeCoordinates(mapHeight, mapWidth);

            renderer.renderMap(mercatorCoordinates[0], mercatorCoordinates[1]);
            messageLabel.setText("Displaying: " + selectedLocation.fullLocationName());
        }

    }
}
