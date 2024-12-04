package presentation.ui.views;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

import javax.swing.*;

import application.dto.WeatherDataDTO;

/**
 * A class that constructs a panel for displaying weather details for a specific day.
 * The panel includes information such as maximum, minimum, and mean temperatures,
 * precipitation, and wind details.
 */
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

    /**
     * Constructs a DayPanel to display weather details for a specific day.
     *
     * @param date          The date for which the weather details are displayed.
     * @param dayIsCurrent  Indicates whether the date corresponds to the current day.
     */
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

    /**
     * Initializes and adds components to the panel.
     */
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

    /**
     * Sets the date label for the panel.
     *
     * @param date          The date to display on the panel.
     * @param dayIsCurrent  Indicates whether the date corresponds to the current day.
     */
    public void setDate(LocalDate date, boolean dayIsCurrent) {
        if (dayIsCurrent) {
            dateLabel.setText("Today");
        }
        else {
            final DayOfWeek dayOfWeek = date.getDayOfWeek();
            dateLabel.setText(dayOfWeek.toString() + " " + date);
        }
    }

    /**
     * Sets the maximum temperature value on the panel.
     *
     * @param maxTemperature The maximum temperature to display.
     */
    public void setMaxTemperatureValue(String maxTemperature) {
        this.maxTemperatureValue.setText(maxTemperature);
    }

    /**
     * Sets the minimum temperature value on the panel.
     *
     * @param minTemperature The minimum temperature to display.
     */
    public void setMinTemperatureValue(String minTemperature) {
        this.minTemperatureValue.setText(minTemperature);
    }

    /**
     * Sets the precipitation value on the panel.
     *
     * @param percipitation The precipitation value to display.
     */
    public void setPercipitationValue(String percipitation) {
        this.percipitationValue.setText(percipitation);
    }

    /**
     * Sets the precipitation value on the panel.
     *
     * @param percipitation The precipitation value to display.
     */
    public void setWindValue(String percipitation) {
        this.windValue.setText(percipitation);
    }

    /**
     * Sets the precipitation value on the panel.
     *
     * @param percipitation The precipitation value to display.
     */
    public void setMeanTemperatureValue(String percipitation) {
        this.meanTemperatureValue.setText(percipitation);
    }

    /**
     * Updates the weather data values on the panel using the provided WeatherDataDTO.
     *
     * @param weatherDataDTO    The DTO containing weather data for multiple days.
     * @param index             The index of the day's data within the DTO.
     * @param temperatureUnitType The unit type for temperature (e.g., Celsius or Fahrenheit).
     */
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
        setWindValue(weatherDataDTO.dataToString("windSpeedDaily", index) + " "
                +
                weatherDataDTO.dataToString("windDirectionDaily", index));
    }
}
