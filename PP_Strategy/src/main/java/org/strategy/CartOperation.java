package org.strategy;

public class CartOperation extends TransportOperation {
    public double calculateTime(double distance, double cargoWeight) {
        double baseSpeed = 8.0;
        double frictionCoefficient = 0.02;
        return distance / baseSpeed + cargoWeight * frictionCoefficient;
    }

    public double calculateCargoCost(double distance, double cargoWeight) {
        return distance * 0.8 + cargoWeight * 0.3;
    }

    public double calculatePassengerCost(double distance, int passengers) {
        return distance * 0.2 + passengers * 25.0;
    }
}

