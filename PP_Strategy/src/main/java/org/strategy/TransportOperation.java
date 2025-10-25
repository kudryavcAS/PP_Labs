package org.strategy;

public abstract class TransportOperation {
    public abstract double calculateTime(double distance, double cargoWeight);

    public abstract double calculateCargoCost(double distance, double cargoWeight);

    public abstract double calculatePassengerCost(double distance, int passengers);
}