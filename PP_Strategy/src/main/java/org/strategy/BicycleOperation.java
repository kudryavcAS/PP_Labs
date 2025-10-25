package org.strategy;

public class BicycleOperation extends TransportOperation {
    public double calculateTime(double distance, double cargoWeight) {
        double baseSpeed = 15.0;
        double frictionCoefficient = 0.05;
        double speedReduction = cargoWeight * frictionCoefficient;
        return distance / Math.max(baseSpeed - speedReduction, 5.0);
    }

    public double calculateCargoCost(double distance, double cargoWeight) {
        return distance * 0.1 + cargoWeight * 0.2;
    }

    public double calculatePassengerCost(double distance, int passengers) {
        System.out.println("The bike is not designed to carry passengers.");
        return 0.0;
    }
}