package presentation.ui.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserOptionsView extends View{

    private static final JLabel optionsLabel = new JLabel("Choose one of the options below: ");

    private static JFrame forecastOptionsWindow = new JFrame("Forecast Options");

    private static JButton forecastOption = new JButton("Forecast");
    private static JButton forecastHourlyOption = new JButton("Hourly");
    private static JButton forecastDailyOption = new JButton("Daily");
    private static JButton comparisonOption = new JButton("Historical Comparison");
    private static JButton mercatorMapOption = new JButton("Mercator Map");

    public UserOptionsView() {
        panel.setName("User Options");
        panel.setLayout(new GridLayout(5, 1, 10, 5));

        panel.add(optionsLabel);
        panel.add(new JLabel());

        panel.add(forecastOption);
        panel.add(comparisonOption);
        panel.add(mercatorMapOption);

        forecastOption.addActionListener(e -> openForecastOptionsWindow());
        panel.setVisible(true);
    }

    // Pops up a new window with two additional options to the forecast option
    private void openForecastOptionsWindow() {
        JPanel panel = new JPanel();
        forecastOptionsWindow.setTitle("Forecast Options");
        forecastOptionsWindow.setSize(350, 200);
        forecastOptionsWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        panel.setLayout(new GridLayout(1, 2, 10, 10));
        panel.add(forecastHourlyOption);
        panel.add(forecastDailyOption);
        forecastOptionsWindow.add(panel);
        forecastOptionsWindow.setVisible(true);
    }

    // Sets the action listeners for all the buttons
    public void setForecastHourlyActionListener(ActionListener listener) {
        forecastHourlyOption.addActionListener(listener);
    }

    public void setForecastDailyActionListener(ActionListener listener) {
        forecastDailyOption.addActionListener(listener);
    }

    public void hideForecastOptionsWindow() {
        forecastOptionsWindow.setVisible(false);
    }

    public void setComparisonActionListener(ActionListener listener) {
        comparisonOption.addActionListener(listener);
    }

    public void setMercatorMapActionListener(ActionListener listener) {
        mercatorMapOption.addActionListener(listener);
    }
}
