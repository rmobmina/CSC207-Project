package infrastructure.frameworks;

import java.util.Scanner;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetHistoricalWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetLocationsWindowUseCase;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.views.SelectNumberLocationsView;
import presentation.ui.windows.FavoritesManager;
import presentation.ui.dashboard.NewDashBoardUi;
import presentation.ui.views.HelpInfoView;
import presentation.ui.views.MainMenuView;
import presentation.ui.views.UserOptionsView;

/**
 * A class used to run the project.
 * Gets an OpenWeatherMap API key from the user before opening the main GUI.
 */
public class App {

    /**
     * The main entry point of the application.
     * This method retrieves the OpenWeatherMap API key from the user via the command line,
     * validates it, and then initializes the main graphical user interface (GUI).
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        String apiKey = "";
        final OpenWeatherApiService apiService = new OpenWeatherApiService();
        final GetLocationDataUseCase locationDataUseCase = new GetLocationDataUseCase(apiService);
        final GetForecastWeatherDataUseCase forecastWeatherDataUseCase = new GetForecastWeatherDataUseCase(apiService);
        final GetHistoricalWeatherDataUseCase historicalWeatherDataUseCase =
                new GetHistoricalWeatherDataUseCase(apiService);
        final NewDashBoardUi dashBoard =
                generateDashBoardUI(locationDataUseCase, forecastWeatherDataUseCase, historicalWeatherDataUseCase);

        // Loop to validate API key input
        boolean validKeyEntered = false;
        final Scanner scanner = new Scanner(System.in);

        while (!validKeyEntered) {
            System.out.println("Enter your OpenWeatherMap API key:");
            apiKey = scanner.nextLine();

            // Validate API key
            if (apiService.isApiKeyValid(apiKey)) {
                dashBoard.setAPIkey(apiKey);
                validKeyEntered = true;
            }
            else {
                System.out.println("Your last key was invalid. Please try again.\n");
            }
        }
        dashBoard.runJFrame(apiService);
    }

    private static NewDashBoardUi generateDashBoardUI(GetLocationDataUseCase locationDataUseCase,
                                                      GetForecastWeatherDataUseCase forecastWeatherDataUseCase,
                                                      GetHistoricalWeatherDataUseCase historicalWeatherDataUseCase) {
        return new NewDashBoardUi(
                new GetLocationsWindowUseCase(),
                locationDataUseCase,
                forecastWeatherDataUseCase,
                historicalWeatherDataUseCase,
                new FavoritesManager(),
                new UserOptionsView(),
                new MainMenuView(),
                new HelpInfoView()
        );
    }
}
