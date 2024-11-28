package presentation.ui;

import application.usecases.GetLocationsWindowUseCase;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.views.*;
import presentation.ui.windows.LocationsWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class that handles UI to display a specific view panel (one for each use case) based on the user option
 * and an options view panel to receive the user's input.
 */

public class DashBoardUi extends JFrame {
    String apiKey = "";
    ApiService apiService;
    LocationsWindow locationsWindow;
    GetLocationsWindowUseCase locationsUseCase;
    UserOptionsView userOptionsView;

    public DashBoardUi(ApiService apiService, UserOptionsView userOptionsView, GetLocationsWindowUseCase getLocationsWindowUseCase) {
        this.apiService = apiService;
        this.userOptionsView = userOptionsView;
        this.locationsUseCase = getLocationsWindowUseCase;
        setTitle("Weather Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(userOptionsView.getPanel());
        userOptionsView.setForecastHourlyActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getLocationsWindow(ForecastHourlyView.OPTION_NAME);
            }
        });

        userOptionsView.setForecastDailyActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getLocationsWindow(ForecastDailyView.OPTION_NAME);
            }
        });

        userOptionsView.setHistoricalActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getLocationsWindow(HistoricalWeatherView.OPTION_NAME);
            }
        });

        userOptionsView.setComparisonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Change later to ask user to enter how many locations they want
                getLocationsWindowMultiple(HistoricalWeatherComparisonView.OPTION_NAME, 2);
            }
        });

        userOptionsView.setAlertActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getLocationsWindow(WeatherAlertView.OPTION_NAME);
            }
        });

        userOptionsView.setMercatorMapActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Change later to ask user to enter how many locations they want
                getLocationsWindowMultiple(MercatorMapView.OPTION_NAME, 2);
            }
        });
    }

    private void getLocationsWindow(String userOption){
        getLocationsWindowMultiple(userOption, 1);
    }

    private void getLocationsWindowMultiple(String userOption, int numOfLocations){
        this.locationsWindow = locationsUseCase.execute(userOption, numOfLocations);
        System.out.println(locationsWindow.getName());
    }

    public void runJFrame(OpenWeatherApiService weatherApiService) {
        apiService = weatherApiService;
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }

    public void setAPIkey(String apiKey) {
        this.apiKey = apiKey;
    }
}
