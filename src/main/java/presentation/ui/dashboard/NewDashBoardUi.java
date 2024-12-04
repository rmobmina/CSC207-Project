package presentation.ui.dashboard;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import application.usecases.GetLocationDataUseCase;
import application.usecases.GetLocationsWindowUseCase;
import domain.interfaces.ApiService;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.views.FavoritesView;
import presentation.ui.views.ForecastDailyView;
import presentation.ui.views.ForecastHourlyView;
import presentation.ui.views.HelpInfoView;
import presentation.ui.views.HistoricalWeatherComparisonView;
import presentation.ui.views.MainMenuView;
import presentation.ui.views.UserOptionsView;
import presentation.ui.windows.FavoritesManager;
import presentation.ui.windows.LocationsWindow;

/**
 * Class that handles UI to display a specific view panel (one for each use case) based on the user option
 * and an options view panel to receive the user's input.
 */
public class NewDashBoardUi extends JFrame {
    private static final String OPTIONS_NAME = "Options";
    private static final String LOCATIONS_WINDOW_NAME = "Locations";
    private static final String MAIN_MENU_NAME = "Main Menu";
    private static final int LOCATIONS_WINDOW_WIDTH = 900;
    private static final int LOCATIONS_WINDOW_HEIGHT = 700;

    private String apiKey = "";
    private ApiService apiService;
    private GetLocationsWindowUseCase locationsWindowUseCase;
    private GetLocationDataUseCase locationDataUseCase;

    private LocationsWindow locationsWindow;
    private UserOptionsView userOptionsView;
    private FavoritesManager favoritesManager;
    private MainMenuView mainMenuView;
    private HelpInfoView helpInfoView;

    private String userOption;

    public NewDashBoardUi(GetLocationsWindowUseCase locationsWindowUseCase,
                          GetLocationDataUseCase locationDataUseCase,
                          FavoritesManager favouriteManager,
                          UserOptionsView userOptionsView,
                          MainMenuView mainMenuView, HelpInfoView helpInfoView) {

        initVariables(locationsWindowUseCase, locationDataUseCase, favouriteManager,
                userOptionsView, mainMenuView, helpInfoView);

        setTitle("Weather Dashboard");
        final int dashboardWindowWidth = 500;
        final int dashboardWindowHeight = 700;
        setSize(dashboardWindowWidth, dashboardWindowHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(mainMenuView.getPanel());
        switchToWindow(MAIN_MENU_NAME);

        setButtonListeners();
    }

    private void initVariables(GetLocationsWindowUseCase getLocationsWindowInit,
                               GetLocationDataUseCase locationDataInit,
                               FavoritesManager favoritesManagerInit,
                               UserOptionsView userOptionsViewInit,
                               MainMenuView mainMenuViewInit,
                               HelpInfoView helpInfoInit) {
        this.locationsWindowUseCase = getLocationsWindowInit;
        this.locationDataUseCase = locationDataInit;
        this.favoritesManager = favoritesManagerInit;
        this.userOptionsView = userOptionsViewInit;
        this.mainMenuView = mainMenuViewInit;
        this.helpInfoView = helpInfoInit;
    }

    private void setButtonListeners() {
        mainMenuView.setStartActionListener(actionEvent -> {
            // Switch to the dashboard
            switchToWindow(OPTIONS_NAME);
        });

        mainMenuView.setHelpActionListener(actionEvent -> {
            if (helpInfoView != null) {
                // Open the help view
                helpInfoView.display();
            }
        });

        userOptionsView.setForecastHourlyActionListener(actionEvent -> {
            userOption = ForecastHourlyView.OPTION_NAME;
            getLocationsWindow();
            userOptionsView.hideForecastOptionsWindow();
        });

        userOptionsView.setForecastDailyActionListener(actionEvent -> {
            userOption = ForecastDailyView.OPTION_NAME;
            getLocationsWindow();
            userOptionsView.hideForecastOptionsWindow();
        });

        userOptionsView.setComparisonActionListener(actionEvent -> {
            userOption = HistoricalWeatherComparisonView.OPTION_NAME;
            getLocationsWindowMultiple(2);
        });

        // Add Mercator Map action
        userOptionsView.setMercatorMapActionListener(actionEvent -> {
            // Open Mercator Map directly
            new infrastructure.frameworks.MercatorDisplayApp().startMercatorMap(apiKey, locationDataUseCase,
                    (OpenWeatherApiService) apiService, LOCATIONS_WINDOW_WIDTH, LOCATIONS_WINDOW_HEIGHT);
        });
    }

    private void getLocationsWindow() {
        getLocationsWindowMultiple(1);
    }

    private void getLocationsWindowMultiple(int numOfLocations) {
        locationsWindow = locationsWindowUseCase.execute(
                userOption,
                new int[]{LOCATIONS_WINDOW_WIDTH, LOCATIONS_WINDOW_HEIGHT},
                numOfLocations,
                locationDataUseCase,
                apiKey,
                apiService,
                this
        );

        // Corrected button listeners
        locationsWindow.setFavoritesButtonListener(actionEvent -> openFavoritesView());
        locationsWindow.setAddToFavoritesButtonListener(favoritesManager);
        locationsWindow.setBackButtonListener(actionEvent -> backToDashBoard());
        switchToWindow(LOCATIONS_WINDOW_NAME);
    }

    private void openFavoritesView() {
        // Pass `this` as the parent window (LocationsWindow is a subclass of JFrame)
        final FavoritesView favoritesView = new FavoritesView(favoritesManager, apiKey, locationsWindow);
        favoritesView.setVisible(true);
    }

    private void switchToWindow(String windowName) {
        hideAllWindows();
        switch (windowName) {
            case MAIN_MENU_NAME:
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
            default:
                throw new IllegalArgumentException("Unknown window: " + windowName);
        }
    }

    /**
     * Closes the current locations window and opens up the dashboard window.
     */
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
        mainMenuView.hidePanel();
        if (locationsWindow != null) {
            locationsWindow.hideWindow();
        }
    }

    /**
     * Runs the JFrame of the DashboardUI and displays the current panel (view).
     * @param weatherApiService the weather api service being used by use cases in the views
     */
    public void runJframe(OpenWeatherApiService weatherApiService) {
        apiService = weatherApiService;
        this.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }

    public UserOptionsView getOptionsView() {
        return userOptionsView;
    }

    public void setApikey(String newApiKey) {
        this.apiKey = newApiKey;
    }

    // added a getter to retrieve API key for favorites
    public String getApikey() {
        return this.apiKey;
    }
}
