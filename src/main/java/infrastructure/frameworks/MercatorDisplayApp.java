package infrastructure.frameworks;

import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import application.usecases.GetLocationDataUseCase;
import application.usecases.LoadMapImageUseCase;
import domain.entities.Coordinate;
import domain.entities.Location;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.visualization.MercatorAlgorithm;
import presentation.visualization.MercatorMapApp;
import presentation.visualization.SwingMapRenderer;
import presentation.ui.DropDownUI;
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
    public void startMercatorMap(String apiKey, GetLocationDataUseCase locationDataUseCase, OpenWeatherApiService apiService) {
        try {
            // Dependency: LoadMapImageUseCase
            final LoadMapImageUseCase loadMapImageUseCase = new LoadMapImageUseCase();

            // Load the initial map image
            final String mapFilePath = new File("").getAbsolutePath() + Constants.MERCATOR_IMAGE_PATH;
            final File file = new File(mapFilePath);

            if (!file.exists()) {
                throw new RuntimeException("Map image file not found at: " + mapFilePath);
            }

            final Image initialMapImage = loadMapImageUseCase.execute(mapFilePath);
            if (initialMapImage == null) {
                throw new RuntimeException("Map image could not be loaded. Check the file path.");
            }

            // Initialize dependencies
            final SwingMapRenderer renderer = new SwingMapRenderer(initialMapImage);
            final MercatorMapApp app = new MercatorMapApp(renderer);

            // Set up the application window
            app.setSize(initialMapImage.getWidth(null), initialMapImage.getHeight(null));
            app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Allow closing without exiting app
            app.setLayout(new BorderLayout());
            app.add(renderer, BorderLayout.CENTER);

            // Add dropdown UI for location selection
            DropDownUI dropDownUI = new DropDownUI(apiKey, locationDataUseCase);
            JButton submitButton = new JButton("Find Location");
            JLabel messageLabel = new JLabel();
            messageLabel.setForeground(Color.RED);

            JPanel inputPanel = new JPanel(new FlowLayout());
            inputPanel.add(dropDownUI);
            inputPanel.add(submitButton);
            app.add(inputPanel, BorderLayout.NORTH);
            app.add(messageLabel, BorderLayout.SOUTH);

            // Button click listener to reload map based on location
            submitButton.addActionListener(e -> {
                Location selectedLocation = dropDownUI.getSelectedLocation();
                if (selectedLocation == null) {
                    messageLabel.setText("No valid location selected!");
                    return;
                }

                try {
                    // Convert latitude and longitude to Mercator projection coordinates
                    double mapWidth = initialMapImage.getWidth(null);
                    double mapHeight = initialMapImage.getHeight(null);
                    MercatorAlgorithm mercator = new MercatorAlgorithm(selectedLocation.getLatitude(), selectedLocation.getLongitude());
                    double[] mercatorCoordinates = mercator.normalizeCoordinates(mapHeight, mapWidth);

                    // Update the map with the red dot at the correct location
                    renderer.renderMap(mercatorCoordinates[0], mercatorCoordinates[1]);
                    messageLabel.setText("Displaying: " + selectedLocation.fullLocationName());
                }
                catch (Exception ex) {
                    messageLabel.setText("Error: " + ex.getMessage());
                }
            });

            app.setVisible(true);

        } catch (IOException exception) {
            System.err.println("Failed to load the map image: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}
