package org.strategy;

public class CarOperation extends TransportOperation {
    public double calculateTime(double distance, double cargoWeight) {
        double baseSpeed = 80.0;
        double frictionCoefficient = 0.01;
        double speedReduction = cargoWeight * frictionCoefficient;
        return distance / Math.max(baseSpeed - speedReduction, 20.0);
    }

    public double calculateCargoCost(double distance, double cargoWeight) {
        return distance * 2.0 + cargoWeight * 0.5;
    }

    public double calculatePassengerCost(double distance, int passengers) {
        return distance * 0.5 + passengers * 50.0;
    }
}