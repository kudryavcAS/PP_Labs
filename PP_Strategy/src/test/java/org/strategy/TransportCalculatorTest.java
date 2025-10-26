package org.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransportCalculatorTest {

    @Test
    void testCarCalculatorCreation() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);
        assertNotNull(calculator);
        assertEquals("Car", calculator.getTransportName());
    }

    @Test
    void testBicycleCalculatorCreation() {
        TransportCalculator calculator = new TransportCalculator(TransportType.BICYCLE);
        assertNotNull(calculator);
        assertEquals("Bicycle", calculator.getTransportName());
    }

    @Test
    void testCartCalculatorCreation() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CART);
        assertNotNull(calculator);
        assertEquals("Cart", calculator.getTransportName());
    }

    @Test
    void testCarCalculatorValidTrip() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);
        assertDoesNotThrow(() -> calculator.calculateFullTrip(100, 50, 2));
    }

    @Test
    void testBicycleCalculatorValidTrip() {
        TransportCalculator calculator = new TransportCalculator(TransportType.BICYCLE);
        assertDoesNotThrow(() -> calculator.calculateFullTrip(10, 5, 0));
    }

    @Test
    void testCartCalculatorValidTrip() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CART);
        assertDoesNotThrow(() -> calculator.calculateFullTrip(50, 200, 4));
    }

    @Test
    void testCarCalculatorExceedPassengers() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFullTrip(100, 50, 5);
        });
        assertTrue(exception.getMessage().contains("cannot carry more than 4 passengers"));
    }

    @Test
    void testBicycleCalculatorWithPassengers() {
        TransportCalculator calculator = new TransportCalculator(TransportType.BICYCLE);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFullTrip(10, 5, 1);
        });
        assertTrue(exception.getMessage().contains("cannot carry passengers"));
    }

    @Test
    void testCartCalculatorExceedCargo() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CART);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFullTrip(50, 600, 4);
        });
        assertTrue(exception.getMessage().contains("Cart cannot carry more than 500,0 kg"));
    }

    @Test
    void testNegativeDistance() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFullTrip(-100, 50, 2);
        });
        assertEquals("Distance cannot be negative", exception.getMessage());
    }

    @Test
    void testNegativeCargo() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFullTrip(100, -50, 2);
        });
        assertEquals("Cargo weight cannot be negative", exception.getMessage());
    }

    @Test
    void testNegativePassengers() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFullTrip(100, 50, -1);
        });
        assertEquals("Passengers count cannot be negative", exception.getMessage());
    }

    @Test
    void testGetTimeValid() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);
        double time = calculator.getTime(100, 50);
        assertTrue(time > 0);
    }

    @Test
    void testGetCargoCostValid() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);
        double cost = calculator.getCargoCost(100, 50);
        assertEquals(225.0, cost);
    }

    @Test
    void testGetPassengerCostValid() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);
        double cost = calculator.getPassengerCost(100, 3);
        assertEquals(200.0, cost);
    }

    @Test
    void testGetTimeNegativeDistance() {
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.getTime(-100, 50);
        });
        assertEquals("Distance and cargo weight cannot be negative", exception.getMessage());
    }
}