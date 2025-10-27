package org.students;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Student Operations")
@Feature("Difference Operation")
class StudentDifferenceTest {

    @Test
    @DisplayName("Should find students only in first list")
    @Description("Test verifies that difference operation correctly finds students present only in the first list")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Find unique students in first list")
    void testDifferenceWithUniqueStudents() {
        // Arrange
        StudentDifference difference = new StudentDifference();
        ArrayList<Student> list1 = new ArrayList<>();
        ArrayList<Student> list2 = new ArrayList<>();

        Student student1 = new Student(1, "John", 101, 4.5);
        Student student2 = new Student(2, "Alice", 102, 3.8);
        Student student3 = new Student(3, "Bob", 101, 4.2);

        list1.add(student1);
        list1.add(student2);
        list1.add(student3);

        list2.add(student3);

        difference.studentsFirst = list1;
        difference.studentsSecond = list2;

        // Act
        difference.operation();

        // Assert
        assertEquals(2, difference.operationResult.size());
        assertTrue(difference.operationResult.contains(student1));
        assertTrue(difference.operationResult.contains(student2));
        assertFalse(difference.operationResult.contains(student3));
    }

    @Test
    @DisplayName("Should return empty list when no unique students in first list")
    @Description("Test verifies that difference returns empty list when all students from first list are in second list")
    @Severity(SeverityLevel.NORMAL)
    @Story("No unique students scenario")
    void testDifferenceNoUniqueStudents() {
        // Arrange
        StudentDifference difference = new StudentDifference();
        ArrayList<Student> list1 = new ArrayList<>();
        ArrayList<Student> list2 = new ArrayList<>();

        Student student1 = new Student(1, "John", 101, 4.5);
        Student student2 = new Student(2, "Alice", 102, 3.8);

        list1.add(student1);
        list1.add(student2);

        list2.add(student1);
        list2.add(student2);
        list2.add(new Student(3, "Bob", 103, 4.2));

        difference.studentsFirst = list1;
        difference.studentsSecond = list2;

        // Act
        difference.operation();

        // Assert
        assertEquals(0, difference.operationResult.size());
        assertTrue(difference.operationResult.isEmpty());
    }

    @Test
    @DisplayName("Should return all students when second list is empty")
    @Description("Test verifies that difference returns all students from first list when second list is empty")
    @Severity(SeverityLevel.NORMAL)
    @Story("Difference with empty second list")
    void testDifferenceWithEmptySecondList() {
        // Arrange
        StudentDifference difference = new StudentDifference();
        ArrayList<Student> list1 = new ArrayList<>();
        ArrayList<Student> list2 = new ArrayList<>();

        Student student1 = new Student(1, "John", 101, 4.5);
        Student student2 = new Student(2, "Alice", 102, 3.8);

        list1.add(student1);
        list1.add(student2);

        difference.studentsFirst = list1;
        difference.studentsSecond = list2;

        // Act
        difference.operation();

        // Assert
        assertEquals(2, difference.operationResult.size());
        assertTrue(difference.operationResult.contains(student1));
        assertTrue(difference.operationResult.contains(student2));
    }
}