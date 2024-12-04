package presentation.ui.views;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * The UserOptionsView class represents a UI view where users can select
 * various weather-related options such as forecasts, comparisons, or maps.
 * It includes a main panel with buttons for different actions and a separate
 * forecast options window for selecting hourly or daily forecasts.
 */
public class UserOptionsView extends View {

    private static final JLabel optionsLabel = new JLabel("Choose one of the options below: ");
    private static final JFrame forecastOptionsWindow = new JFrame("Forecast Options");

    private static final JButton forecastOption = new JButton("Forecast");
    private static final JButton forecastHourlyOption = new JButton("Hourly");
    private static final JButton forecastDailyOption = new JButton("Daily");
    private static final JButton comparisonOption = new JButton("Historical Comparison");
    private static final JButton mercatorMapOption = new JButton("Mercator Map");

    /**
     * Constructs a UserOptionsView instance.
     * Initializes the main panel and adds buttons for selecting various options.
     */
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

    /**
     * Opens a new window for selecting specific forecast options (Hourly or Daily).
     */
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

    /**
     * Sets the action listener for the Hourly Forecast button.
     *
     * @param listener the ActionListener to be triggered when the Hourly button is clicked.
     */
    public void setForecastHourlyActionListener(ActionListener listener) {
        forecastHourlyOption.addActionListener(listener);
    }

    /**
     * Sets the action listener for the Daily Forecast button.
     *
     * @param listener the ActionListener to be triggered when the Daily button is clicked.
     */
    public void setForecastDailyActionListener(ActionListener listener) {
        forecastDailyOption.addActionListener(listener);
    }

    /**
     * Hides the forecast options window.
     * This is used when the user makes a selection or navigates away.
     */
    public void hideForecastOptionsWindow() {
        forecastOptionsWindow.setVisible(false);
    }

    /**
     * Sets the action listener for the Historical Comparison button.
     *
     * @param listener the ActionListener to be triggered when the Historical Comparison button is clicked.
     */
    public void setComparisonActionListener(ActionListener listener) {
        comparisonOption.addActionListener(listener);
    }

    /**
     * Sets the action listener for the Mercator Map button.
     *
     * @param listener the ActionListener to be triggered when the Mercator Map button is clicked.
     */
    public void setMercatorMapActionListener(ActionListener listener) {
        mercatorMapOption.addActionListener(listener);
    }
}
