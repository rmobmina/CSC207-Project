package presentation.ui.windows;

import domain.entities.Location;
import presentation.ui.DropDownUI;

import javax.swing.*;

public abstract class LocationsWindow {
    protected final JPanel panel = new JPanel();

    protected Location location;

    protected DropDownUI dropDown;

    public JPanel getWindow() {
        return panel;
    }

    public LocationsWindow(String name) {
        panel.setName(name);
    }
}
