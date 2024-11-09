package infrastructure.frameworks;

import infrastructure.adapters.OpenWeatherApiService;
import domain.entities.Location;
import domain.entities.WeatherData;
import application.usecases.GetLocationDataUseCase;
import application.usecases.GetWeatherDataUseCase;
import org.json.JSONException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        OpenWeatherApiService apiService = new OpenWeatherApiService();
        GetLocationDataUseCase locationUseCase = new GetLocationDataUseCase(apiService);
        GetWeatherDataUseCase weatherUseCase = new GetWeatherDataUseCase(apiService);

        Scanner scanner = new Scanner(System.in);
        System.out.println("(Type 'quit' to exit) Enter a city:");
        String city = scanner.nextLine();
        if ("quit".equals(city)) {
            System.exit(0);
        }

        System.out.println("Enter your OpenWeatherMap API key:");
        String apiKey = scanner.nextLine();

        Location location = locationUseCase.execute(city, apiKey);
        if (location != null) {
            WeatherData weatherData = weatherUseCase.execute(location, apiKey);
            if (weatherData != null) {
                try {
                    System.out.println("\nWeather Data:\n" + weatherData.getWeatherDetails().toString(4));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Error: Could not retrieve weather data.");
            }
        } else {
            System.out.println("Error: Could not retrieve location data.");
        }
        scanner.close();
    }
}
