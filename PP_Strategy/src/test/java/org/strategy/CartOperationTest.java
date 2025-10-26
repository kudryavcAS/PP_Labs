package org.strategy;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Transport Calculator")
@Feature("Cart Transportation Operations")
class CartOperationTest {

    private final CartOperation cartOperation = new CartOperation();

    @Test
    @DisplayName("Calculate time for cart under normal conditions")
    @Story("Time Calculation")
    @Severity(SeverityLevel.CRITICAL)
    void testCalculateTimeNormalCase() {
        Allure.step("Calculate time for 80 km distance and 200 kg cargo");
        double time = cartOperation.calculateTime(80, 200);

        Allure.step("Verify time is positive");
        assertTrue(time > 0);

        Allure.step("Verify time is approximately 14.0 hours");
        assertEquals(14.0, time, 0.1);
    }

    @Test
    @DisplayName("Compare time with heavy vs light cargo for cart")
    @Story("Time Calculation")
    @Severity(SeverityLevel.NORMAL)
    void testCalculateTimeDifference() {
        Allure.step("Calculate time with heavy cargo (400 kg)");
        double timeWithHeavyCargo = cartOperation.calculateTime(50, 400);

        Allure.step("Calculate time with light cargo (100 kg)");
        double timeWithLightCargo = cartOperation.calculateTime(50, 100);

        Allure.step("Verify heavy cargo takes more time");
        assertTrue(timeWithHeavyCargo > timeWithLightCargo);
    }

    @Test
    @DisplayName("Calculate cargo cost for cart")
    @Story("Cost Calculation")
    @Severity(SeverityLevel.CRITICAL)
    void testCalculateCargoCost() {
        Allure.step("Calculate cargo cost for 100 km and 200 kg");
        double cost = cartOperation.calculateCargoCost(100, 200);

        Allure.step("Verify cost is 140.0");
        assertEquals(140.0, cost);
    }

    @Test
    @DisplayName("Calculate passenger cost for cart")
    @Story("Cost Calculation")
    @Severity(SeverityLevel.CRITICAL)
    void testCalculatePassengerCost() {
        Allure.step("Calculate passenger cost for 100 km and 3 passengers");
        double cost = cartOperation.calculatePassengerCost(100, 3);

        Allure.step("Verify cost is 95.0");
        assertEquals(95.0, cost);
    }

    @Test
    @DisplayName("Get maximum passengers for cart")
    @Story("Capacity Information")
    @Severity(SeverityLevel.NORMAL)
    void testGetMaxPassengers() {
        Allure.step("Get maximum passengers for cart");
        assertEquals(8, CartOperation.getMaxPassengers());
    }

    @Test
    @DisplayName("Get maximum cargo weight for cart")
    @Story("Capacity Information")
    @Severity(SeverityLevel.NORMAL)
    void testGetMaxCargoWeight() {
        Allure.step("Get maximum cargo weight for cart");
        assertEquals(500.0, CartOperation.getMaxCargoWeight());
    }

    @Test
    @DisplayName("Validate capacity when cargo limit exceeded for cart")
    @Story("Capacity Validation")
    @Severity(SeverityLevel.NORMAL)
    void testValidateCapacityCargoLimit() {
        Allure.parameter("Requested Cargo Weight", 510.0);
        Allure.parameter("Maximum Cargo Weight", 500.0);

        Allure.step("Try to validate 510 kg cargo (exceeds 500 kg limit)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartOperation.validateCapacity(510.0, 0);
        });

        Allure.step("Verify error message contains cargo limit information");
        assertTrue(exception.getMessage().contains("Cart cannot carry more than 500,0 kg."));
        assertTrue(exception.getMessage().contains("Requested: 510,0 kg"));
    }

    @Test
    @DisplayName("Validate capacity when passenger limit exceeded for cart")
    @Story("Capacity Validation")
    @Severity(SeverityLevel.NORMAL)
    void testValidateCapacityPassengers() {
        Allure.parameter("Requested Passengers", 9);
        Allure.parameter("Maximum Passengers", 8);

        Allure.step("Try to validate 9 passengers (exceeds 8 passenger limit)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartOperation.validateCapacity(10.0, 9);
        });

        Allure.step("Verify error message contains passenger limit information");
        assertEquals("Cart cannot carry more than 8 passengers. Requested: 9", exception.getMessage());
    }
}