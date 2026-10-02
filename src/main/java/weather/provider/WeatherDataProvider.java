package weather.provider;
import weather.model.WeatherData;
import weather.model.Location;

public interface WeatherDataProvider {

    WeatherData getCurrentWeather(Location location);

}
