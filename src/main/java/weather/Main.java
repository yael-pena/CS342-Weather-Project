package weather;

import weather.model.Location;

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

public class Main {

    public static void main(String[] args) {
        println("🌤️ Initializing Real-Time Multi-City Weather Service...");

        List<Location> locations = List.of(
                new Location("Chicago", "41.85", "-87.65"),
                new Location("Los Angeles", "34.05", "-118.24"),
                new Location("New York", "40.71", "-74.01")
        );

        // We need to create an HTTPClient Connect
        // this target location in locations, locations is our list of records
        // we use each record to form our URL
        // our actual request to the service we're making is that
        // we have a hardcoded string, putting in the data that changes
        // in this case, the data we need to pout in that changes is the
        // latitude and longitude
        // one way to concat strings is to use +



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




}
