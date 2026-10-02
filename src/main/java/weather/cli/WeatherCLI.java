package weather.cli;

import static java.lang.IO.println;

public class WeatherCLI {
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

