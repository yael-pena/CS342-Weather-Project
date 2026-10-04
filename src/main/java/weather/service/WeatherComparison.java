package weather.service;

import weather.model.Location;
import weather.model.WeatherData;

public record WeatherComparison(Location firstLocation,
                                WeatherData firstWeather,
                                Location secondLocation,
                                WeatherData secondWeather){}