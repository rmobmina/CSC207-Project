package presentation.ui.dashboard;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetHistoricalWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetLocationsWindowUseCase;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.windows.FavoritesManager;
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
    MainMenuView mainMenuView;
    HelpInfoView helpInfoView;

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
                          FavoritesManager favouriteManager,
                          UserOptionsView userOptionsView, SelectNumberLocationsView numberLocationsView,
                          MainMenuView mainMenuView, HelpInfoView helpInfoView) {

        initVariables(locationsWindowUseCase, locationDataUseCase,
                forecastWeatherDataUseCase, historicalWeatherDataUseCase, favouriteManager,
                userOptionsView, numberLocationsView, mainMenuView, helpInfoView);

        setTitle("Weather Dashboard");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(mainMenuView.getPanel());
        switchToWindow("MainMenu");

        setButtonListeners();
    }


    private void initVariables(GetLocationsWindowUseCase getLocationsWindowUseCase,
                               GetLocationDataUseCase locationDataUseCase,
                               GetForecastWeatherDataUseCase forecastWeatherDataUseCase,
                               GetHistoricalWeatherDataUseCase historicalWeatherDataUseCase,
                               FavoritesManager favoritesManager,
                               UserOptionsView userOptionsView,
                               SelectNumberLocationsView numberLocationsView,
                               MainMenuView mainMenuView,
                               HelpInfoView helpInfoView) {
        this.locationsWindowUseCase = getLocationsWindowUseCase;
        this.locationDataUseCase = locationDataUseCase;
        this.forecastWeatherDataUseCase = forecastWeatherDataUseCase;
        this.historicalWeatherDataUseCase = historicalWeatherDataUseCase;
        this.favoritesManager = favoritesManager;
        this.userOptionsView = userOptionsView;
        this.numberLocationsView = numberLocationsView;
        this.mainMenuView = mainMenuView;
        this.helpInfoView = helpInfoView;
    }

    private void setButtonListeners() {
        mainMenuView.setStartActionListener(e -> {
            switchToWindow(OPTIONS_NAME); // Switch to the dashboard
        });

        mainMenuView.setHelpActionListener(e -> {
            if (helpInfoView != null) {
                helpInfoView.display(); // Open the help view
            }
        });

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
            new infrastructure.frameworks.MercatorDisplayApp().startMercatorMap(apiKey, locationDataUseCase, (OpenWeatherApiService) apiService, 500, 500);
        });

        numberLocationsView.setActionListener(e -> getLocationsWindowMultiple(
                userOption, locationsWindowWidth, locationsWindowHeight, numberLocationsView.getNumOfLocations()));
    }

    private void getLocationsWindow(String userOption, int width, int height) {
        getLocationsWindowMultiple(userOption, width, height, 1);
    }

    private void getLocationsWindowMultiple(String userOption, int width, int height, int numOfLocations) {
        locationsWindow = locationsWindowUseCase.execute(
                userOption,
                new int[]{900, 600},
                2,
                locationDataUseCase,
                apiKey,
                apiService,
                this
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
            case "MainMenu":
                mainMenuView.showPanel();
                this.setContentPane(mainMenuView.getPanel());
                break;
            case OPTIONS_NAME:
                userOptionsView.showPanel();
                this.setContentPane(userOptionsView.getPanel());
                break;
            case LOCATIONS_WINDOW_NAME:
                locationsWindow.openWindow();
                toggleShowDashBoard(false);
                break;
            case NUMBER_LOCATIONS_WINDOW_NAME:
                numberLocationsView.showPanel();
                this.setContentPane(numberLocationsView.getPanel());
                break;
            default:
                throw new IllegalArgumentException("Unknown window: " + windowName);
        }
    }

    // Closes the current locations window and opens up the dashboard window
    public void backToDashBoard() {
        toggleShowDashBoard(true);
        switchToWindow(OPTIONS_NAME);
    }

    private void toggleShowDashBoard(boolean toggle) {
        this.setVisible(toggle);
    }

    private void hideAllWindows() {
        mainMenuView.hidePanel();
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
