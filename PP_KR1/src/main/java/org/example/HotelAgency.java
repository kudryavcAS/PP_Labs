package org.example;

import java.io.*;
import java.util.*;

public class HotelAgency {
    private final Set<Hotel> hotels;

    public HotelAgency() {
        this.hotels = new HashSet<>();
    }

    public void readFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String currentCity = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.matches("[а-яА-Яa-zA-Z\\s-]+") && !line.matches(".*\\d.*")) {
                    currentCity = line;
                } else {
                    if (currentCity != null) {
                        try {
                            String[] parts = line.split("\\s+", 2);
                            if (parts.length == 2) {
                                String hotelName = parts[0];
                                int stars = Integer.parseInt(parts[1]);
                                Hotel hotel = new Hotel(currentCity, hotelName, stars);
                                hotels.add(hotel);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing line: " + line);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }

    public void displayByCity(String cityName) {
        System.out.println("Hotels in " + cityName + ":");

        List<Hotel> cityHotels = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (hotel.getCity().equalsIgnoreCase(cityName)) {
                cityHotels.add(hotel);
            }
        }

        if (cityHotels.isEmpty()) {
            System.out.println("No hotels found");
            return;
        }

        cityHotels.sort((h1, h2) -> {
            int nameCompare = h1.getName().compareTo(h2.getName());
            if (nameCompare != 0) {
                return nameCompare;
            }
            return Integer.compare(h2.getStars(), h1.getStars());
        });

        for (Hotel hotel : cityHotels) {
            System.out.printf("  %s - %d stars\n", hotel.getName(), hotel.getStars());
        }
    }


    public void displayByName(String hotelName) {
        System.out.println("Cities with " + hotelName + ":");

        List<Hotel> nameHotels = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (hotel.getName().equalsIgnoreCase(hotelName)) {
                nameHotels.add(hotel);
            }
        }

        if (nameHotels.isEmpty()) {
            System.out.println("No cities found");
            return;
        }

        nameHotels.sort((h1, h2) -> {
            int cityCompare = h1.getCity().compareTo(h2.getCity());
            if (cityCompare != 0) {
                return cityCompare;
            }
            return Integer.compare(h2.getStars(), h1.getStars());
        });

        for (Hotel hotel : nameHotels) {
            System.out.printf("  %s - %d stars\n", hotel.getCity(), hotel.getStars());
        }
    }

    public static void main(String[] args) {
        HotelAgency manager = new HotelAgency();

        try (Scanner scanner = new Scanner(System.in)) {
            manager.readFromFile("hotel.txt");
            System.out.println("Data loaded successfully!");
            System.out.println("Available commands: city, name, exit");

            label:
            while (true) {
                System.out.print("\nEnter command: ");
                String command = scanner.nextLine().trim().toLowerCase();

                switch (command) {
                    case "exit":
                        System.out.println("Program is completed");
                        break label;
                    case "city":
                        System.out.print("Enter city name: ");
                        String city = scanner.nextLine().trim();
                        if (city.isEmpty()) {
                            System.out.println("City name cannot be empty");
                        } else {
                            manager.displayByCity(city);
                        }
                        break;
                    case "name":
                        System.out.print("Enter hotel name: ");
                        String name = scanner.nextLine().trim();
                        if (name.isEmpty()) {
                            System.out.println("Hotel name cannot be empty");
                        } else {
                            manager.displayByName(name);
                        }
                        break;
                    case "help":
                        System.out.println("Commands:");
                        System.out.println("  city - search hotels by city");
                        System.out.println("  name - search cities by hotel name");
                        System.out.println("  exit - close program");
                        System.out.println("  help - show this message");
                        break;
                    default:
                        System.out.println("Unknown command. Type 'help' for available commands.");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
