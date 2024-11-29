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
    ErrorLocationsWindow errorLocationsWindow = new ErrorLocationsWindow("ERROR", 100, 100);

    final String OPTIONS_NAME = "Options";
    final String LOCATIONS_WINDOW_NAME = "Locations";
    final String NUMBER_LOCATIONS_WINDOW_NAME = "Number Locations";

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

        add(userOptionsView.getWindow());
        add(numberLocationsView.getWindow());

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
        userOptionsView.setForecastHourlyActionListener(e -> getLocationsWindow(ForecastHourlyView.OPTION_NAME));

        userOptionsView.setForecastDailyActionListener(e -> getLocationsWindow(ForecastDailyView.OPTION_NAME));

        userOptionsView.setHistoricalActionListener(e -> getLocationsWindow(HistoricalWeatherView.OPTION_NAME));

        userOptionsView.setComparisonActionListener(e -> {
                // Ask user to enter how many locations they want
                showNumberOfLocationsWindow();
                userOption = HistoricalWeatherComparisonView.OPTION_NAME;
        });

        userOptionsView.setAlertActionListener(e -> getLocationsWindow(WeatherAlertView.OPTION_NAME));

        userOptionsView.setMercatorMapActionListener(e -> {
                // Ask user to enter how many locations they want
                showNumberOfLocationsWindow();
                userOption = MercatorMapView.OPTION_NAME;
        });

        numberLocationsView.setActionListener(e -> getLocationsWindowMultiple(
                userOption, numberLocationsView.getNumOfLocations()));
    }

    private void getLocationsWindow(String userOption){
        getLocationsWindowMultiple(userOption, 1);
    }

    private void getLocationsWindowMultiple(String userOption, int numOfLocations){
        this.locationsWindow = locationsWindowUseCase.execute(userOption, 200, 200, numOfLocations);
        add(locationsWindow.getPanel());
        switchToWindow(LOCATIONS_WINDOW_NAME);
        System.out.println(locationsWindow.getName());
    }

    // Opens up a window to get the desired number of locations before applying it to the chosen use case
    private void showNumberOfLocationsWindow(){
        switchToWindow(NUMBER_LOCATIONS_WINDOW_NAME);
    }

    private void switchToWindow(String windowName){
        hideAllWindows();
        switch(windowName){
            case OPTIONS_NAME:
                userOptionsView.showWindow();
                this.setContentPane(userOptionsView.getWindow()); break;
            case LOCATIONS_WINDOW_NAME:
                locationsWindow.openWindow();
                this.setContentPane(locationsWindow.getPanel()); break;
            case NUMBER_LOCATIONS_WINDOW_NAME:
                numberLocationsView.showWindow();
                this.setContentPane(numberLocationsView.getWindow()); break;
            default:
                errorLocationsWindow.openWindow();
                this.setContentPane(errorLocationsWindow.getPanel()); break;
        }
    }

    private void hideAllWindows() {
        userOptionsView.hideWindow();
        if (locationsWindow != null) locationsWindow.hideWindow();
        numberLocationsView.hideWindow();
        errorLocationsWindow.hideWindow();
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
