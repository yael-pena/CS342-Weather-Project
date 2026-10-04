package weather;

import weather.model.Location;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import weather.cli.WeatherCLI;
import weather.model.Location;
import weather.provider.OpenMeteoWeatherProvider;
import weather.provider.WeatherDataProvider;
import weather.service.WeatherService;
import weather.service.WeatherSummary;

import java.util.List;
import static java.lang.IO.println;

// STARTER CODE:
// This class intentionally contains several responsibilities.
// Refactor it into the required model, provider, service, and CLI packages.
// Record where we're keeping the data

public class Main {

    public static void main(String[] args) {
        println("🌤️ Initializing Real-Time Multi-City Weather Service...");

        List<Location> locations = List.of(
                new Location("Chicago", "41.85", "-87.65"),
                new Location("Los Angeles", "34.05", "-118.24"),
                new Location("New York", "40.71", "-74.01")
        );

        WeatherDataProvider provider = new OpenMeteoWeatherProvider();
        WeatherService service = new WeatherService(provider, locations);
        WeatherCLI cli = new WeatherCLI(service);

        cli.run();
    }
}
