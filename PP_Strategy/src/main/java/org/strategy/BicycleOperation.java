package org.strategy;

public class BicycleOperation implements TransportOperation {
    private static final int MAX_PASSENGERS = 0;
    private static final double MAX_CARGO_WEIGHT = 50.0;

    @Override
    public double calculateTime(double distance, double cargoWeight) {
        validateCapacity(cargoWeight, 0);
        double baseSpeed = 15.0;
        double frictionCoefficient = 0.05;
        double speedReduction = cargoWeight * frictionCoefficient;
        return distance / Math.max(baseSpeed - speedReduction, 5.0);
    }

    @Override
    public double calculateCargoCost(double distance, double cargoWeight) {
        validateCapacity(cargoWeight, 0);
        return distance * 0.1 + cargoWeight * 0.2;
    }

    @Override
    public double calculatePassengerCost(double distance, int passengers) {
        validateCapacity(0, passengers);
        System.out.println("The bike is not designed to carry passengers.");
        return 0.0;
    }

    void validateCapacity(double cargoWeight, int passengers) {
        if (cargoWeight > MAX_CARGO_WEIGHT) {
            throw new IllegalArgumentException(
                    String.format("Bicycle cannot carry more than %.1f kg. Requested: %.1f kg",
                            MAX_CARGO_WEIGHT, cargoWeight)
            );
        }
        if (passengers > MAX_PASSENGERS) {
            throw new IllegalArgumentException("Bicycle cannot carry passengers");
        }
    }

    public static int getMaxPassengers() {
        return MAX_PASSENGERS;
    }

    public static double getMaxCargoWeight() {
        return MAX_CARGO_WEIGHT;
    }
}