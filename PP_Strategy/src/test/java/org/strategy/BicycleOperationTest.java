package org.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BicycleOperationTest {

    private final BicycleOperation bicycleOperation = new BicycleOperation();

    @Test
    void testCalculateTimeWithCargo() {
        double time = bicycleOperation.calculateTime(30, 10);
        assertTrue(time > 0);
    }

    @Test
    void testGetMaxPassengers_AlwaysZero() {
        assertEquals(0, BicycleOperation.getMaxPassengers());
    }

    @Test
    void testGetMaxCargoWeight() {
        assertEquals(50.0, BicycleOperation.getMaxCargoWeight());
    }

    @Test
    void testValidateCapacityCargoLimit() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bicycleOperation.validateCapacity(60.0, 0);
        });

        assertTrue(exception.getMessage().contains("Bicycle cannot carry more than 50,0 kg."));
        assertTrue(exception.getMessage().contains("Requested: 60,0 kg"));
    }

    @Test
    void testValidateCapacityPassengers() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bicycleOperation.validateCapacity(10.0, 1);
        });

        assertEquals("Bicycle cannot carry passengers", exception.getMessage());
    }
}