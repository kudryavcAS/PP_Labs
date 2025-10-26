package org.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarOperationTest {

    private final CarOperation carOperation = new CarOperation();

    @Test
    void testCalculateTimeNormalCase() {
        double time = carOperation.calculateTime(100, 50);
        assertTrue(time > 0);
        assertEquals(1.25, time, 0.1);
    }

    @Test
    void testCalculateTimeDifference() {
        double timeWithHeavyCargo = carOperation.calculateTime(100, 300);
        double timeWithLightCargo = carOperation.calculateTime(100, 10);
        assertTrue(timeWithHeavyCargo > timeWithLightCargo);
    }

    @Test
    void testCalculateCargoCost() {
        double cost = carOperation.calculateCargoCost(100, 50);
        assertEquals(225.0, cost);
    }

    @Test
    void testCalculatePassengerCost() {
        double cost = carOperation.calculatePassengerCost(100, 3);
        assertEquals(200.0, cost);
    }

    @Test
    void testGetMaxPassengers() {
        assertEquals(4, CarOperation.getMaxPassengers());
    }

    @Test
    void testGetMaxCargoWeight() {
        assertEquals(350.0, CarOperation.getMaxCargoWeight());
    }

    @Test
    void testValidateCapacityCargoLimit() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carOperation.validateCapacity(360.0, 0);
        });

        assertTrue(exception.getMessage().contains("Car cannot carry more than 350,0 kg."));
        assertTrue(exception.getMessage().contains("Requested: 360,0 kg"));
    }

    @Test
    void testValidateCapacityPassengers() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carOperation.validateCapacity(10.0, 5);
        });

        assertEquals("Car cannot carry more than 4 passengers. Requested: 5", exception.getMessage());
    }
}