package presentation.ui.windows;

import application.dto.WeatherDataDTO;
import application.dto.WeatherDataDTOGenerator;
import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.DropDownUI;
import java.util.List;

import javax.swing.*;
import javax.swing.plaf.basic.DefaultMenuLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public abstract class LocationsWindow extends JFrame {
    protected final JPanel panel = new JPanel();

    protected JButton backButton = new JButton("Back To DashBoard");

    protected JButton enterLocationButton = new JButton("Enter Location");

    private GetLocationDataUseCase locationDataUseCase;
    private String apiKey;

    protected Location location;

    protected WeatherDataDTO weatherDataDTO;

    protected DropDownUI dropDown;

    public JPanel getPanel() {
        return panel;
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

        panel.add(new JLabel(name));
        //panel.setLayout(new DefaultMenuLayout(panel, BoxLayout.Y_AXIS));
        panel.setVisible(true);
        enterLocationButton.addActionListener(e -> getLocationFromUseCase());
        addComponents();

        add(panel);
        this.setVisible(false);
    }

    protected void addComponents() {
        panel.add(backButton);
        panel.add(dropDown);
        panel.add(enterLocationButton);
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
