package org.students;

import java.util.ArrayList;

public class StudentDifference extends StudentOperation {

    @Override
    public void operation() {
        operationResult = new ArrayList<>();
        if (studentsFirst == null || studentsSecond == null) {
            System.out.println("One of the student lists is not initialized");
            return;
        }

        for (Student student : studentsFirst) {
            if (!studentsSecond.contains(student)) {
                operationResult.add(student);
            }
        }

        System.out.println("Difference (First - Second) found. Number of students: " + operationResult.size());

        for (Student student : operationResult) {
            System.out.println(student);
        }
    }
}