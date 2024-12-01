//package domain.entities;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public class WeatherDataTest {
//
//    @Test
//    public void testWeatherDataCreation() {
//        List<String> alerts = Arrays.asList("Storm Warning");
//        WeatherData data = new WeatherData("New York", LocalDate.now(), 23.5, 65, 10.5, 2.3, alerts);
//
//        assertEquals("New York", data.getLocation());
//        assertEquals(23.5, data.getTemperature());
//        assertEquals(65, data.getHumidity());
//        assertTrue(data.getAlerts().contains("Storm Warning"));
//    }
//}
