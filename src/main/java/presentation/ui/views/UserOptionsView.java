package presentation.ui.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserOptionsView extends View{

    private static final JLabel optionsLabel = new JLabel("Choose one of the options below: ");

    private static JButton forecastOption = new JButton("Forecast");
    private static JButton forecastHourlyOption = new JButton("Hourly");
    private static JButton forecastDailyOption = new JButton("Daily");
    private static JButton historicalOption = new JButton("Historical");
    private static JButton alertOption = new JButton("Weather Alerts");
    private static JButton comparisonOption = new JButton("Historical Comparison");
    private static JButton mercatorMapOption = new JButton("Mercator Map");

    public UserOptionsView() {
        panel.setName("User Options");
        panel.setLayout(new GridLayout(5, 1, 10, 5));

        panel.add(optionsLabel);
        panel.add(new JLabel());

        panel.add(forecastOption);
        panel.add(historicalOption);
        panel.add(alertOption);
        panel.add(comparisonOption);
        panel.add(mercatorMapOption);

        forecastOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openForecastOptionsWindow();
            }
        });
        panel.setVisible(true);
    }

    // Pops a new window with two additional options to the forecast option
    private void openForecastOptionsWindow() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.setTitle("Forecast Options");
        frame.setSize(200, 200);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        panel.setLayout(new GridLayout(1, 2, 10, 10));
        panel.add(forecastHourlyOption);
        panel.add(forecastDailyOption);
        frame.add(panel);
        frame.setVisible(false);
    }

    // Sets the action listeners for all the buttons
    public void setForecastHourlyActionListener(ActionListener listener) {
        forecastHourlyOption.addActionListener(listener);
    }

    public void setForecastDailyActionListener(ActionListener listener) {
        forecastDailyOption.addActionListener(listener);
    }

    public void setHistoricalActionListener(ActionListener listener) {
        historicalOption.addActionListener(listener);
    }

    public void setAlertActionListener(ActionListener listener) {
        alertOption.addActionListener(listener);
    }

    public void setComparisonActionListener(ActionListener listener) {
        comparisonOption.addActionListener(listener);
    }

    public void setMercatorMapActionListener(ActionListener listener) {
        mercatorMapOption.addActionListener(listener);
    }
}
