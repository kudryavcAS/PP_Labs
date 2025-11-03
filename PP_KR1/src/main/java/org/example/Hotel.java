package org.example;

import java.util.*;

public class Hotel {
    private final String city;
    private final String name;
    private final int stars;

    public Hotel(String city, String name, int stars) {
        this.city = city;
        this.name = name;
        this.stars = stars;
    }

    public Hotel(String fileLine) {
        String[] parts = fileLine.split("\\s+", 3); // Разделяем на 3 части
        if (parts.length == 3) {
            this.city = parts[0];
            this.name = parts[1];
            this.stars = Integer.parseInt(parts[2]);
        } else {
            throw new IllegalArgumentException("Invalid string format: " + fileLine);
        }
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public int getStars() {
        return stars;
    }

    @Override
    public String toString() {
        return String.format("Hotel\nCity:'%s', \nName:'%s', \nStars: %d}", city, name, stars);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return stars == hotel.stars &&
                Objects.equals(city, hotel.city) &&
                Objects.equals(name, hotel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, name, stars);
    }
}