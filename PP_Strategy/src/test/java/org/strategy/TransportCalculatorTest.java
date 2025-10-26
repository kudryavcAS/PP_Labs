package org.strategy;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Transport Calculator")
@Feature("Calculator Management")
class TransportCalculatorTest {

    @Test
    @DisplayName("Create calculator for car")
    @Story("Calculator Initialization")
    @Severity(SeverityLevel.CRITICAL)
    void testCarCalculatorCreation() {
        Allure.step("Create calculator for car");
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);

        Allure.step("Verify calculator is created");
        assertNotNull(calculator);

        Allure.step("Verify transport name is 'Car'");
        assertEquals("Car", calculator.getTransportName());
    }

    @Test
    @DisplayName("Create calculator for bicycle")
    @Story("Calculator Initialization")
    @Severity(SeverityLevel.CRITICAL)
    void testBicycleCalculatorCreation() {
        Allure.step("Create calculator for bicycle");
        TransportCalculator calculator = new TransportCalculator(TransportType.BICYCLE);

        Allure.step("Verify calculator is created");
        assertNotNull(calculator);

        Allure.step("Verify transport name is 'Bicycle'");
        assertEquals("Bicycle", calculator.getTransportName());
    }

    @Test
    @DisplayName("Create calculator for cart")
    @Story("Calculator Initialization")
    @Severity(SeverityLevel.CRITICAL)
    void testCartCalculatorCreation() {
        Allure.step("Create calculator for cart");
        TransportCalculator calculator = new TransportCalculator(TransportType.CART);

        Allure.step("Verify calculator is created");
        assertNotNull(calculator);

        Allure.step("Verify transport name is 'Cart'");
        assertEquals("Cart", calculator.getTransportName());
    }

    @Test
    @DisplayName("Successful trip calculation for car")
    @Story("Trip Calculation")
    @Severity(SeverityLevel.BLOCKER)
    void testCarCalculatorValidTrip() {
        Allure.step("Create car calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);

        Allure.step("Calculate trip with valid parameters (100 km, 50 kg, 2 passengers)");
        assertDoesNotThrow(() -> calculator.calculateFullTrip(100, 50, 2));
    }

    @Test
    @DisplayName("Successful trip calculation for bicycle")
    @Story("Trip Calculation")
    @Severity(SeverityLevel.BLOCKER)
    void testBicycleCalculatorValidTrip() {
        Allure.step("Create bicycle calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.BICYCLE);

        Allure.step("Calculate trip with valid parameters (10 km, 5 kg, 0 passengers)");
        assertDoesNotThrow(() -> calculator.calculateFullTrip(10, 5, 0));
    }

    @Test
    @DisplayName("Successful trip calculation for cart")
    @Story("Trip Calculation")
    @Severity(SeverityLevel.BLOCKER)
    void testCartCalculatorValidTrip() {
        Allure.step("Create cart calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.CART);

        Allure.step("Calculate trip with valid parameters (50 km, 200 kg, 4 passengers)");
        assertDoesNotThrow(() -> calculator.calculateFullTrip(50, 200, 4));
    }

    @Test
    @DisplayName("Error when car exceeds passenger limit")
    @Story("Capacity Validation")
    @Severity(SeverityLevel.NORMAL)
    void testCarCalculatorExceedPassengers() {
        Allure.step("Create car calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);

        Allure.step("Try to calculate trip with 5 passengers (exceeds 4 passenger limit)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFullTrip(100, 50, 5);
        });

        Allure.step("Verify error message contains passenger limit");
        assertTrue(exception.getMessage().contains("cannot carry more than 4 passengers"));
    }

    @Test
    @DisplayName("Error when bicycle has passengers")
    @Story("Capacity Validation")
    @Severity(SeverityLevel.NORMAL)
    void testBicycleCalculatorWithPassengers() {
        Allure.step("Create bicycle calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.BICYCLE);

        Allure.step("Try to calculate trip with 1 passenger (bicycle cannot carry passengers)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFullTrip(10, 5, 1);
        });

        Allure.step("Verify error message about passengers");
        assertTrue(exception.getMessage().contains("cannot carry passengers"));
    }

    @Test
    @DisplayName("Error when cart exceeds cargo limit")
    @Story("Capacity Validation")
    @Severity(SeverityLevel.NORMAL)
    void testCartCalculatorExceedCargo() {
        Allure.step("Create cart calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.CART);

        Allure.step("Try to calculate trip with 600 kg cargo (exceeds 500 kg limit)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFullTrip(50, 600, 4);
        });

        Allure.step("Verify error message contains cargo limit");
        assertTrue(exception.getMessage().contains("Cart cannot carry more than 500,0 kg"));
    }

    @Test
    @DisplayName("Error with negative distance")
    @Story("Input Validation")
    @Severity(SeverityLevel.MINOR)
    void testNegativeDistance() {
        Allure.step("Create car calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);

        Allure.step("Try to calculate trip with negative distance (-100 km)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFullTrip(-100, 50, 2);
        });

        Allure.step("Verify error message about negative distance");
        assertEquals("Distance cannot be negative", exception.getMessage());
    }

    @Test
    @DisplayName("Error with negative cargo weight")
    @Story("Input Validation")
    @Severity(SeverityLevel.MINOR)
    void testNegativeCargo() {
        Allure.step("Create car calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);

        Allure.step("Try to calculate trip with negative cargo weight (-50 kg)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFullTrip(100, -50, 2);
        });

        Allure.step("Verify error message about negative cargo");
        assertEquals("Cargo weight cannot be negative", exception.getMessage());
    }

    @Test
    @DisplayName("Error with negative passengers")
    @Story("Input Validation")
    @Severity(SeverityLevel.MINOR)
    void testNegativePassengers() {
        Allure.step("Create car calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);

        Allure.step("Try to calculate trip with negative passengers (-1)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFullTrip(100, 50, -1);
        });

        Allure.step("Verify error message about negative passengers");
        assertEquals("Passengers count cannot be negative", exception.getMessage());
    }

    @Test
    @DisplayName("Calculate time for car")
    @Story("Individual Calculations")
    @Severity(SeverityLevel.CRITICAL)
    void testGetTimeValid() {
        Allure.step("Create car calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);

        Allure.step("Calculate time for 100 km and 50 kg cargo");
        double time = calculator.getTime(100, 50);

        Allure.step("Verify time is positive");
        assertTrue(time > 0);
    }

    @Test
    @DisplayName("Calculate cargo cost for car")
    @Story("Individual Calculations")
    @Severity(SeverityLevel.CRITICAL)
    void testGetCargoCostValid() {
        Allure.step("Create car calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);

        Allure.step("Calculate cargo cost for 100 km and 50 kg cargo");
        double cost = calculator.getCargoCost(100, 50);

        Allure.step("Verify cost is 225.0");
        assertEquals(225.0, cost);
    }

    @Test
    @DisplayName("Calculate passenger cost for car")
    @Story("Individual Calculations")
    @Severity(SeverityLevel.CRITICAL)
    void testGetPassengerCostValid() {
        Allure.step("Create car calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);

        Allure.step("Calculate passenger cost for 100 km and 3 passengers");
        double cost = calculator.getPassengerCost(100, 3);

        Allure.step("Verify cost is 200.0");
        assertEquals(200.0, cost);
    }

    @Test
    @DisplayName("Error when calculating time with negative distance")
    @Story("Input Validation")
    @Severity(SeverityLevel.MINOR)
    void testGetTimeNegativeDistance() {
        Allure.step("Create car calculator");
        TransportCalculator calculator = new TransportCalculator(TransportType.CAR);

        Allure.step("Try to calculate time with negative distance (-100 km)");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.getTime(-100, 50);
        });

        Allure.step("Verify error message about negative parameters");
        assertEquals("Distance and cargo weight cannot be negative", exception.getMessage());
    }
}