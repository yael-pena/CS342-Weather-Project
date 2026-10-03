package weather.provider;
//import our other files we need to access
import weather.model.WeatherData;
import weather.model.Location;
//import java.net to be able to create URI and send http
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//importing java.io to be able to handle exceptions
import java.io.IOException;

//We will be using the interface provided by WeatherDataProvider to save the information gathered in
//a WeatherData object
public final class OpenMeteoWeatherProvider implements WeatherDataProvider {
    
    @Override
    public WeatherData getWeatherData(Location location){
        //grabs latitude and longitude of given location
        var latitude = location.latitude();
        var longitude = location.longitude();
        //creates the url for the json file that the API returns for this specific lat and long
        String URL = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,apparent_temperature,precipitation&temperature_unit=fahrenheit";
        //create the uri to send through to the api using the url
        URI uri = URI.create(URL);
        //create the client that will communicate with API, send or receive Http, communicates w/API
        HttpClient client = HttpClient.newHttpClient();
        //the "letter" being sent to the API to tell it what to give us from the uri
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        //try sending the request and store the body(JSON response file) into a string, handle errors
        try{
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            //reponse code of 200 means request was successful, so check if not successful
            if (response.statusCode() != 200){
                throw new RuntimeException("Weather API returned status of: " + response.statusCode());
            }
            //store the JSON response as a String
            String json = response.body();

            return parseWeatherData(json);  //run the method to parse the json file and return data

        } catch (IOException | InterruptedException e){ //couldn't get response from API
            throw new RuntimeException("Failed to get weather data", e);
        }

    }

    // This is where the JSON string is being parsed and seeing what is there
    // Using REGEX, look for the pattern, looking for temperature_2m, and then give me this ______

    private WeatherData parseWeatherData(String json) {
        /*Pattern sets the pattern to be searched for by Matcher, which searches the given json file
        and looks for the given pattern. Then, the found data is grabbed using .group, which returns
        the number after the pattern we decided to look for */
        //look for a "temperature_2m" in the json file, then return the number following it regardless of whitespace
        Pattern tempPattern = Pattern.compile("\"temperature_2m\":\\s*([0-9.-]+)");
        Matcher tempMatcher = tempPattern.matcher(json);

        //look for a "apparent_temperature" in the json file, then return the number following it regardless of whitespace
        Pattern feelsLikePattern = Pattern.compile("\"apparent_temperature\":\\s*([0-9.-]+)");
        Matcher feelsLikeMatcher = feelsLikePattern.matcher(json);

        //look for a "precipitation" in the json file, then return the number following it regardless of whitespace
        Pattern precipitationPattern = Pattern.compile("\"apparent_temperature\":\\s*([0-9.-]+)");
        Matcher precipitationMatcher = precipitationPattern.matcher(json);

        //If a match was found for all data, parse as a double from a string
        if (tempMatcher.find() && feelsLikeMatcher.find() && precipitationMatcher.find()){
            double temperature = Double.parseDouble(tempMatcher.group(1));
            double feelsLikeTemp = Double.parseDouble(feelsLikeMatcher.group(1));
            double precipitation = Double.parseDouble(precipitationMatcher.group(1));

            return new WeatherData(temperature, feelsLikeTemp, precipitation);
        }
        //should one, or more, of the data's not be found, throw an exception
        throw new RuntimeException("Weather data could not be found in API response");
    }
}
