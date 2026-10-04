package weather;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import weather.cli.WeatherCLI;
import weather.model.Location;
import weather.model.WeatherData;
import weather.provider.WeatherDataProvider;
import weather.service.WeatherService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WeatherCLITest {

    private WeatherService service;

    private InputStream originalIn;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Save the real System.in and System.out so we can restore them later.
        originalIn = System.in;
        originalOut = System.out;

        List<Location> locations = List.of(
                new Location("Chicago", "41.85", "-87.65"),
                new Location("Los Angeles", "34.05", "-118.24"),
                new Location("New York", "40.71", "-74.01")
        );

        WeatherDataProvider fakeProvider = new FakeWeatherProvider();

        service = new WeatherService(fakeProvider, locations);
    }

    @AfterEach
    void tearDown() {
        // Restore normal input and output after every test.
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    // Controlled test implementation.
    // No real Open-Meteo API is used during these tests.
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

    private String runCLI(String input) {
        // Pretend this string was typed into the keyboard.
        System.setIn(
                new ByteArrayInputStream(input.getBytes())
        );

        // Capture everything printed to System.out.
        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));

        WeatherCLI cli = new WeatherCLI(service);
        cli.run();

        return output.toString();
    }

    @Test
    void helpDisplaysAvailableCommands() {
        String output = runCLI("help\nquit\n");

        assertTrue(output.contains("Available commands:"));
        assertTrue(output.contains("current <location>"));
        assertTrue(output.contains("compare <location1> <location2>"));
    }

    @Test
    void locationsDisplaysKnownLocations() {
        String output = runCLI("locations\nquit\n");

        assertTrue(output.contains("Chicago"));
        assertTrue(output.contains("Los Angeles"));
        assertTrue(output.contains("New York"));
    }

    @Test
    void currentDisplaysWeatherForLocation() {
        String output = runCLI("current Chicago\nquit\n");

        assertTrue(output.contains("60.0°F"));
        assertTrue(output.contains("Humidity: 70.0%"));
        assertTrue(output.contains("Wind Speed: 10.0 mph"));
    }

    @Test
    void invalidCommandDisplaysErrorMessage() {
        String output = runCLI("banana\nquit\n");

        assertTrue(output.contains("Unknown command: banana"));
    }

    @Test
    void unknownLocationDisplaysErrorMessage() {
        String output = runCLI("current Gotham\nquit\n");

        assertTrue(output.contains("Error: Unknown location: Gotham"));
    }
}