package application.usecases;

import domain.entities.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConvertToMercatorUseCaseTest {

    private final ConvertToMercatorUseCase useCase = new ConvertToMercatorUseCase();

    @Test
    void testNormalCase() {
        Coordinate coordinate = new Coordinate(45.0, -90.0);
        double mapWidth = 1024.0;
        double mapHeight = 512.0;

        double[] result = useCase.execute(coordinate, mapWidth, mapHeight);

        assertEquals(256.0, result[0], 0.01, "x-coordinate is incorrect");
        assertEquals(112.35, result[1], 0.01, "y-coordinate is incorrect");
    }

    @Test
    void testEquatorAndPrimeMeridian() {
        Coordinate coordinate = new Coordinate(0.0, 0.0);
        double mapWidth = 1024.0;
        double mapHeight = 512.0;

        double[] result = useCase.execute(coordinate, mapWidth, mapHeight);

        assertEquals(512.0, result[0], 0.01, "x-coordinate is incorrect");
        assertEquals(256.0, result[1], 0.01, "y-coordinate is incorrect");
    }

    @Test
    void testNorthPole() {
        Coordinate coordinate = new Coordinate(90.0, 0.0);
        double mapWidth = 1024.0;
        double mapHeight = 512.0;

        double[] result = useCase.execute(coordinate, mapWidth, mapHeight);

        assertEquals(512.0, result[0], 0.01, "x-coordinate is incorrect");
        assertTrue(result[1] < 0, "y-coordinate should be off the map for the North Pole");
    }

    @Test
    void testSouthPole() {
        Coordinate coordinate = new Coordinate(-90.0, 0.0);
        double mapWidth = 1024.0;
        double mapHeight = 512.0;

        double[] result = useCase.execute(coordinate, mapWidth, mapHeight);

        assertEquals(512.0, result[0], 0.01, "x-coordinate is incorrect");
        assertTrue(result[1] > mapHeight, "y-coordinate should be off the map for the South Pole");
    }

    @Test
    void testInternationalDateLinePositive() {
        Coordinate coordinate = new Coordinate(0.0, 180.0);
        double mapWidth = 1024.0;
        double mapHeight = 512.0;

        double[] result = useCase.execute(coordinate, mapWidth, mapHeight);

        assertEquals(1024.0, result[0], 0.01, "x-coordinate is incorrect");
        assertEquals(256.0, result[1], 0.01, "y-coordinate is incorrect");
    }

    @Test
    void testInternationalDateLineNegative() {
        Coordinate coordinate = new Coordinate(0.0, -180.0);
        double mapWidth = 1024.0;
        double mapHeight = 512.0;

        double[] result = useCase.execute(coordinate, mapWidth, mapHeight);

        assertEquals(0.0, result[0], 0.01, "x-coordinate is incorrect");
        assertEquals(256.0, result[1], 0.01, "y-coordinate is incorrect");
    }

}
