package org.strategy;

public class TransportCalculator {
    private TransportOperation operation = null;

    public TransportCalculator(TransportType type) {
        switch (type) {
            case CAR:
                this.operation = new CarOperation();
                break;
            case BICYCLE:
                this.operation = new BicycleOperation();
                break;
            case CART:
                this.operation = new CartOperation();
                break;
            default:
                System.out.println("There is no such type of transport");
        }
    }

    public void calculateFullTrip(double distance, double cargoWeight, int passengers) {
        if (operation == null) {
            throw new IllegalStateException("Transport operation is not set");
        }

        if (distance < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }

        if (cargoWeight < 0) {
            throw new IllegalArgumentException("Cargo weight cannot be negative");
        }

        if (passengers < 0) {
            throw new IllegalArgumentException("Passengers count cannot be negative");
        }

        double time = operation.calculateTime(distance, cargoWeight);
        double cargoCost = operation.calculateCargoCost(distance, cargoWeight);
        double passengerCost = operation.calculatePassengerCost(distance, passengers);
        double totalCost = cargoCost + passengerCost;

        System.out.println(getTransportName() + ":");
        System.out.printf("Distance: %.1f km, Cargo: %.1f kg, Passengers: %d%n", distance, cargoWeight, passengers);
        System.out.printf("Time: %.2f hours%n", time);
        System.out.printf("Cargo cost: %.2f $.%n", cargoCost);
        System.out.printf("Passenger cost: %.2f $.%n", passengerCost);
        System.out.printf("Total cost: %.2f $.%n", totalCost);
    }

    public double getTime(double distance, double cargoWeight) {
        if (operation == null) {
            throw new IllegalStateException("Transport operation is not set");
        }

        if (distance < 0 || cargoWeight < 0) {
            throw new IllegalArgumentException("Distance and cargo weight cannot be negative");
        }

        return operation.calculateTime(distance, cargoWeight);
    }

    public double getCargoCost(double distance, double cargoWeight) {
        if (operation == null) {
            throw new IllegalStateException("Transport operation is not set");
        }

        if (distance < 0 || cargoWeight < 0) {
            throw new IllegalArgumentException("Distance and cargo weight cannot be negative");
        }

        return operation.calculateCargoCost(distance, cargoWeight);
    }

    public double getPassengerCost(double distance, int passengers) {
        if (operation == null) {
            throw new IllegalStateException("Transport operation is not set");
        }

        if (distance < 0 || passengers < 0) {
            throw new IllegalArgumentException("Distance and passengers count cannot be negative");
        }

        return operation.calculatePassengerCost(distance, passengers);
    }

    private String getTransportName() {
        if (operation instanceof CarOperation) {
            return "Car";
        }
        if (operation instanceof BicycleOperation) {
            return "Bicycle";
        }
        if (operation instanceof CartOperation) {
            return "Cart";
        }
        return "Unknown transport";
    }
}