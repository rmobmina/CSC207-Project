package presentation.ui.views;

import application.dto.WeatherDataDTO;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

// This is class that constructs a panel for a specific day (with weather details)
public class DayPanel extends JPanel {
    JLabel dateLabel = new JLabel();
    JLabel maxTemperatureLabel = new JLabel();
    JLabel maxTemperatureValue = new JLabel();
    JLabel minTemperatureLabel = new JLabel();
    JLabel minTemperatureValue = new JLabel();
    JLabel meanTemperatureLabel = new JLabel();
    JLabel meanTemperatureValue = new JLabel();
    JLabel percipitationLabel = new JLabel();
    JLabel percipitationValue = new JLabel();
    JLabel windLabel = new JLabel();
    JLabel windValue = new JLabel();

    public DayPanel(LocalDate date, boolean dayIsCurrent) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setDate(date, dayIsCurrent);
        this.setBorder(BorderFactory.createTitledBorder(""));
        maxTemperatureLabel.setText("Max Temperature: ");
        minTemperatureLabel.setText("Min Temperature: ");
        meanTemperatureLabel.setText("Mean Temperature: ");
        percipitationLabel.setText("Percipitation: ");
        windLabel.setText("Wind Speed & Direction: ");
        initComponents();
    }

    private void initComponents() {
        this.add(dateLabel);
        final JPanel weatherPanel = new JPanel();
        weatherPanel.setLayout(new GridLayout(5, 1, 1, 1));
        weatherPanel.add(maxTemperatureLabel);
        weatherPanel.add(maxTemperatureValue);
        weatherPanel.add(minTemperatureLabel);
        weatherPanel.add(minTemperatureValue);
        weatherPanel.add(meanTemperatureLabel);
        weatherPanel.add(meanTemperatureValue);
        weatherPanel.add(percipitationLabel);
        weatherPanel.add(percipitationValue);
        weatherPanel.add(windLabel);
        weatherPanel.add(windValue);
        this.add(weatherPanel);
    }

    public void setDate(LocalDate date, boolean dayIsCurrent) {
        if (dayIsCurrent) {
            dateLabel.setText("Today");
        }
        else {
            final DayOfWeek dayOfWeek = date.getDayOfWeek();
            dateLabel.setText(dayOfWeek.toString() + " " + date);
        }
    }

    public void setMaxTemperatureValue(String maxTemperature) {
        this.maxTemperatureValue.setText(maxTemperature);
    }

    public void setMinTemperatureValue(String minTemperature) {
        this.minTemperatureValue.setText(minTemperature);
    }

    public void setPercipitationValue(String percipitation) {
        this.percipitationValue.setText(percipitation);
    }

    public void setWindValue(String percipitation) {
        this.windValue.setText(percipitation);
    }

    public void setMeanTemperatureValue(String percipitation) {
        this.meanTemperatureValue.setText(percipitation);
    }

    public void updateWeatherDataValues(WeatherDataDTO weatherDataDTO, int index, String temperatureUnitType) {
        // Update temperature values
        setMaxTemperatureValue(
                weatherDataDTO.temperatureToString("temperatureMaxDaily", index, temperatureUnitType));
        setMinTemperatureValue(
                weatherDataDTO.temperatureToString("temperatureMinDaily", index, temperatureUnitType));
        setMeanTemperatureValue(weatherDataDTO.temperatureToString("temperatureMeanDaily", index, temperatureUnitType));
        // Update precipitation value
        setPercipitationValue(
                weatherDataDTO.dataToString("percipitationDaily", index));
        // Optionally, update additional fields like wind speed and direction
        setWindValue(weatherDataDTO.dataToString("windSpeedDaily", index) + " " +
                weatherDataDTO.dataToString("windDirectionDaily", index));
    }
}
