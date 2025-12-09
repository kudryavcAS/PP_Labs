package org.strategy;

public class CartOperation implements TransportOperation {
    private static final int MAX_PASSENGERS = 8;
    private static final double MAX_CARGO_WEIGHT = 500.0;

    @Override
    public double calculateTime(double distance, double cargoWeight) {
        validateCapacity(cargoWeight, 0);
        double baseSpeed = 8.0;
        double frictionCoefficient = 0.02;
        return distance / baseSpeed + cargoWeight * frictionCoefficient;
    }

    @Override
    public double calculateCargoCost(double distance, double cargoWeight) {
        validateCapacity(cargoWeight, 0);
        return distance * 0.8 + cargoWeight * 0.3;
    }

    @Override
    public double calculatePassengerCost(double distance, int passengers) {
        validateCapacity(0, passengers);
        return distance * 0.2 + passengers * 25.0;
    }

    void validateCapacity(double cargoWeight, int passengers) {
        if (cargoWeight > MAX_CARGO_WEIGHT) {
            throw new IllegalArgumentException(
                    String.format("Cart cannot carry more than %.1f kg. Requested: %.1f kg",
                            MAX_CARGO_WEIGHT, cargoWeight)
            );
        }
        if (passengers > MAX_PASSENGERS) {
            throw new IllegalArgumentException(
                    String.format("Cart cannot carry more than %d passengers. Requested: %d",
                            MAX_PASSENGERS, passengers)
            );
        }
    }

   public static int getMaxPassengers() {
        return MAX_PASSENGERS;
    }

    public static double getMaxCargoWeight() {
        return MAX_CARGO_WEIGHT;
    }
}