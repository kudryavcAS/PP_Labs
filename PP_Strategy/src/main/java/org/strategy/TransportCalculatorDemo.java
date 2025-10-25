package org.strategy;

import java.util.Scanner;

public class TransportCalculatorDemo {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Calculate time and cost for your trip\n");

        double distance = getInputDouble("Enter distance (km): ");
        double cargoWeight = getInputDouble("Enter cargo weight <= 500 (kg): ");
        int passengers = getInputInt("Enter number of passengers: <= 8: ");

        TransportType transportType = selectTransport();

        calculateAndPrintResult(transportType, distance, cargoWeight, passengers);

        in.close();
    }

    private static double getInputDouble(String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            if (in.hasNextDouble()) {
                value = in.nextDouble();
                if (value >= 0) {
                    in.nextLine();
                    return value;
                } else {
                    System.out.println("Value cannot be negative. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                in.next();
            }
        }
    }

    private static int getInputInt(String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (in.hasNextInt()) {
                value = in.nextInt();
                if (value >= 0) {
                    in.nextLine();
                    return value;
                } else {
                    System.out.println("Value cannot be negative. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                in.next();
            }
        }
    }

    private static TransportType selectTransport() {
        System.out.println("\nSelect transport type: ");
        System.out.println("1. Car - max passengers: "+ CarOperation.getMaxPassengers()+
                " max weight: "+ CarOperation.getMaxCargoWeight());
        System.out.println("2. Bicycle - max passengers: "+ BicycleOperation.getMaxPassengers()+
                " max weight: "+ BicycleOperation.getMaxCargoWeight());
        System.out.println("3. Cart - max passengers: "+ CartOperation.getMaxPassengers()+
                " max weight: "+ CartOperation.getMaxCargoWeight());

        int choice;
        while (true) {
            System.out.print("Enter your choice (1-3): ");
            if (in.hasNextInt()) {
                choice = in.nextInt();
                in.nextLine(); // очистка буфера

                switch (choice) {
                    case 1:
                        return TransportType.CAR;
                    case 2:
                        return TransportType.BICYCLE;
                    case 3:
                        return TransportType.CART;
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2 or 3.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number (1-3).");
                in.next(); // очистка неверного ввода
            }
        }
    }

    private static void calculateAndPrintResult(TransportType transportType,
                                                double distance,
                                                double cargoWeight,
                                                int passengers) {

        TransportCalculator calculator = new TransportCalculator(transportType);
        calculator.calculateFullTrip(distance, cargoWeight, passengers);

    }
}