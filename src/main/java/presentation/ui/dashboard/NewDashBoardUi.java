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
import presentation.ui.windows.VisualizationUI;

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
    FavoritesManager favoritesManager;
    MainMenuView mainMenuView;
    HelpInfoView helpInfoView;

    final String OPTIONS_NAME = "Options";
    final String LOCATIONS_WINDOW_NAME = "Locations";
    final String MAIN_MENU_NAME = "Main Menu";
    final String HELP_INFO_NAME = "Help Info";

    final int locationsWindowWidth = 910;
    final int locationsWindowHeight = 350;

    private String userOption;

    public NewDashBoardUi(GetLocationsWindowUseCase locationsWindowUseCase,
                          GetLocationDataUseCase locationDataUseCase,
                          GetForecastWeatherDataUseCase forecastWeatherDataUseCase,
                          GetHistoricalWeatherDataUseCase historicalWeatherDataUseCase,
                          FavoritesManager favouriteManager,
                          UserOptionsView userOptionsView,
                          MainMenuView mainMenuView, HelpInfoView helpInfoView) {

        initVariables(locationsWindowUseCase, locationDataUseCase,
                forecastWeatherDataUseCase, historicalWeatherDataUseCase, favouriteManager,
                userOptionsView, mainMenuView);

        setTitle("Weather Dashboard");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(userOptionsView.getPanel());

        switchToWindow(MAIN_MENU_NAME);

        setButtonListeners();

    }

    private void initVariables(GetLocationsWindowUseCase getLocationsWindowUseCase,
                               GetLocationDataUseCase locationDataUseCase,
                               GetForecastWeatherDataUseCase forecastWeatherDataUseCase,
                               GetHistoricalWeatherDataUseCase historicalWeatherDataUseCase,
                               FavoritesManager favoritesManager,
                               UserOptionsView userOptionsView,
                               MainMenuView mainMenuView) {
        this.locationsWindowUseCase = getLocationsWindowUseCase;
        this.locationDataUseCase = locationDataUseCase;
        this.forecastWeatherDataUseCase = forecastWeatherDataUseCase;
        this.historicalWeatherDataUseCase = historicalWeatherDataUseCase;
        this.favoritesManager = favoritesManager;
        this.userOptionsView = userOptionsView;
        this.mainMenuView = mainMenuView;
        this.helpInfoView = new HelpInfoView(); // Initialize HelpInfoView here
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
            userOption = HistoricalWeatherComparisonView.OPTION_NAME;
            getLocationsWindowMultiple(
                    userOption, locationsWindowWidth, locationsWindowHeight, 2);
        });

        // Add Mercator Map action
        userOptionsView.setMercatorMapActionListener(e -> {
            // Open Mercator Map directly
//            userOption = MercatorMapView.OPTION_NAME;
//            getLocationsWindowMultiple(
//                    userOption, locationsWindowWidth, locationsWindowHeight, 2);
            new infrastructure.frameworks.MercatorDisplayApp().startMercatorMap(apiKey, locationDataUseCase,
                    (OpenWeatherApiService) apiService, locationsWindowWidth, locationsWindowHeight);
        });

        mainMenuView.setStartActionListener(eListener -> backToDashBoard());
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

    private void switchToWindow(String windowName) {
        hideAllWindows();
        switch (windowName) {
            case OPTIONS_NAME:
                userOptionsView.showPanel();
                this.setContentPane(userOptionsView.getPanel());
                break;
            case MAIN_MENU_NAME:
                mainMenuView.showPanel();
                this.setContentPane(mainMenuView.getPanel());
                break;
            case LOCATIONS_WINDOW_NAME:
                locationsWindow.openWindow();
                toggleShowDashBoard(false);
                // closes the current window
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
        mainMenuView.hidePanel();
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
