package presentation.ui.windows;

import javax.swing.*;

import application.usecases.GetLocationDataUseCase;
import domain.interfaces.ApiService;

/**
 * The UserOptionsView class represents a UI view where users can select
 * various weather-related options such as forecasts, comparisons, or maps.
 * It includes a main panel with buttons for different actions and a separate
 * forecast options window for selecting hourly or daily forecasts.
 */
public class ErrorLocationsWindow extends LocationsWindow {

    private final JLabel errorMessageLabel = new JLabel("");

    /**
     * Constructs an ErrorLocationsWindow instance.
     *
     * @param errorMessage       The error message to display in the window.
     * @param dimensions         The dimensions of the window.
     * @param locationDataUseCase The use case for handling location data.
     * @param apiKey             The API key for accessing weather data.
     * @param apiService         The service for interacting with the weather API.
     */
    public ErrorLocationsWindow(String errorMessage, int[] dimensions,
                                GetLocationDataUseCase locationDataUseCase, String apiKey, ApiService apiService) {
        super("Error Locations Window", dimensions, locationDataUseCase, apiKey, apiService);
        setErrorMessage(errorMessage);
        mainPanel.remove(dropDown);
        mainPanel.remove(enterLocationButton);
        mainPanel.add(errorMessageLabel);
    }

    /**
     * Sets the error message to be displayed in the window.
     *
     * @param errorMessage The error message to display.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessageLabel.setText(errorMessage);
    }

    /**
     * Placeholder for visualization functionality.
     * This method is overridden but does not perform any actions as error windows do not
     * support visualization.
     */
    @Override
    protected void openVisualization() {

    }

    /**
     * Placeholder for fetching weather data.
     * This method is overridden but does not perform any actions as error windows do not
     * require weather data.
     */
    @Override
    protected void getWeatherData() {

    }

    /**
     * Placeholder for displaying weather data.
     * This method is overridden but does not perform any actions as error windows do not
     * display weather data.
     */
    @Override
    protected void displayWeatherData() {

    }

}
