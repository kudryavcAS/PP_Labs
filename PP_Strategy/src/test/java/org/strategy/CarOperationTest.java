package org.strategy;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Transport Calculator")
@Feature("Car Transportation Operations")
class CarOperationTest {

    private final CarOperation carOperation = new CarOperation();

    @Test
    @DisplayName("Calculate time for car under normal conditions")
    @Story("Time Calculation")
    @Severity(SeverityLevel.CRITICAL)
    void testCalculateTimeNormalCase() {
        Allure.step("Calculate time for 100 km distance and 50 kg cargo");
        double time = carOperation.calculateTime(100, 50);

        Allure.step("Verify time is positive");
        assertTrue(time > 0);

        Allure.step("Verify time is approximately 1.25 hours");
        assertEquals(1.25, time, 0.1);
    }

    @Test
    @DisplayName("Compare time with heavy vs light cargo")
    @Story("Time Calculation")
    @Severity(SeverityLevel.NORMAL)
    void testCalculateTimeDifference() {
        Allure.step("Calculate time with heavy cargo (300 kg)");
        double timeWithHeavyCargo = carOperation.calculateTime(100, 300);

        Allure.step("Calculate time with light cargo (10 kg)");
        double timeWithLightCargo = carOperation.calculateTime(100, 10);

        Allure.step("Verify heavy cargo takes more time");
        assertTrue(timeWithHeavyCargo > timeWithLightCargo);
    }

    @Test
    @DisplayName("Calculate cargo cost for car")
    @Story("Cost Calculation")
    @Severity(SeverityLevel.CRITICAL)
    void testCalculateCargoCost() {
        Allure.step("Calculate cargo cost for 100 km and 50 kg");
        double cost = carOperation.calculateCargoCost(100, 50);

        Allure.step("Verify cost is 225.0");
        assertEquals(225.0, cost);
    }

    @Test
    @DisplayName("Calculate passenger cost for car")
    @Story("Cost Calculation")
    @Severity(SeverityLevel.CRITICAL)
    void testCalculatePassengerCost() {
        Allure.step("Calculate passenger cost for 100 km and 3 passengers");
        double cost = carOperation.calculatePassengerCost(100, 3);

        Allure.step("Verify cost is 200.0");
        assertEquals(200.0, cost);
    }

    @Test
    @DisplayName("Get maximum passengers for car")
    @Story("Capacity Information")
    @Severity(SeverityLevel.NORMAL)
    void testGetMaxPassengers() {
        Allure.step("Get maximum passengers for car");
        assertEquals(4, CarOperation.getMaxPassengers());
    }

    @Test
    @DisplayName("Get maximum cargo weight for car")
    @Story("Capacity Information")
    @Severity(SeverityLevel.NORMAL)
    void testGetMaxCargoWeight() {
        Allure.step("Get maximum cargo weight for car");
        assertEquals(350.0, CarOperation.getMaxCargoWeight());
    }

    @Test
    @DisplayName("Validate capacity when cargo limit exceeded")
    @Story("Capacity Validation")
    @Severity(SeverityLevel.NORMAL)
    void testValidateCapacityCargoLimit() {
        Allure.parameter("Requested Cargo Weight", 360.0);
        Allure.parameter("Maximum Cargo Weight", 350.0);

        Allure.step("Try to validate 360 kg cargo (exceeds 350 kg limit)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carOperation.validateCapacity(360.0, 0);
        });

        Allure.step("Verify error message contains cargo limit information");
        assertTrue(exception.getMessage().contains("Car cannot carry more than 350,0 kg."));
        assertTrue(exception.getMessage().contains("Requested: 360,0 kg"));
    }

    @Test
    @DisplayName("Validate capacity when passenger limit exceeded")
    @Story("Capacity Validation")
    @Severity(SeverityLevel.NORMAL)
    void testValidateCapacityPassengers() {
        Allure.parameter("Requested Passengers", 5);
        Allure.parameter("Maximum Passengers", 4);

        Allure.step("Try to validate 5 passengers (exceeds 4 passenger limit)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carOperation.validateCapacity(10.0, 5);
        });

        Allure.step("Verify error message contains passenger limit information");
        assertEquals("Car cannot carry more than 4 passengers. Requested: 5", exception.getMessage());
    }
}