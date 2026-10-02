package weather.model;
/*Once again using record since the gathered data should be immutable. This once again allows java to
give us the temperature, feelslikeTemp and precipitation with built in functions rather than
defining them ourselves*/
public record WeatherData(double temperature, double feelsLikeTemp, double precipitation){}