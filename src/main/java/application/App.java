package application;

import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.DashboardUI;

import java.util.*;

public class App {
    public static void main(String[] args) {
        DashboardUI dashboardUI = new DashboardUI();
        String apiKey;
        OpenWeatherApiService apiService = new OpenWeatherApiService();
        boolean validKeyEntered = false;
        final Scanner scanner = new Scanner(System.in);

        while (!validKeyEntered) {
            System.out.println("Enter your OpenWeatherMap API key.");
            apiKey = scanner.nextLine();
            if (apiService.isAPIKeyValid(apiKey)) {
                dashboardUI.setAPIkey(apiKey);
                validKeyEntered = true;
            }
            else {
                System.out.println("Your last key was invalid. Please try again.\n");
            }
        }
        dashboardUI.runJFrame(apiService);
    }

}
