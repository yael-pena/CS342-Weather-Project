package weather.model;

/*Using a record here since the location data should be immutable for each instance defined.
The use of a record also allows java to give us functions such as .lat() and .lon()
without the need for us to define them nor create them as separate functions*/
public record Location(String name, String lat, String lon) {}