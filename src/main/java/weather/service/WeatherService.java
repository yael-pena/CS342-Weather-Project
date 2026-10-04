package weather.service;

import weather.model.Location;
import weather.model.WeatherData;
import weather.provider.WeatherDataProvider;

import java.util.ArrayList;
import java.util.List;

public class WeatherService {
    private final WeatherDataProvider provider;
    private final List<Location> locations;

    public WeatherService(WeatherDataProvider provider, List<Location> locations) {
        this.provider = provider;
        this.locations = locations;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public WeatherData getCurrentWeather(String city) {
        Location location = findLocation(city);
        return provider.getCurrentWeather(location);
    }

    public WeatherComparison compare(String firstCity, String secondCity) {
        Location firstLocation = findLocation(firstCity);
        Location secondLocation = findLocation(secondCity);

        WeatherData firstWeather = provider.getCurrentWeather(firstLocation);
        WeatherData secondWeather = provider.getCurrentWeather(secondLocation);

        return new WeatherComparison(
                firstLocation,
                firstWeather,
                secondLocation,
                secondWeather
        );
    }

    public WeatherSummary summary(String city) {
        Location location = findLocation(city);
        WeatherData weather = provider.getCurrentWeather(location);

        return new WeatherSummary(location, weather);
    }

    public WeatherSummary getWarmestLocation() {
        if (locations.isEmpty()) {
            throw new IllegalStateException("No locations are configured.");
        }

        WeatherSummary warmest = null;

        for (Location location : locations) {
            WeatherData currWeather = provider.getCurrentWeather(location);
            if (warmest == null || currWeather.temp() > warmest.weather().temp()) {
                warmest = new WeatherSummary(location, currWeather);
            }

        }
        return warmest;
    }

    public Location findLocation(String city) {
        for (Location location : locations) {
            if (location.city().equalsIgnoreCase(city)) {
                return location;
            }
        }

        throw new IllegalArgumentException("Unknown location: " + city);
    }

}
