package presentation.ui;

import domain.entities.Location;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class LocationsWindow {
    protected final JPanel panel = new JPanel();

    public JPanel getWindow() {
        return panel;
    }
}
