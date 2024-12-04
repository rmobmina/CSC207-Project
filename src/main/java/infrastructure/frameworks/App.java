package infrastructure.frameworks;

import java.util.Scanner;

import application.usecases.GetLocationDataUseCase;
import application.usecases.GetLocationsWindowUseCase;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.dashboard.NewDashBoardUi;
import presentation.ui.views.HelpInfoView;
import presentation.ui.views.MainMenuView;
import presentation.ui.views.UserOptionsView;
import presentation.ui.windows.FavoritesManager;

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
        final NewDashBoardUi dashBoard =
                generateDashBoardUi(locationDataUseCase);

        // Loop to validate API key input
        boolean validKeyEntered = false;
        final Scanner scanner = new Scanner(System.in);

        while (!validKeyEntered) {
            System.out.println("Enter your OpenWeatherMap API key:");
            apiKey = scanner.nextLine();

            // Validate API key
            if (apiService.isApiKeyValid(apiKey)) {
                dashBoard.setApikey(apiKey);
                validKeyEntered = true;
            }
            else {
                System.out.println("Your last key was invalid. Please try again.\n");
            }
        }
        dashBoard.runJframe(apiService);
    }

    /**
     * Generates a new instance of {@link NewDashBoardUi}.
     *
     * @param locationDataUseCase The use case interactor for handling location data.
     * @return A new instance of the main dashboard UI.
     */
    private static NewDashBoardUi generateDashBoardUi(GetLocationDataUseCase locationDataUseCase) {
        return new NewDashBoardUi(
                new GetLocationsWindowUseCase(),
                locationDataUseCase,
                new FavoritesManager(),
                new UserOptionsView(),
                new MainMenuView(),
                new HelpInfoView()
        );
    }
}
