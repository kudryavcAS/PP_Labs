package org.example;

import java.io.*;
import java.util.*;

public class HotelAgency {
    private final List<Hotel> hotels;

    public HotelAgency() {
        this.hotels = new ArrayList<>();
    }

    public void readFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    Hotel hotel = new Hotel(line);
                    hotels.add(hotel);
                } catch (Exception e) {
                    System.out.println("Error: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }

    public void displayByCity(String cityName) {
        System.out.println("Hotels in " + cityName + ":");
        boolean found = false;
        for (Hotel hotel : hotels) {
            if (hotel.getCity().equalsIgnoreCase(cityName)) {
                System.out.println(hotel.getName() + " - " + hotel.getStars() + " stars");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No hotels found");
        }
    }

    public void displayByName(String hotelName) {
        System.out.println("Cities with " + hotelName + ":");
        boolean found = false;
        for (Hotel hotel : hotels) {
            if (hotel.getName().equalsIgnoreCase(hotelName)) {
                System.out.println(hotel.getCity());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No cities found");
        }
    }

    public static void main(String[] args) {
        HotelAgency manager = new HotelAgency();

        manager.readFromFile("hotel.txt");

        manager.displayByCity("Минск");
        manager.displayByName("Hilton");
    }
}