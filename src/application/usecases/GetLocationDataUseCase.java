package application.usecases;

import domain.entities.Location;
import domain.interfaces.ApiService;

public class GetLocationDataUseCase {
    private final ApiService apiService;

    public GetLocationDataUseCase(ApiService apiService) {
        this.apiService = apiService;
    }

    public Location execute(String city, String apiKey) {
        return apiService.fetchLocation(city, apiKey);
    }
}
