package infrastructure.frameworks;

import java.util.Scanner;

import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.DashboardUI;

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

        final DashboardUI dashboardUi = new DashboardUI();
        String apiKey;
        final OpenWeatherApiService apiService = new OpenWeatherApiService();
        boolean validKeyEntered = false;
        final Scanner scanner = new Scanner(System.in);

        while (!validKeyEntered) {
            System.out.println("Enter your OpenWeatherMap API key.");
            apiKey = scanner.nextLine();
            if (apiService.isApiKeyValid(apiKey)) {
                dashboardUi.setAPIkey(apiKey);
                validKeyEntered = true;
            }
            else {
                System.out.println("Your last key was invalid. Please try again.\n");
            }
        }
        dashboardUi.runJFrame(apiService);
    }

}
