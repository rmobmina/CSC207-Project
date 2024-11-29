package presentation.ui.windows;

import javax.swing.*;

public class ErrorLocationsWindow extends LocationsWindow {
    private JLabel errorMessageLabel = new JLabel("");

    public ErrorLocationsWindow(String errorMessage, int width, int height) {
        super("Error Locations Window", width, height);
        setErrorMessage(errorMessage);
        panel.add(errorMessageLabel);
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessageLabel.setText(errorMessage);
    }
}
