package weather.provider;   //sets this file to be included in the weather.provider package

import weather.model.Location;  //imports the Location class from weather.model package
//imports the WeatherData class from weather.model packge so we can store that data in a WeatherData
//object to pass into OpenMeteoWeatherProvider
import weather.model.WeatherData;   

public interface WeatherDataProvider {
    WeatherData getWeatherData(Location location);
}
