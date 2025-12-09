package org.students;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Student Operations")
@Feature("Intersection Operation")
class StudentIntersectionTest {

    @Test
    @DisplayName("Should find common students in both lists")
    @Description("Test verifies that intersection operation correctly finds students present in both lists")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Find common students")
    void testIntersectionWithCommonStudents() {
        // Arrange
        StudentIntersection intersection = new StudentIntersection();
        ArrayList<Student> list1 = new ArrayList<>();
        ArrayList<Student> list2 = new ArrayList<>();

        Student student1 = new Student(1, "John", 101, 4.5);
        Student student2 = new Student(2, "Alice", 102, 3.8);
        Student student3 = new Student(3, "Bob", 101, 4.2);

        list1.add(student1);
        list1.add(student2);
        list1.add(student3);

        list2.add(student2);
        list2.add(student3);
        list2.add(new Student(4, "Eve", 103, 3.9));

        intersection.studentsFirst = list1;
        intersection.studentsSecond = list2;

        // Act
        intersection.operation();

        // Assert
        assertEquals(2, intersection.operationResult.size());
        assertTrue(intersection.operationResult.contains(student2));
        assertTrue(intersection.operationResult.contains(student3));
        assertFalse(intersection.operationResult.contains(student1));
    }

    @Test
    @DisplayName("Should return empty list when no common students")
    @Description("Test verifies that intersection returns empty list when there are no common students")
    @Severity(SeverityLevel.NORMAL)
    @Story("No common students scenario")
    void testIntersectionNoCommonStudents() {
        // Arrange
        StudentIntersection intersection = new StudentIntersection();
        ArrayList<Student> list1 = new ArrayList<>();
        ArrayList<Student> list2 = new ArrayList<>();

        list1.add(new Student(1, "John", 101, 4.5));
        list1.add(new Student(2, "Alice", 102, 3.8));

        list2.add(new Student(3, "Bob", 103, 4.2));
        list2.add(new Student(4, "Eve", 104, 3.9));

        intersection.studentsFirst = list1;
        intersection.studentsSecond = list2;

        // Act
        intersection.operation();

        // Assert
        assertEquals(0, intersection.operationResult.size());
        assertTrue(intersection.operationResult.isEmpty());
    }

    @Test
    @DisplayName("Should return all students when lists are identical")
    @Description("Test verifies that intersection returns all students when both lists are identical")
    @Severity(SeverityLevel.NORMAL)
    @Story("Identical lists scenario")
    void testIntersectionIdenticalLists() {
        // Arrange
        StudentIntersection intersection = new StudentIntersection();
        ArrayList<Student> list1 = new ArrayList<>();
        ArrayList<Student> list2 = new ArrayList<>();

        Student student1 = new Student(1, "John", 101, 4.5);
        Student student2 = new Student(2, "Alice", 102, 3.8);

        list1.add(student1);
        list1.add(student2);
        list2.add(student1);
        list2.add(student2);

        intersection.studentsFirst = list1;
        intersection.studentsSecond = list2;

        // Act
        intersection.operation();

        // Assert
        assertEquals(2, intersection.operationResult.size());
        assertTrue(intersection.operationResult.contains(student1));
        assertTrue(intersection.operationResult.contains(student2));
    }
}