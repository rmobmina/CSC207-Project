package infrastructure.frameworks;

// importing needed tools
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;

import application.usecases.GetLocationDataUseCase;
import application.usecases.GetWeatherDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;
import presentation.ui.LocationsWindow;
import presentation.ui.LocationsWindowGenerator;

/**
 * Main class for the weather application.
 * This class serves as the entry point for fetching and displaying weather data.
 */
public class Main {

    /**
     * Main method that runs the weather application.
     *
     * @param args command-line arguments, not used in this application
     */
    public static void main(String[] args) {
        // initializing varibles
        final OpenWeatherApiService apiService = new OpenWeatherApiService();
        final GetLocationDataUseCase locationUseCase = new GetLocationDataUseCase(apiService);
        final GetWeatherDataUseCase weatherUseCase = new GetWeatherDataUseCase(apiService);
        String apiKey = "";
        final int indentAmount = 4;
        List<Location> validLocations = new ArrayList<>();
        final Scanner scanner = new Scanner(System.in);

        while (true) {

            // get user inputs for locations and API keys
            System.out.println("(Type 'quit' to exit) Enter a city or multiple cities: (type ',' in between cities)");
            final String cities = scanner.nextLine();
            if ("quit".equals(cities)) {
                System.exit(0);
            }

            String[] citiesArr = cities.trim().split(",");
            for (int i = 0; i < citiesArr.length; i++) {
                citiesArr[i] = citiesArr[i].trim();
            }

            if ("".equals(apiKey)) {
                System.out.println("Enter your OpenWeatherMap API key.");
                apiKey = scanner.nextLine();
            }

            LocalDate startDate = LocalDate.of(2024, 9, 11);
            LocalDate endDate = LocalDate.of(2024, 9, 12);

            // Check whether each city is valid and store it in the array list
            validLocations = getValidLocations(citiesArr, locationUseCase, apiKey);

            if (validLocations.size() != 1) {
                // If not only 1 location is valid, then generate a new locations window based on how many
                // valid locations there are
                LocationsWindow window = LocationsWindowGenerator.generateLocationsWindow(validLocations);
                System.out.println(window.getWindow().getName());
            }
            else {
                System.out.println("Only 1 valid location found");
            }

//          if (location != null) {
//
//                // given that the information is valid, we then find weather details -
//                final WeatherData weatherData = weatherUseCase.execute(location, startDate, endDate);
//                if (weatherData != null) {
//
//                    // - and print it into the console
//                    try {
//                        System.out.println("\nWeather Data:\n"
//                                + weatherData.getWeatherDetails().toString(indentAmount));
//                    }
//
//                    // the rest of the code from here simply accounts for errors
//                    catch (JSONException exception) {
//                        exception.printStackTrace();
//                    }
//
//                }
//                else {
//                    System.out.println("Error: Could not retrieve weather data.");
//                }
//            }
//            else {
//                System.out.println("Error: Could not retrieve location data.");
//            }
        }
    }

    public static List<Location> getValidLocations(String[] cities, GetLocationDataUseCase locationUseCase, String apiKey){
        // we run the location use case given this information
        ArrayList<Location> validLocations = new ArrayList<>();
        for (String city : cities) {
            Location location = locationUseCase.execute(city, apiKey);
            if (location != null) {
                validLocations.add(location);
            }
        }
        return validLocations;
    }
}


