package presentation.ui;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetHistoricalWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetLocationsWindowUseCase;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.views.*;
import presentation.ui.windows.ErrorLocationsWindow;
import presentation.ui.windows.LocationsWindow;

import javax.swing.*;

/**
 * Class that handles UI to display a specific view panel (one for each use case) based on the user option
 * and an options view panel to receive the user's input.
 */

public class NewDashBoardUi extends JFrame {
    String apiKey = "";
    ApiService apiService;
    GetLocationsWindowUseCase locationsWindowUseCase;
    GetLocationDataUseCase locationDataUseCase;
    GetForecastWeatherDataUseCase forecastWeatherDataUseCase;
    GetHistoricalWeatherDataUseCase historicalWeatherDataUseCase;

    LocationsWindow locationsWindow;
    UserOptionsView userOptionsView;
    SelectNumberLocationsView numberLocationsView;

    final String OPTIONS_NAME = "Options";
    final String LOCATIONS_WINDOW_NAME = "Locations";
    final String NUMBER_LOCATIONS_WINDOW_NAME = "Number Locations";

    final int locationsWindowWidth = 500;
    final int locationsWindowHeight = 500;

    private String userOption;

    public NewDashBoardUi(GetLocationsWindowUseCase locationsWindowUseCase,
                          GetLocationDataUseCase locationDataUseCase,
                          GetForecastWeatherDataUseCase forecastWeatherDataUseCase,
                          GetHistoricalWeatherDataUseCase historicalWeatherDataUseCase,
                          UserOptionsView userOptionsView, SelectNumberLocationsView numberLocationsView) {

        initVariables(locationsWindowUseCase, locationDataUseCase,
                forecastWeatherDataUseCase, historicalWeatherDataUseCase,
                userOptionsView, numberLocationsView);

        setTitle("Weather Dashboard");
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(userOptionsView.getPanel());
        add(numberLocationsView.getPanel());

        switchToWindow(OPTIONS_NAME);

        setButtonListeners();

    }

    private void initVariables(GetLocationsWindowUseCase getLocationsWindowUseCase,
                               GetLocationDataUseCase locationDataUseCase,
                               GetForecastWeatherDataUseCase forecastWeatherDataUseCase,
                               GetHistoricalWeatherDataUseCase historicalWeatherDataUseCase,
                               UserOptionsView userOptionsView,
                               SelectNumberLocationsView numberLocationsView){
        this.locationsWindowUseCase = getLocationsWindowUseCase;
        this.locationDataUseCase = locationDataUseCase;
        this.forecastWeatherDataUseCase = forecastWeatherDataUseCase;
        this.historicalWeatherDataUseCase = historicalWeatherDataUseCase;
        this.userOptionsView = userOptionsView;
        this.numberLocationsView = numberLocationsView;
    }

    private void setButtonListeners(){
        userOptionsView.setForecastHourlyActionListener(e -> {
            getLocationsWindow(ForecastHourlyView.OPTION_NAME, locationsWindowWidth, locationsWindowHeight);
            userOptionsView.hideForecastOptionsWindow();
        });

        userOptionsView.setForecastDailyActionListener(e -> {
            getLocationsWindow(ForecastDailyView.OPTION_NAME, locationsWindowWidth, locationsWindowHeight);
            userOptionsView.hideForecastOptionsWindow();
        });

        userOptionsView.setHistoricalActionListener(e -> getLocationsWindow(HistoricalWeatherView.OPTION_NAME,
                locationsWindowWidth, locationsWindowHeight));

        userOptionsView.setComparisonActionListener(e -> {
                // Ask user to enter how many locations they want
                showNumberOfLocationsWindow();
                userOption = HistoricalWeatherComparisonView.OPTION_NAME;
        });

        // Remove later (this will automatically pop up when the user enters a location)
        userOptionsView.setAlertActionListener(e -> getLocationsWindow(WeatherAlertView.OPTION_NAME,
                locationsWindowWidth, locationsWindowHeight));

        userOptionsView.setMercatorMapActionListener(e -> {
                // Ask user to enter how many locations they want
                showNumberOfLocationsWindow();
                userOption = MercatorMapView.OPTION_NAME;
        });

        numberLocationsView.setActionListener(e -> getLocationsWindowMultiple(
                userOption, locationsWindowWidth, locationsWindowHeight, numberLocationsView.getNumOfLocations()));
    }

    private void getLocationsWindow(String userOption, int width, int height){
        getLocationsWindowMultiple(userOption, width, height, 1);
    }

    private void getLocationsWindowMultiple(String userOption, int width, int height, int numOfLocations){
        this.locationsWindow = locationsWindowUseCase.execute(userOption, new int[]{width, height}, numOfLocations, locationDataUseCase, apiKey, apiService);
        locationsWindow.setBackButtonListener(e -> backToDashBoard());
        switchToWindow(LOCATIONS_WINDOW_NAME);
    }

    // Opens up a window to get the desired number of locations before applying it to the chosen use case
    private void showNumberOfLocationsWindow(){
        switchToWindow(NUMBER_LOCATIONS_WINDOW_NAME);
    }

    private void switchToWindow(String windowName){
        hideAllWindows();
        switch(windowName){
            case OPTIONS_NAME:
                userOptionsView.showPanel();
                this.setContentPane(userOptionsView.getPanel()); break;
            case LOCATIONS_WINDOW_NAME:
                locationsWindow.openWindow();
                toggleShowDashBoard(false); break; // closes the current window
            case NUMBER_LOCATIONS_WINDOW_NAME:
                numberLocationsView.showPanel();
                this.setContentPane(numberLocationsView.getPanel()); break;
            default:

        }
    }

    // Closes the current locations window and opens up the dahboard window
    private void backToDashBoard(){
        toggleShowDashBoard(true);
        switchToWindow(OPTIONS_NAME);
    }

    private void toggleShowDashBoard(boolean toggle){
        this.setVisible(toggle);
    }

    private void hideAllWindows() {
        userOptionsView.hidePanel();
        numberLocationsView.hidePanel();
        if (locationsWindow != null) locationsWindow.hideWindow();
    }

    public void runJFrame(OpenWeatherApiService weatherApiService) {
        apiService = weatherApiService;
        this.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }

    public void setAPIkey(String apiKey) {
        this.apiKey = apiKey;
    }
}
