package org.strategy;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Transport Calculator")
@Feature("Bicycle Transportation Operations")
class BicycleOperationTest {

    private final BicycleOperation bicycleOperation = new BicycleOperation();

    @Test
    @DisplayName("Calculate time for bicycle with cargo")
    @Story("Time Calculation")
    @Severity(SeverityLevel.CRITICAL)
    void testCalculateTimeWithCargo() {
        Allure.step("Calculate time for 30 km distance and 10 kg cargo");
        double time = bicycleOperation.calculateTime(30, 10);

        Allure.step("Verify time is positive");
        assertTrue(time > 0);
    }

    @Test
    @DisplayName("Get maximum passengers for bicycle (always zero)")
    @Story("Capacity Information")
    @Severity(SeverityLevel.NORMAL)
    void testGetMaxPassengersAlwaysZero() {
        Allure.step("Get maximum passengers for bicycle");
        assertEquals(0, BicycleOperation.getMaxPassengers());
    }

    @Test
    @DisplayName("Get maximum cargo weight for bicycle")
    @Story("Capacity Information")
    @Severity(SeverityLevel.NORMAL)
    void testGetMaxCargoWeight() {
        Allure.step("Get maximum cargo weight for bicycle");
        assertEquals(50.0, BicycleOperation.getMaxCargoWeight());
    }

    @Test
    @DisplayName("Validate capacity when cargo limit exceeded for bicycle")
    @Story("Capacity Validation")
    @Severity(SeverityLevel.NORMAL)
    void testValidateCapacityCargoLimit() {
        Allure.parameter("Requested Cargo Weight", 60.0);
        Allure.parameter("Maximum Cargo Weight", 50.0);

        Allure.step("Try to validate 60 kg cargo (exceeds 50 kg limit)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bicycleOperation.validateCapacity(60.0, 0);
        });

        Allure.step("Verify error message contains cargo limit information");
        assertTrue(exception.getMessage().contains("Bicycle cannot carry more than 50,0 kg."));
        assertTrue(exception.getMessage().contains("Requested: 60,0 kg"));
    }

    @Test
    @DisplayName("Validate capacity when bicycle has passengers")
    @Story("Capacity Validation")
    @Severity(SeverityLevel.NORMAL)
    void testValidateCapacityPassengers() {
        Allure.parameter("Requested Passengers", 1);
        Allure.parameter("Maximum Passengers", 0);

        Allure.step("Try to validate 1 passenger (bicycle cannot carry passengers)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bicycleOperation.validateCapacity(10.0, 1);
        });

        Allure.step("Verify error message about passengers");
        assertEquals("Bicycle cannot carry passengers", exception.getMessage());
    }
}