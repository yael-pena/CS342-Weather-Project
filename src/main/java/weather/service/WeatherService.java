package weather.service;

import weather.model.Location;
import weather.model.WeatherData;
import weather.provider.WeatherDataProvider;

import java.util.List;

public class WeatherService {
    private final WeatherDataProvider provider;
    private final List<Location> locations;

    public WeatherService(WeatherDataProvider provider, List<Location> locations) {
        this.provider = provider;
        this.locations = locations;
    }

    public WeatherData getCurrentWeather(Location location) {
        return provider.getCurrentWeather(location);
    }


}
