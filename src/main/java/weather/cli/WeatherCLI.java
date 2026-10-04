package weather.cli;

import weather.model.Location;
import weather.model.WeatherData;
import weather.service.WeatherComparison;
import weather.service.WeatherService;
import weather.service.WeatherSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.IO.println;

public class WeatherCLI {

    private final WeatherService service;

    public WeatherCLI(WeatherService service) {
        this.service = service;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Weather Information Service");
        System.out.println("CS 342 - Software Design");
        System.out.println("Type \"help\" for available commands.");

        boolean on = true;

        while (on) {
            System.out.print("weather> ");

            String userInput = scanner.nextLine();

            if (userInput.isBlank()) continue;

            String[] args = userInput.trim().split("\\s+", 2);
            String userCommand = args[0].toLowerCase();

            try {
                switch (userCommand) {
                    case "help" -> {
                        System.out.println("Available commands:");
                        System.out.println("  help:");
                        System.out.println("      Display available commands and their syntax.");
                        System.out.println("  locations:");
                        System.out.println("      Display all known locations.");
                        System.out.println("  current <location>:");
                        System.out.println("      Display the current weather for a location.");
                        System.out.println("  compare <location1> <location2>:");
                        System.out.println("      Compare the current weather of two locations.");
                        System.out.println("  summary <location>:");
                        System.out.println("      Display a summary of current conditions.");
                        System.out.println("  warmest:");
                        System.out.println("      Display the warmest currently known location.");
                        System.out.println("  quit:");
                        System.out.println("      Exit the application.");
                    }
                    case "locations" -> {
                        for (Location location : service.getLocations()) {
                            System.out.println(location.city());
                        }
                    }
                    case "current" -> {
                        if (args.length < 2) {
                            System.out.println("Usage: current <location>");
                            break;
                        }

                        String city = args[1];
                        WeatherData weather = service.getCurrentWeather(city);

                        renderBar(city, weather.temp());
                        System.out.printf("Humidity: %.1f%%%n", weather.humidity());
                        System.out.printf("Wind Speed: %.1f mph%n", weather.windSpeed());
                    }
                    case "compare" -> {
                        if (args.length < 2) {
                            System.out.println("Usage: compare <location1> <location2>");
                            break;
                        }

                        Pattern pattern = Pattern.compile("\"([^\"]+)\"|(\\S+)");
                        Matcher matcher = pattern.matcher(args[1]);

                        List<String> cities = new ArrayList<>();

                        while (matcher.find()) {
                            if (matcher.group(1) != null) {
                                cities.add(matcher.group(1));
                            } else {
                                cities.add(matcher.group(2));
                            }
                        }

                        if (cities.size() != 2) {
                            System.out.println("Usage: compare <location1> <location2>");
                            break;
                        }

                        WeatherComparison comparison =
                                service.compare(cities.get(0), cities.get(1));

                        System.out.printf(
                                "%s: %.1f°F, %.1f%% humidity, %.1f mph wind%n",
                                comparison.firstLocation().city(),
                                comparison.firstWeather().temp(),
                                comparison.firstWeather().humidity(),
                                comparison.firstWeather().windSpeed()
                        );

                        System.out.printf(
                                "%s: %.1f°F, %.1f%% humidity, %.1f mph wind%n",
                                comparison.secondLocation().city(),
                                comparison.secondWeather().temp(),
                                comparison.secondWeather().humidity(),
                                comparison.secondWeather().windSpeed()
                        );

                    }
                    case "summary" -> {
                        if (args.length < 2) {
                            System.out.println("Usage: summary <location>");
                            break;
                        }

                        WeatherSummary summary = service.summary(args[1]);

                        System.out.printf(
                                "%s is currently %.1f°F with %.1f%% humidity and winds of %.1f mph.%n",
                                summary.location().city(),
                                summary.weather().temp(),
                                summary.weather().humidity(),
                                summary.weather().windSpeed()
                        );
                    }
                    case "warmest" -> {
                        WeatherSummary warmest = service.getWarmestLocation();

                        System.out.printf(
                                "%s is currently the warmest location at %.1f°F.%n",
                                warmest.location().city(),
                                warmest.weather().temp()
                        );
                    }
                    case "quit" -> {
                        on = false;
                    }
                    default -> {
                        System.out.println("Unknown command: " + userCommand);
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());

            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    static void renderBar(String city, double temp) {
        System.out.printf("%-15s | %5.1f°F [", city, temp);
        int barLength = (int) Math.max(0, temp / 2);

        for (int j = 0; j < barLength; j++) {
            System.out.print("■");
        }
        for (int j = barLength; j < 40; j++) {
            System.out.print(" ");
        }
        println("]");
    }
}

