CS 342 Weather Information Service

Team Members:

Yael Pena
Julian Alanis

Running The Application:

This project requires Java 25 and Maven and to compile the project, run:

mvn compile

Then run the application through the Main class. The application starts an interactive command-line interface with the following commands:

help
locations
current <location>
compare <location1> <location2>
summary <location>
warmest
quit

The application only supports the three cities: Chicago, Los Angeles, and New York.

Running the Tests:

To run the complete test suite, use:

mvn test

The project contains 13 JUnit tests. There's 8 tests in WeatherServiceTest and 5 tests in WeatherCLITest

The tests use controlled weather data and do not require an Internet connection or access to the Open-Meteo API.

Design:

The original starter application had most of its functionality inside Main, which was bad software architecture. To remedy this issue, responsibilities of the program were refactored so that each major responsibility is handled by a separate component.

Main:

Main acts as the composition root of the application. It creates the known locations, creates the production OpenMeteoWeatherProvider, injects the provider into WeatherService, creates the WeatherCLI, and starts the application.

Location:

Location is a record containing the name, latitude, and longitude of a location.

WeatherData:

WeatherData is a record containing the current temperature, humidity, and wind speed returned for a location.

WeatherDataProvider:

WeatherDataProvider is the interface used to retrieve current weather information for a location.

OpenMeteoWeatherProvider:

OpenMeteoWeatherProvider is the production implementation of WeatherDataProvider. It communicates with the Open-Meteo API, checks the HTTP response, parses the returned weather information from the JSON response, and converts that information into a WeatherData object.

WeatherService:

WeatherService contains the main weather-related application logic and behavior. It finds known locations, retrieves current weather through a WeatherDataProvider, compares locations, produces weather summaries, and determines the warmest configured location.

WeatherCLI:

WeatherCLI handles all the command line interactions with the user. It reads commands, checks the validity of command arguments, calls the appropriate methods in WeatherService, and displays the results. It does not directly communicate with Open-Meteo because it doesn't need to and shouldn't have to. All the CLI cares about is connecting the user with the service, and returning the information that was requested.

Interfaces:

The project uses the WeatherDataProvider interface to separate the application from a specific source of weather data. WeatherService communicates with a WeatherDataProvider rather than directly communicating with OpenMeteoWeatherProvider. This allows another weather provider to be substituted without changing the main application logic. OpenMeteoWeatherProvider implements this interface for the production application.

Dependency Injection:

WeatherDataProvider is injected into WeatherService through its constructor:

public WeatherService(WeatherDataProvider provider, List<Location> locations)

This prevents WeatherService from being tightly coupled to OpenMeteoWeatherProvider.

In the real application, Main injects an OpenMeteoWeatherProvider. During testing, a controlled test implementation of WeatherDataProvider can be injected instead.

This makes WeatherService easier to test and also makes it possible to replace Open-Meteo with another weather provider without rewriting the service.

Testing:

The application is tested without accessing the live Open-Meteo API.

The JUnit tests use a FakeWeatherProvider that implements WeatherDataProvider and returns predetermined weather information for Chicago, Los Angeles, and New York.

For example, the test provider can always return known values such as a temperature of 60°F for Chicago. This makes the tests predictable and prevents test results from depending on the current weather, Internet access, or availability of the Open-Meteo service.

The service tests cover behaviors including retrieving weather, known and unknown locations, comparisons, summaries, the additional feature, and provider failures.

The CLI tests simulate user commands and verify the resulting output, including valid commands and error handling.

Java 25:

The project uses several modern Java features where they improve readability.

I used Java records because these classes only represented data and didn't need much additional behavior. I also used the enhanced switch expressions for the Command Line, streamlining the process. I also used the List.of command to create the application's predefined locations. 

Additional Feature:

The additional feature I implemented in this project is the warmest command. When the user enters: "warmest", I thought it be the simplest since all we'd have to do are make comparisons with all the locations in the Locations list with their temperature values, similarly to how a MAX() operation works in SQL, and then return the highest temperature. The CLI then displays the result to the user. This feature uses the existing WeatherDataProvider and WeatherService architecture rather than communicating directly with the weather API.

Design Reflection Questions: 

1. The original starter code was a mess. While the program did work, it was poorly coded in the sense that if this program was to be used practically, it wouldn't last very long because it'd be difficult to maintain and update, and having everything in main made it so there was no proper software architecture. Main stored the locations, communicated with Open-Meteo, handled errors, and displayed weather information. To refactor, a common question I'd ask myself every time I noticed an opportunity to improve the architecture of the starter code is "Does the behavior of this task depend on the behavior of another task?" or "Can this responsibility be simplified to one task at a time?" I separated the responsibilities into different components. Location and WeatherData were records that represented our data, OpenMeteoWeatherProvider handled communication with the API and parsing its response, WeatherService was the link between the CLI and the WeatherDataProvider, delivering and receiving to handle application weather related logic, and the WeatherCLI handled the displaying and user end aspect of the program, that being the commands and the actual information. Lastly, Main went from being where it literally did everything, and now became the building blocks for creating and connecting everything together.

2. This was the main thing our professor wanted us to understand. Before, the program depended entirely on the OpenMeteoWeatherProvider. So if any other API wanted to be used instead, you'd have to change 80% - 90% of the software architecture in order to reconnect the behavior and logic like a jigsaw puzzle. By giving WeatherService an interface, if we wanted to replace OpenMeteo with another weather API, we could create another class that implements WeatherDataProvider without having to rewrite the logic inside WeatherService.

3. It's easier because everything gets passed through its constructor instead of creating an OpenMeteoWeatherProvider itself. During the actual application, we inject OpenMeteoWeatherProvider. During testing, we can instead inject our FakeWeatherProvider, which returns weather values that we already know. This lets us test the behavior of WeatherService by itself without depending on the real API.

4. Live Weather API is very volatile and impractical to use as a test case. It also requires an internet connection. If our tests depended on the actual temperature in Chicago, we wouldn't know what value to expect every time the test runs. The logical equivalent would be generating a random temperature value every time the test is run to compare against. The API could also be down even when there's nothing wrong with our code. By using a FakeWeatherProvider, we control the values being returned, making the tests predictable and repeatable.

5. The main part that would need to change would be the provider implementation. We'd have to create a new class that implements WeatherDataProvider and handles however way the new API sends and formats its weather information. Main would also need to account for this new change so that it creates and injects the new provider instead of the OpenMeteo one. As for the main reason why we designed interfaces in the first place, WeatherService does not need to change because it communicates through the WeatherDataProvider interface instead of the OpenMeteo object. WeatherService does not care about what WeatherDataProvider implementation it's connecting to because as long as the object follows the rules defined by the interface, it will work. WeatherCLI also doesn't need to change because it only communicates with WeatherService, of which nothing needs to be changed if there were a new API that we were to use. This is again, the main benefit of one: separating responsibilities, and two: programming an interface instead of directly depending on one specific weather API. Having this mindset going into programming any kind of software is the differentiating factor between a software engineer that can just create code, and create efficient, long lasting code.







