package presentation.ui.dashboard;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetHistoricalWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetLocationsWindowUseCase;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.FavoritesManager;
import presentation.ui.views.*;
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
    FavoritesManager favoritesManager;

    final String OPTIONS_NAME = "Options";
    final String LOCATIONS_WINDOW_NAME = "Locations";
    final String NUMBER_LOCATIONS_WINDOW_NAME = "Number Locations";

    final int locationsWindowWidth = 710;
    final int locationsWindowHeight = 350;

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
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        favoritesManager = new FavoritesManager(); // Initialized FavoritesManager
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
                               SelectNumberLocationsView numberLocationsView) {
        this.locationsWindowUseCase = getLocationsWindowUseCase;
        this.locationDataUseCase = locationDataUseCase;
        this.forecastWeatherDataUseCase = forecastWeatherDataUseCase;
        this.historicalWeatherDataUseCase = historicalWeatherDataUseCase;
        this.userOptionsView = userOptionsView;
        this.numberLocationsView = numberLocationsView;
    }

    private void setButtonListeners() {
        userOptionsView.setForecastHourlyActionListener(e -> {
            getLocationsWindow(ForecastHourlyView.OPTION_NAME, locationsWindowWidth, locationsWindowHeight);
            userOptionsView.hideForecastOptionsWindow();
        });

        userOptionsView.setForecastDailyActionListener(e -> {
            getLocationsWindow(ForecastDailyView.OPTION_NAME, locationsWindowWidth, locationsWindowHeight);
            userOptionsView.hideForecastOptionsWindow();
        });

        userOptionsView.setComparisonActionListener(e -> {
            // Ask user to enter how many locations they want
            showNumberOfLocationsWindow();
            userOption = HistoricalWeatherComparisonView.OPTION_NAME;
        });

        // Add Mercator Map action
        userOptionsView.setMercatorMapActionListener(e -> {
            // Open Mercator Map directly
            new infrastructure.frameworks.MercatorDisplayApp().startMercatorMap(apiKey, locationDataUseCase, (OpenWeatherApiService) apiService);
        });

        numberLocationsView.setActionListener(e -> getLocationsWindowMultiple(
                userOption, locationsWindowWidth, locationsWindowHeight, numberLocationsView.getNumOfLocations()));
    }

    private void getLocationsWindow(String userOption, int width, int height) {
        getLocationsWindowMultiple(userOption, width, height, 1);
    }

    private void getLocationsWindowMultiple(String userOption, int width, int height, int numOfLocations) {
        this.locationsWindow = locationsWindowUseCase.execute(
                userOption, new int[]{width, height}, numOfLocations, locationDataUseCase, apiKey, apiService
        );

        // Corrected button listeners
        locationsWindow.setFavoritesButtonListener(e -> openFavoritesView());
        locationsWindow.setAddToFavoritesButtonListener(favoritesManager);
        locationsWindow.setBackButtonListener(e -> backToDashBoard());
        switchToWindow(LOCATIONS_WINDOW_NAME);
    }

    private void openFavoritesView() {
        // Pass `this` as the parent window (LocationsWindow is a subclass of JFrame)
        FavoritesView favoritesView = new FavoritesView(favoritesManager, apiKey, locationsWindow);
        favoritesView.setVisible(true);
    }

    // Opens up a window to get the desired number of locations before applying it to the chosen use case
    private void showNumberOfLocationsWindow() {
        switchToWindow(NUMBER_LOCATIONS_WINDOW_NAME);
    }

    private void switchToWindow(String windowName) {
        hideAllWindows();
        switch (windowName) {
            case OPTIONS_NAME:
                userOptionsView.showPanel();
                this.setContentPane(userOptionsView.getPanel());
                break;
            case LOCATIONS_WINDOW_NAME:
                locationsWindow.openWindow();
                toggleShowDashBoard(false);
                break; // closes the current window
            case NUMBER_LOCATIONS_WINDOW_NAME:
                numberLocationsView.showPanel();
                this.setContentPane(numberLocationsView.getPanel());
                break;
            default:
        }
    }

    // Closes the current locations window and opens up the dashboard window
    private void backToDashBoard() {
        toggleShowDashBoard(true);
        switchToWindow(OPTIONS_NAME);
    }

    private void toggleShowDashBoard(boolean toggle) {
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

    public UserOptionsView getOptionsView() {
        return userOptionsView;
    }

    public void setAPIkey(String apiKey) {
        this.apiKey = apiKey;
    }

    // added a getter to retrieve API key for favorites
    public String getAPIkey() {
        return this.apiKey;
    }
}
