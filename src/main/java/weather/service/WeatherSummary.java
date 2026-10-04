package weather.service;

import weather.model.Location;
import weather.model.WeatherData;

public record WeatherSummary(Location location, WeatherData weather) {
}
