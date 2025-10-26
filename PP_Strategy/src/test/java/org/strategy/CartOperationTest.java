package org.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartOperationTest {

    private final CartOperation cartOperation = new CartOperation();

    @Test
    void testCalculateTimeNormalCase() {
        double time = cartOperation.calculateTime(80, 200);
        assertTrue(time > 0);
        assertEquals(14.0, time, 0.1); // 80km / 8kmh + 200kg * 0.02 = 10 + 4 = 14 часов
    }

    @Test
    void testCalculateTimeDifference() {
        double timeWithHeavyCargo = cartOperation.calculateTime(50, 400);
        double timeWithLightCargo = cartOperation.calculateTime(50, 100);
        assertTrue(timeWithHeavyCargo > timeWithLightCargo);
    }

    @Test
    void testCalculateCargoCost() {
        double cost = cartOperation.calculateCargoCost(100, 200);
        assertEquals(140.0, cost); // 100*0.8 + 200*0.3 = 80 + 60 = 140
    }

    @Test
    void testCalculatePassengerCost() {
        double cost = cartOperation.calculatePassengerCost(100, 3);
        assertEquals(95.0, cost); // 100*0.2 + 3*25 = 20 + 75 = 95
    }

    @Test
    void testGetMaxPassengers() {
        assertEquals(8, CartOperation.getMaxPassengers());
    }

    @Test
    void testGetMaxCargoWeight() {
        assertEquals(500.0, CartOperation.getMaxCargoWeight());
    }

    @Test
    void testValidateCapacityCargoLimit() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartOperation.validateCapacity(510.0, 0);
        });

        assertTrue(exception.getMessage().contains("Cart cannot carry more than 500,0 kg."));
        assertTrue(exception.getMessage().contains("Requested: 510,0 kg"));
    }

    @Test
    void testValidateCapacityPassengers() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartOperation.validateCapacity(10.0, 9);
        });

        assertEquals("Cart cannot carry more than 8 passengers. Requested: 9", exception.getMessage());
    }
}