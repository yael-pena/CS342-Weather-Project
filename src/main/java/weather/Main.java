package weather;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.IO.println;

// STARTER CODE:
// This class intentionally contains several responsibilities.
// Refactor it into the required model, provider, service, and CLI packages.
// Record where we're keeping the data
record TargetLocation(String city, String lat, String lon) {}

public class Main {

    public static void main(String[] args) {
        println("🌤️ Initializing Real-Time Multi-City Weather Service...");

        // Creating a list of those records, a record for each city
        List<TargetLocation> locations = List.of(
                new TargetLocation("Chicago", "41.85", "-87.65"),
                new TargetLocation("Los Angeles", "34.05", "-118.24"),
                new TargetLocation("New York", "40.71", "-74.01")
        );

        // We need to create an HTTPClient Connect
        // this target location in locations, locations is our list of records
        // we use each record to form our URL
        // our actual request to the service we're making is that
        // we have a hardcoded string, putting in the data that changes
        // in this case, the data we need to pout in that changes is the
        // latitude and longitude
        // one way to concat strings is to use +

        try (HttpClient client = HttpClient.newHttpClient()) {
            for (TargetLocation target : locations) {
                String url = "https://api.open-meteo.com/v1/forecast?latitude=" + target.lat()
                        + "&longitude=" + target.lon()
                        + "&current=temperature_2m&temperature_unit=fahrenheit";

                // Takes request and builds to the proper format
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                // This is where we get the response after sending it to OpenMedia
                // Will come back in JSON format
                HttpResponse<String> response =
                        client.send(request, HttpResponse.BodyHandlers.ofString());

                // Means we have an actual response
                // Not guaranteed a JSON string that has the values you want will be returned
                // Here we're checking that they sent a valid string
                if (response.statusCode() == 200) {
                    double temp = parseTemperature(response.body()); // Go through JSON and find actual temp
                    renderBar(target.city(), temp); // method to call that prints to the command line
                } else {
                    println("⚠️ " + target.city()
                            + " API Error: Code " + response.statusCode()); //
                }
            }
        } catch (Exception e) {
            println("❌ Operational Error: " + e.getMessage());
        }
    }

    // Design objects so that we're able to test them without a connection
    // Create a command line interface
    // Use things called buffers and readers
    // Catch exceptions, handle errors in a way that makes sense
    // Go to API documentation, and in the commands, you'll need to provide
    // What do you need as a parameter to make that request
    // What kind of data do you receive, how do I return the data, and what does it look like
    // Understand that, and you can request whatever you want.
    //OpenMeteoWeatherProvie will implement everything for you.
    // One place where that JSON information gets parsed
    // Given command, and object, tell weatherService what to do
    // We're going to need to add additional .java classes into our directories
    // At least 8 WeatherServiceTests
    // At least 5 in the WeatherCLITest
    // Will not fully test your program but it's the minimum requirement
    // Add other pull requests wind speed, humidity, go through the API, see what meteo has
    // Pick something interseting. Compare weather between two locations
    // At mininum, compare temperature and additional things to pull

    // When the program starts, can we pull all the information we need and pull the interface
    // You should be pulling the information per request, not ahead of time.
    // We'll look for it, not how any modern application works.
    // Could have everything stored in the cloud instead of client side that way easily referenceable
    

    // This is where the JSON string is being parsed and seeing what is there
    // Using REGEX, look for the pattern, looking for temperature_2m, and then give me this ______

    static double parseTemperature(String json) {
        Pattern pattern = Pattern.compile("\"temperature_2m\":\\s*([0-9.-]+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) { // If a match was found, we will return that value, parsed as a double from a string
            return Double.parseDouble(matcher.group(1));
        }

        // Default Starter Code Solution if Temp not found: Returns 72.0
        // FIXME: look at how to deal with instances when not found
        // TODO: The final project must replace this silent fallback
        // with an appropriate error-handling strategy.
        return 72.0;
    }

    static void renderBar(String city, double temp) {
        System.out.printf("%-15s | %5.1f°F [", city, temp);
        int barLength = (int) Math.max(0, temp / 2);

        for (int j = 0; j < barLength; j++) {
            System.out.print("■");
        }
        for (int j = barLength; j < 40; j++) {
            System.out.print(" ");
        }
        println("]");
    }
}
