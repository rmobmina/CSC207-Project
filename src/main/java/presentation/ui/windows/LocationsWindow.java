package presentation.ui.windows;

import domain.entities.Location;
import presentation.ui.DropDownUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class LocationsWindow extends JFrame {
    protected final JPanel panel = new JPanel();

    protected JButton backButton = new JButton("Back To DashBoard");

    protected Location location;

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

    protected LocationsWindow(String name, int width, int height) {
        this.setTitle(name);
        this.setSize(width, height);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel.add(new JLabel(name));
        panel.add(backButton);
        panel.setVisible(true);
        add(panel);
        this.setVisible(false);
    }

    public void setBackButtonListener(ActionListener listener){
        backButton.addActionListener(listener);
    }


}
