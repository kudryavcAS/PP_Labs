package org.strategy;

public interface TransportOperation {
    public abstract double calculateTime(double distance, double cargoWeight);

    public abstract double calculateCargoCost(double distance, double cargoWeight);

    public abstract double calculatePassengerCost(double distance, int passengers);

}