package presentation.cli;

import domain.entities.Location;
import domain.entities.WeatherData;
import domain.services.WeatherService;
import infrastructure.apis.OpenWeatherService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class WeatherApp {
    private final WeatherService weatherService;

    public WeatherApp(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter city: ");
        String city = scanner.nextLine();
        System.out.print("Enter country: ");
        String country = scanner.nextLine();

        Location location = new Location(city, 0.0, 0.0, country);

        WeatherData currentWeather = weatherService.getCurrentWeather(location);
        System.out.println("Current weather: " + currentWeather);

        System.out.print("View historical data? (yes/no): ");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {
            System.out.print("Enter start date (yyyy-mm-dd): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());
            System.out.print("Enter end date (yyyy-mm-dd): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine());

            List<WeatherData> historicalData = weatherService.getHistoricalWeather(location, startDate, endDate);
            historicalData.forEach(System.out::println);
        }

        scanner.close();
    }

    public static void main(String[] args) {
        WeatherService weatherService = new OpenWeatherService();
        WeatherApp app = new WeatherApp(weatherService);
        app.start();
    }
}
