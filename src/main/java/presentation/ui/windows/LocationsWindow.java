package presentation.ui.windows;

import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.*;

import application.dto.WeatherDataDTO;
import application.dto.WeatherDataDTOGenerator;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import presentation.ui.DropDownUI;

/**
 * The `LocationsWindow` class serves as an abstract base class for creating UI windows
 * that handle location-based weather data. It provides a framework for managing user
 * inputs, displaying weather data, and integrating with favorites functionality.
 */
public abstract class LocationsWindow extends JFrame {
    protected final JPanel mainPanel = new JPanel();
    protected final JPanel inputPanel = new JPanel();
    protected final JButton favoritesButton = new JButton("Favorites");
    protected final JButton addToFavoritesButton = new JButton("Add to Favorites");

    protected JButton backButton = new JButton("Back To DashBoard");

    protected JButton enterLocationButton = new JButton("Enter Location");

    private final GetLocationDataUseCase locationDataUseCase;

    protected VisualizationUI visualizationUI;
    private final String apiKey;

    public Location location;

    protected WeatherDataDTO weatherDataDTO;

    protected DropDownUI dropDown;

    /**
     * Returns the main panel of the window.
     *
     * @return The main panel (`JPanel`).
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }


    /**
     * Returns the name (title) of the window.
     *
     * @return The window title as a string.
     */
    public String getName() {
        return this.getTitle();
    }

    /**
     * Opens the window and makes it visible to the user.
     */
    public void openWindow() {
        this.setVisible(true);
    }

    /**
     * Hides the window, making it invisible to the user.
     */
    public void hideWindow() {
        this.setVisible(false);
    }

    /**
     * Constructs a `LocationsWindow` instance.
     *
     * @param name                The title of the window.
     * @param dimensions          The dimensions of the window (width and height).
     * @param locationDataUseCase The use case for fetching location data.
     * @param apiKey              The API key for accessing weather services.
     * @param apiService          The API service for weather data retrieval.
     */
    protected LocationsWindow(String name, int[] dimensions, GetLocationDataUseCase locationDataUseCase, String apiKey,
                              ApiService apiService) {
        this.setTitle(name);
        this.setSize(dimensions[0], dimensions[1]);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.locationDataUseCase = locationDataUseCase;
        this.visualizationUI = new VisualizationUI(1, mainPanel);
        this.apiKey = apiKey;
        this.dropDown = new DropDownUI(apiKey, locationDataUseCase);

        mainPanel.add(new JLabel(name));
        mainPanel.setVisible(true);
        enterLocationButton.addActionListener(e -> getLocationFromUseCase());
        addComponents();
        mainPanel.add(inputPanel);

        add(mainPanel);
        this.setVisible(false);
    }

    /**
     * Abstract method to open a visualization for the weather data.
     * Implementations must define how the visualization is displayed.
     */
    protected abstract void openVisualization();

    protected void addComponents() {
        inputPanel.add(backButton);
        inputPanel.add(dropDown);
        inputPanel.add(enterLocationButton);
        inputPanel.add(favoritesButton);
        inputPanel.add(addToFavoritesButton);
    }

    /**
     * Abstract method to fetch weather data for the selected location.
     * Implementations must define the data retrieval process.
     */
    protected abstract void getWeatherData();

    /**
     * Abstract method to display weather data in the UI.
     * Implementations must define how the data is presented.
     */
    protected abstract void displayWeatherData();

    /**
     * Generates a `WeatherDataDTO` using the provided weather data and date range.
     *
     * @param weatherData The raw weather data object.
     * @param startDate   The start date of the data range.
     * @param endDate     The end date of the data range.
     */
    protected void generateWeatherDataDTO(WeatherData weatherData, LocalDate startDate, LocalDate endDate) {
        if (location != null) {
            weatherDataDTO = WeatherDataDTOGenerator.createWeatherDataDTO(weatherData, location, startDate, endDate);
        }
    }

    /**
     * Retrieves the selected location from the dropdown and updates the UI with weather data.
     * Displays an error message if no location is selected.
     */
    private void getLocationFromUseCase() {
        final Location selectedLocation = dropDown.getSelectedLocation();
        // For now keep it this way, but later needs to be changed
        if (selectedLocation == null) {
            JOptionPane.showMessageDialog(this, "No locations found");
        }
        else {
            this.location = selectedLocation;
            getWeatherData();
            displayWeatherData();
        }
    }

    /**
     * Sets the action listener for the "Back to Dashboard" button.
     *
     * @param listener The action listener to be triggered when the button is clicked.
     */
    public void setBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    /**
     * Sets the action listener for the "Favorites" button.
     *
     * @param listener The action listener to be triggered when the button is clicked.
     */
    public void setFavoritesButtonListener(ActionListener listener) {
        favoritesButton.addActionListener(listener);
    }

    /**
     * Sets the action listener for the "Add to Favorites" button.
     *
     * @param favoritesManager The manager for handling favorite locations.
     */
    public void setAddToFavoritesButtonListener(FavoritesManager favoritesManager) {
        addToFavoritesButton.addActionListener(e -> {
            if (location != null) {
                if (favoritesManager.getFavorites().contains(location)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Location already added to favorites!",
                            "Duplicate Entry",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
                else {
                    favoritesManager.addFavorite(location);
                    JOptionPane.showMessageDialog(
                            this,
                            "Location added to favorites!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
            else {
                JOptionPane.showMessageDialog(
                        this,
                        "No location selected.",
                        "Error",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });
    }

    /**
     * Sets the selected location and updates the weather data and display.
     *
     * @param location The location to set.
     */
    public void setWeatherLocation(Location location) {
        this.location = location;
        getWeatherData();
        displayWeatherData();
    }

    /**
     * Updates the search bar with the given city name.
     *
     * @param cityName The name of the city to set in the search bar.
     */
    public void setSearchBarText(String cityName) {
        dropDown.getLocationField().setText(cityName);
    }

}
