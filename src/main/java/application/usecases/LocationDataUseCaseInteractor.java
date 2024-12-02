package application.usecases;

import domain.entities.Location;

import java.util.List;

public class LocationDataUseCaseInteractor {

    private GetLocationDataUseCase locationDataUseCase;

    public LocationDataUseCaseInteractor(GetLocationDataUseCase locationDataUseCase) {
        this.locationDataUseCase = locationDataUseCase;
    }

    public List<Location> getLocations(String city, String apiKey){
        return this.locationDataUseCase.execute(city, apiKey);
    }
}
