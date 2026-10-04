package weather.provider;

import weather.model.Location;
import weather.model.WeatherData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.IO.println;

public class OpenMeteoWeatherProvider implements WeatherDataProvider {

    public WeatherData getCurrentWeather(Location location) {
        try (
            HttpClient client = HttpClient.newHttpClient()) {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=" + location.lat()
                    + "&longitude=" + location.lon()
                    + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m" // requests temp, humidity, and wind speed
                    + "&temperature_unit=fahrenheit" // requests temperature in fahrenheit
                    + "&wind_speed_unit=mph"; // requests wind speed in mph


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
            // Here we're checking if they sent an invalid string

            if (response.statusCode() != 200) {
                throw new IllegalStateException(
                        "Open-Meteo API error: HTTP " + response.statusCode()
                );
            }
                double temp = parseTemperature(response.body());
                double humid = parseHumidity(response.body());
                double windSpeed = parseWindSpeed(response.body());

                return new WeatherData(temp, humid, windSpeed);
        } catch (IllegalStateException e) {
            // An error that our provider intentionally detected
            throw e;

        } catch (Exception e) {
            // An unexpected error while trying to retrieve the weather
            throw new IllegalStateException(
                    "Unable to retrieve weather data: " + e.getMessage(), e
            );
        }
    }

    static double parseTemperature(String json) {
        Pattern pattern = Pattern.compile("\"temperature_2m\":\\s*([0-9.-]+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) { // If a match was found, we will return that value, parsed as a double from a string
            return Double.parseDouble(matcher.group(1));
        }

        throw new IllegalStateException("Temperature data missing from API response");
    }

    static double parseHumidity(String json) {
        Pattern pattern = Pattern.compile("\"relative_humidity_2m\":\\s*([0-9.-]+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) { // If a match was found, we will return that value, parsed as a double from a string
            return Double.parseDouble(matcher.group(1));
        }

        throw new IllegalStateException("Humidity data missing from API response.");
    }

    static double parseWindSpeed(String json) {
        Pattern pattern = Pattern.compile("\"wind_speed_10m\":\\s*([0-9.-]+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) { // If a match was found, we will return that value, parsed as a double from a string
            return Double.parseDouble(matcher.group(1));
        }

        throw new IllegalStateException("Wind speed data missing from API response.");
    }
}

