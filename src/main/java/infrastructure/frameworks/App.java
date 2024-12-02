package infrastructure.frameworks;

import java.util.Scanner;

import application.usecases.GetForecastWeatherDataUseCase;
import application.usecases.GetHistoricalWeatherDataUseCase;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetLocationsWindowUseCase;
import infrastructure.adapters.OpenWeatherApiService;
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
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        // Initialize apiKey with a default value
        String apiKey = "";
        final OpenWeatherApiService apiService = new OpenWeatherApiService();
        GetLocationDataUseCase locationDataUseCase = new GetLocationDataUseCase(apiService);
        GetForecastWeatherDataUseCase forecastWeatherDataUseCase = new GetForecastWeatherDataUseCase(apiService);
        GetHistoricalWeatherDataUseCase historicalWeatherDataUseCase = new GetHistoricalWeatherDataUseCase(apiService);
        final NewDashBoardUi dashBoard = generateDashBoardUI(locationDataUseCase, forecastWeatherDataUseCase, historicalWeatherDataUseCase);

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
            } else {
                System.out.println("Your last key was invalid. Please try again.\n");
            }
        }

        // Set action listeners for Mercator map
        setupMercatorMapIntegration(dashBoard, apiKey, locationDataUseCase, apiService);

        // Launch the main dashboard
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
                new UserOptionsView(),
                new SelectNumberLocationsView()
        );
    }

    private static void setupMercatorMapIntegration(NewDashBoardUi dashBoard, String apiKey,
                                                    GetLocationDataUseCase locationDataUseCase,
                                                    OpenWeatherApiService apiService) {
        dashBoard.getOptionsView().setMercatorMapActionListener(e -> {
            new MercatorDisplayApp().startMercatorMap(apiKey, locationDataUseCase, apiService);
        });
    }
}
