package presentation.ui.windows;

import application.dto.WeatherDataDTO;
import application.dto.WeatherDataDTOGenerator;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import presentation.ui.DropDownUI;
import presentation.ui.FavoritesManager;
import presentation.ui.views.FavoritesView;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public abstract class LocationsWindow extends JFrame {
    protected final JPanel mainPanel = new JPanel();
    protected final JPanel inputPanel = new JPanel();
    protected JButton favoritesButton = new JButton("Favorites"); // Button for favorites
    protected JButton addToFavoritesButton = new JButton("Add to Favorites");


    protected JButton backButton = new JButton("Back To DashBoard");

    protected JButton enterLocationButton = new JButton("Enter Location");

    private GetLocationDataUseCase locationDataUseCase;

    protected VisualizationUI visualizationUI;
    private String apiKey;

    public Location location;

    protected WeatherDataDTO weatherDataDTO;

    protected DropDownUI dropDown;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public String getName() {
        return this.getTitle();
    }

    public void openWindow() {
        this.setVisible(true);
    }

    public void hideWindow() {
        this.setVisible(false);
    }

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

    protected abstract void openVisualization();

    protected void addComponents() {
        inputPanel.add(backButton);
        inputPanel.add(dropDown);
        inputPanel.add(enterLocationButton);
        inputPanel.add(favoritesButton); // Added the Favorites button to the panel
        inputPanel.add(addToFavoritesButton); // Added the ADDTOFAVORITES button
    }

    protected abstract void getWeatherData();

    protected abstract void displayWeatherData();

    protected void generateWeatherDataDTO(WeatherData weatherData, LocalDate startDate, LocalDate endDate) {
        if (location != null) {
            weatherDataDTO = WeatherDataDTOGenerator.createWeatherDataDTO(weatherData, location, startDate, endDate);
        }
    }

    // Currently a place holder
    protected void openFavoritesView(FavoritesManager favoritesManager) {
        FavoritesView favoritesView = new FavoritesView(favoritesManager, apiKey, this); // Pass `this` as the parent window
        favoritesView.setVisible(true);
    }


    private void getLocationFromUseCase() {
        Location selectedLocation = dropDown.getSelectedLocation();
        // For now keep it this way, but later needs to be changed
        if (selectedLocation == null) {
            JOptionPane.showMessageDialog(this, "No locations found");
        } else {
            this.location = selectedLocation;
            getWeatherData();
            displayWeatherData();
        }
    }

    public void setBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void setFavoritesButtonListener(ActionListener listener) {
        favoritesButton.addActionListener(listener);
    }

    public void setAddToFavoritesButtonListener(FavoritesManager favoritesManager) {
        addToFavoritesButton.addActionListener(e -> {
            if (location != null) { // Ensure a location is selected
                if (favoritesManager.getFavorites().contains(location)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Location already added to favorites!",
                            "Duplicate Entry",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    favoritesManager.addFavorite(location);
                    JOptionPane.showMessageDialog(
                            this,
                            "Location added to favorites!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No location selected.",
                        "Error",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });
    }

    public void setWeatherLocation(Location location) {
        this.location = location;
        getWeatherData(); // Fetch weather data for the selected location
        displayWeatherData(); // Display the fetched data
    }

    public void setSearchBarText(String cityName) {
        dropDown.getLocationField().setText(cityName);
    }


}
