package org.students;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Student Operations")
@Feature("Union Operation")
class StudentUnionTest {

    @Test
    @DisplayName("Should combine all unique students from both lists")
    @Description("Test verifies that union operation combines all unique students from both lists without duplicates")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Combine unique students")
    void testUnionWithDifferentStudents() {
        // Arrange
        StudentUnion union = new StudentUnion();
        ArrayList<Student> list1 = new ArrayList<>();
        ArrayList<Student> list2 = new ArrayList<>();

        Student student1 = new Student(1, "John", 101, 4.5);
        Student student2 = new Student(2, "Alice", 102, 3.8);
        Student student3 = new Student(3, "Bob", 101, 4.2);
        Student student4 = new Student(4, "Eve", 103, 3.9);

        list1.add(student1);
        list1.add(student2);

        list2.add(student3);
        list2.add(student4);

        union.studentsFirst = list1;
        union.studentsSecond = list2;

        // Act
        union.operation();

        // Assert
        assertEquals(4, union.operationResult.size());
        assertTrue(union.operationResult.contains(student1));
        assertTrue(union.operationResult.contains(student2));
        assertTrue(union.operationResult.contains(student3));
        assertTrue(union.operationResult.contains(student4));
    }

    @Test
    @DisplayName("Should remove duplicates when students overlap")
    @Description("Test verifies that union operation removes duplicate students when they appear in both lists")
    @Severity(SeverityLevel.NORMAL)
    @Story("Handle overlapping students")
    void testUnionWithOverlappingStudents() {
        // Arrange
        StudentUnion union = new StudentUnion();
        ArrayList<Student> list1 = new ArrayList<>();
        ArrayList<Student> list2 = new ArrayList<>();

        Student student1 = new Student(1, "John", 101, 4.5);
        Student student2 = new Student(2, "Alice", 102, 3.8);
        Student student3 = new Student(3, "Bob", 101, 4.2);

        list1.add(student1);
        list1.add(student2);

        list2.add(student2);
        list2.add(student3);

        union.studentsFirst = list1;
        union.studentsSecond = list2;

        // Act
        union.operation();

        // Assert
        assertEquals(3, union.operationResult.size());
        assertTrue(union.operationResult.contains(student1));
        assertTrue(union.operationResult.contains(student2));
        assertTrue(union.operationResult.contains(student3));
    }

    @Test
    @DisplayName("Should handle union with empty second list")
    @Description("Test verifies that union operation works correctly when one list is empty")
    @Severity(SeverityLevel.NORMAL)
    @Story("Union with empty list")
    void testUnionWithEmptyLists() {
        // Arrange
        StudentUnion union = new StudentUnion();
        ArrayList<Student> list1 = new ArrayList<>();
        ArrayList<Student> list2 = new ArrayList<>();

        list1.add(new Student(1, "John", 101, 4.5));
        list1.add(new Student(2, "Alice", 102, 3.8));

        union.studentsFirst = list1;
        union.studentsSecond = list2;

        // Act
        union.operation();

        // Assert
        assertEquals(2, union.operationResult.size());
    }
}