/**************************************************************************************************
 * CSC207 PROJECT: THE WEATHER APP
 *
 * Module Description
 * ===================
 *
 * This module is part of The Weather App project for CSC207: Software Design.
 * The code herein contributes to the overall functionality of the application and adheres
 * to design principles and documentation standards. Proper attribution is given where
 * external resources are used, and all functions include comprehensive explanations.
 *
 * Copyright and Usage Information
 * ================================
 *
 * This file is provided solely for the personal and private use of CSC207 students,
 * teaching staff, and the Department of Computer Science at the University of Toronto
 * St. George campus. Any form of distribution of this code, whether as provided or
 * with modifications, is strictly prohibited.
 *
 * This file is Copyright (c) Akram Klai, Reena Obmina, Edison Yao, Bader Aleisari, Arham Mohammed.
 **************************************************************************************************/

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
            if (apiService.isApiKeyValid(apiKey)) {
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
