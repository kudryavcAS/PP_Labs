package org.strategy;

public class CarOperation implements TransportOperation {
    private static final int MAX_PASSENGERS = 4;
    private static final double MAX_CARGO_WEIGHT = 350.0;

    @Override
    public double calculateTime(double distance, double cargoWeight) {
        validateCapacity(cargoWeight, 0); // пассажиры проверяются отдельно
        double baseSpeed = 80.0;
        double frictionCoefficient = 0.01;
        double speedReduction = cargoWeight * frictionCoefficient;
        return distance / Math.max(baseSpeed - speedReduction, 20.0);
    }

    @Override
    public double calculateCargoCost(double distance, double cargoWeight) {
        validateCapacity(cargoWeight, 0);
        return distance * 2.0 + cargoWeight * 0.5;
    }

    @Override
    public double calculatePassengerCost(double distance, int passengers) {
        validateCapacity(0, passengers);
        return distance * 0.5 + passengers * 50.0;
    }

    void validateCapacity(double cargoWeight, int passengers) {
        if (cargoWeight > MAX_CARGO_WEIGHT) {
            throw new IllegalArgumentException(
                    String.format("Car cannot carry more than %.1f kg. Requested: %.1f kg",
                            MAX_CARGO_WEIGHT, cargoWeight)
            );
        }
        if (passengers > MAX_PASSENGERS) {
            throw new IllegalArgumentException(
                    String.format("Car cannot carry more than %d passengers. Requested: %d",
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