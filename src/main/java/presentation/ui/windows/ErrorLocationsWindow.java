package presentation.ui.windows;

import application.usecases.GetLocationDataUseCase;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;

import javax.swing.*;

public class ErrorLocationsWindow extends LocationsWindow {
    private JLabel errorMessageLabel = new JLabel("");

    public ErrorLocationsWindow(String errorMessage, int[] dimensions, GetLocationDataUseCase locationDataUseCase, String apiKey,
                                ApiService apiService) {
        super("Error Locations Window", dimensions, locationDataUseCase, apiKey, apiService);
        setErrorMessage(errorMessage);
        panel.remove(dropDown);
        panel.remove(enterLocationButton);
        panel.add(errorMessageLabel);
    }

    @Override
    protected void getWeatherData() {

    }

    @Override
    protected void displayWeatherData() {

    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessageLabel.setText(errorMessage);
    }
}
