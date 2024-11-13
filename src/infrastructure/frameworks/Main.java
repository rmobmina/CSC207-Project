package infrastructure.frameworks;

// importing needed tools
import java.time.LocalDate;
import java.util.Scanner;

import org.json.JSONException;

import application.usecases.GetLocationDataUseCase;
import application.usecases.GetWeatherDataUseCase;
import domain.entities.Location;
import domain.entities.WeatherData;
import infrastructure.adapters.OpenWeatherApiService;

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
        final Scanner scanner = new Scanner(System.in);

        while (true) {

            // get user inputs for locations and API keys
            System.out.println("(Type 'quit' to exit) Enter a city:");
            final String city = scanner.nextLine();
            if ("quit".equals(city)) {
                System.exit(0);
            }

            if ("".equals(apiKey)) {
                System.out.println("Enter your OpenWeatherMap API key.");
                apiKey = scanner.nextLine();
            }

            LocalDate startDate = LocalDate.of(2024, 9, 11);
            LocalDate endDate = LocalDate.of(2024, 9, 12);

            // we run the location use case given this information
            final Location location = locationUseCase.execute(city, apiKey);

            if (location != null) {

                // given that the information is valid, we then find weather details -
                final WeatherData weatherData = weatherUseCase.execute(location, startDate, endDate);
                if (weatherData != null) {

                    // - and print it into the console
                    try {
                        System.out.println("\nWeather Data:\n"
                                + weatherData.getWeatherDetails().toString(indentAmount));
                    }

                    // the rest of the code from here simply accounts for errors
                    catch (JSONException exception) {
                        exception.printStackTrace();
                    }

                }
                else {
                    System.out.println("Error: Could not retrieve weather data.");
                }
            }
            else {
                System.out.println("Error: Could not retrieve location data.");
            }
        }
    }
}


