package presentation.ui.views;

import javax.swing.*;
import java.awt.*;

public class UserOptionsView {
    private String userOption;

    private final JPanel userOptionsPanel = new JPanel();

    private static final JLabel optionsLabel = new JLabel("Choose one of the options below: ");

    private static JButton forecastOption = new JButton("Forecast");
    private static JButton historicalOption = new JButton("Historical");
    private static JButton alertOption = new JButton("Weather Alerts");
    private static JButton comparisonOption = new JButton("Historical Comparison");
    private static JButton mercatorMapOption = new JButton("Mercator Map");

    public UserOptionsView() {
        userOptionsPanel.setName("User Options");
        userOptionsPanel.setLayout(new GridLayout(5, 1, 5, 5));
        userOptionsPanel.setVisible(true);

        userOptionsPanel.add(optionsLabel);

        userOptionsPanel.add(forecastOption);
        userOptionsPanel.add(historicalOption);
        userOptionsPanel.add(alertOption);
        userOptionsPanel.add(comparisonOption);
        userOptionsPanel.add(mercatorMapOption);
    }

    public JPanel getPanel(){
        return userOptionsPanel;
    }

    public String getUserOption() {
        return userOption;
    }
}
