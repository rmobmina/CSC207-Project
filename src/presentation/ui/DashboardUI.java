package presentation.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardUI extends JFrame {
    // implemented variables, might not end up using all depending
    static final JLabel locationLabel = new JLabel("Location:");
    static final JTextField locationField = new JTextField(20);
    static final JLabel temperatureLabel = new JLabel("Temperature:");
    static final JLabel temperatureValue = new JLabel("N/A"); // Placeholder for temperature
    static final JLabel conditionLabel = new JLabel("Condition:");
    static final JLabel conditionValue = new JLabel("N/A"); // Placeholder for weather condition
    static final JLabel humidityLabel = new JLabel("Humidity:");
    static final JLabel humidityValue = new JLabel("N/A"); // Placeholder for humidity
    static final JLabel windLabel = new JLabel("Wind:");
    static final JLabel windValue = new JLabel("N/A"); // Placeholder for wind speed
    static final JButton refreshButton = new JButton("Refresh");
    static final JButton rangeOfTimeButton = new JButton("Range of Time");
    static final JButton simulationButton = new JButton("Simulation");

    // A: main dashboard, its messy for now but we'll split them up for clean architecture and for a cleaner look
    public DashboardUI() {
        setTitle("Weather Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10)); // B: Will be changed as we add more elements

        // B: main compenents of GUI, most are placeholders
        DropDownUI menu = new DropDownUI();
        panel.add(new JLabel("Location:"));
        panel.add(menu);

        panel.add(temperatureLabel);
        panel.add(temperatureValue);

        panel.add(conditionLabel);
        panel.add(conditionValue);

        panel.add(humidityLabel);
        panel.add(humidityValue);

        panel.add(windLabel);
        panel.add(windValue);

        panel.add(new JLabel());
        panel.add(refreshButton);

        // B: buttons for range and simulation
        panel.add(rangeOfTimeButton);
        panel.add(simulationButton);

        add(panel);

        // Buttons that make the menus for range of time and the simulation
        rangeOfTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRangeOfTimeWindow();
            }
        });

        simulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSimulationWindow();
            }
        });
    }

    // Placeholder window for "Range of Time" feature
    private void openRangeOfTimeWindow() {
        JFrame rangeWindow = new JFrame("Range of Time");
        rangeWindow.setSize(300, 200);
        rangeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel placeholderLabel = new JLabel("feature not added yet.", SwingConstants.CENTER);
        rangeWindow.add(placeholderLabel);
        rangeWindow.setVisible(true);
    }

    // Placeholder window for simulation
    private void openSimulationWindow() {
        JFrame simulationWindow = new JFrame("Simulation");
        simulationWindow.setSize(300, 200);
        simulationWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel placeholderLabel = new JLabel("feature not added yet.", SwingConstants.CENTER);
        simulationWindow.add(placeholderLabel);
        simulationWindow.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DashboardUI frame = new DashboardUI();
            frame.setVisible(true);
        });
    }
}
