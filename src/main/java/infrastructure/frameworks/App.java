package infrastructure.frameworks;

import java.util.Scanner;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetHistoricalWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetLocationsWindowUseCase;
import infrastructure.adapters.OpenWeatherApiService;
import org.jetbrains.annotations.NotNull;
import presentation.ui.NewDashBoardUi;
import presentation.ui.views.SelectNumberLocationsView;
import presentation.ui.views.UserOptionsView;

/**
 * A class used to run the project.
 * Gets an OpenWeatherMap API key from the user before opening the main GUI.
 */
public class App {

    /**
     * The main method of the App class.
     * Runs the project by getting the user's API key and launching the rest of the project.
     * @param args an Array of String objects representing arguments (not used)
     */
    public static void main(String[] args) {
        // instantiates important variables
        String apiKey;
        final OpenWeatherApiService apiService = new OpenWeatherApiService();
        GetLocationDataUseCase locationDataUseCase = new GetLocationDataUseCase(apiService);
        GetForecastWeatherDataUseCase forecastWeatherDataUseCase = new GetForecastWeatherDataUseCase(apiService);
        GetHistoricalWeatherDataUseCase historicalWeatherDataUseCase = new GetHistoricalWeatherDataUseCase(apiService);
        final NewDashBoardUi dashBoard = generateDashBoardUI(locationDataUseCase, forecastWeatherDataUseCase, historicalWeatherDataUseCase);
        boolean validKeyEntered = false;
        final Scanner scanner = new Scanner(System.in);

        // while the key we have is not usable, keep bugging the user for one
        while (!validKeyEntered) {
            System.out.println("Enter your OpenWeatherMap API key.");
            apiKey = scanner.nextLine();

            // OpenWeatherApiService objects have a method to check the validity of the key
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
        return new NewDashBoardUi(new GetLocationsWindowUseCase(),
                locationDataUseCase, forecastWeatherDataUseCase, historicalWeatherDataUseCase,
                new UserOptionsView(), new SelectNumberLocationsView());
    }
}
