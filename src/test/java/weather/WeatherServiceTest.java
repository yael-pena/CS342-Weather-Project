package weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import weather.model.Location;
import weather.model.WeatherData;
import weather.provider.WeatherDataProvider;
import weather.service.WeatherComparison;
import weather.service.WeatherService;
import weather.service.WeatherSummary;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeatherServiceTest {

    private WeatherService service;

    @BeforeEach
    void setUp() {

        List<Location> locations = List.of(
                new Location("Chicago", "41.85", "-87.65"),
                new Location("Los Angeles", "34.05", "-118.24"),
                new Location("New York", "40.71", "-74.01")
        );

        WeatherDataProvider fakeProvider = new FakeWeatherProvider();

        service = new WeatherService(fakeProvider, locations);
    }

    // Test implementation of WeatherDataProvider.
    // This prevents our tests from accessing the real Open-Meteo API.
    private static class FakeWeatherProvider implements WeatherDataProvider {

        @Override
        public WeatherData getCurrentWeather(Location location) {

            return switch (location.city()) {
                case "Chicago" ->
                        new WeatherData(60.0, 70.0, 10.0);

                case "Los Angeles" ->
                        new WeatherData(80.0, 40.0, 5.0);

                case "New York" ->
                        new WeatherData(65.0, 60.0, 8.0);

                default ->
                        throw new IllegalArgumentException(
                                "No test weather for: " + location.city()
                        );
            };
        }
    }

    @Test
    void getLocationsReturnsKnownLocations() {
        List<Location> locations = service.getLocations();

        assertEquals(3, locations.size());
        assertEquals("Chicago", locations.get(0).city());
        assertEquals("Los Angeles", locations.get(1).city());
        assertEquals("New York", locations.get(2).city());
    }

    @Test
    void providerFailureThrowsException() {
        WeatherDataProvider failingProvider = location -> {
            throw new IllegalStateException("Provider failure");
        };

        List<Location> locations = List.of(
                new Location("Chicago", "41.85", "-87.65")
        );

        WeatherService failingService =
                new WeatherService(failingProvider, locations);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> failingService.getCurrentWeather("Chicago")
        );

        assertEquals("Provider failure", exception.getMessage());
    }

    @Test
    void findLocationIgnoresCapitalization() {
        Location location = service.findLocation("chicago");

        assertEquals("Chicago", location.city());
    }

    @Test
    void findLocationThrowsExceptionForUnknownLocation() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.findLocation("Gotham")
        );

        assertEquals("Unknown location: Gotham", exception.getMessage());
    }

    @Test
    void getCurrentWeatherReturnsWeatherFromProvider() {
        WeatherData weather = service.getCurrentWeather("Chicago");

        assertEquals(60.0, weather.temp());
        assertEquals(70.0, weather.humidity());
        assertEquals(10.0, weather.windSpeed());
    }

    @Test
    void compareReturnsWeatherForBothLocations() {
        WeatherComparison comparison =
                service.compare("Chicago", "New York");

        assertEquals("Chicago", comparison.firstLocation().city());
        assertEquals(60.0, comparison.firstWeather().temp());

        assertEquals("New York", comparison.secondLocation().city());
        assertEquals(65.0, comparison.secondWeather().temp());
    }

    @Test
    void summaryReturnsLocationAndWeather() {
        WeatherSummary summary = service.summary("Los Angeles");

        assertEquals("Los Angeles", summary.location().city());
        assertEquals(80.0, summary.weather().temp());
        assertEquals(40.0, summary.weather().humidity());
        assertEquals(5.0, summary.weather().windSpeed());
    }

    @Test
    void getWarmestLocationReturnsWarmestLocation() {
        WeatherSummary warmest = service.getWarmestLocation();

        assertEquals("Los Angeles", warmest.location().city());
        assertEquals(80.0, warmest.weather().temp());
    }
}