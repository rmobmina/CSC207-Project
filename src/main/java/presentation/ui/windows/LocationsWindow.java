package presentation.ui.windows;

import application.dto.WeatherDataDTO;
import application.dto.WeatherDataDTOGenerator;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import presentation.ui.DropDownUI;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public abstract class LocationsWindow extends JFrame {
    protected final JPanel mainPanel = new JPanel();
    protected final JPanel inputPanel = new JPanel();

    protected JButton backButton = new JButton("Back To DashBoard");

    protected JButton enterLocationButton = new JButton("Enter Location");

    private GetLocationDataUseCase locationDataUseCase;
    private String apiKey;

    protected Location location;

    protected WeatherDataDTO weatherDataDTO;

    protected DropDownUI dropDown;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public String getName() { return this.getTitle(); }

    public void openWindow(){
        this.setVisible(true);
    }

    public void hideWindow(){
        this.setVisible(false);
    }

    protected LocationsWindow(String name, int[] dimensions, GetLocationDataUseCase locationDataUseCase, String apiKey,
                              ApiService apiService) {
        this.setTitle(name);
        this.setSize(dimensions[0], dimensions[1]);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.locationDataUseCase = locationDataUseCase;
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

    protected void addComponents() {
        inputPanel.add(backButton);
        inputPanel.add(dropDown);
        inputPanel.add(enterLocationButton);
    }

    protected abstract void getWeatherData();
    protected abstract void displayWeatherData();

    protected void generateWeatherDataDTO(WeatherData weatherData, LocalDate startDate, LocalDate endDate) {
        if (location != null) {
            weatherDataDTO = WeatherDataDTOGenerator.createWeatherDataDTO(weatherData, location, startDate, endDate);
        }
    }

    private void getLocationFromUseCase() {
        Location selectedLocation = dropDown.getSelectedLocation();
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

    public void setBackButtonListener(ActionListener listener){
        backButton.addActionListener(listener);
    }


}
