package org.example;

import java.io.*;
import java.util.*;

public class RecordBookDemo {

    public static void main(String[] args) {
        List<RecordBook> recordBooks = readFromFile("input.txt");

        writeExcellentStudents(recordBooks, "output.txt");
    }

    public static List<RecordBook> readFromFile(String filename) {
        List<RecordBook> recordBooks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            RecordBook currentRecordBook = null;
            RecordBook.Session currentSession = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    if (currentRecordBook != null) {
                        recordBooks.add(currentRecordBook);
                        currentRecordBook = null;
                    }
                    continue;
                }

                if (line.matches("[1-9]")) {
                    int sessionNumber = Integer.parseInt(line);
                    if (currentRecordBook != null) {
                        currentSession = currentRecordBook.addSession(sessionNumber);
                    }
                    continue;
                }

                String[] studentData = line.split(",");
                if (studentData.length >= 3) {
                    String[] nameParts = studentData[0].trim().split(" ");
                    if (nameParts.length >= 2) {
                        String lastName = nameParts[0];
                        String firstName = nameParts[1];

                        int course;
                        try {
                            course = Integer.parseInt(studentData[1].trim());
                        } catch (NumberFormatException e) {
                            course = 0;
                        }

                        String group = studentData[2].trim();

                        currentRecordBook = new RecordBook(lastName, firstName, course, group);
                    }
                    continue;
                }

                if (line.contains("-")) {
                    String[] subjectData = line.split("-");
                    if (subjectData.length == 2) {
                        String subjectName = subjectData[0].trim();
                        String gradeStr = subjectData[1].trim();

                        try {
                            int grade = Integer.parseInt(gradeStr);
                            boolean isExam = grade >= 2;

                            if (currentSession != null) {
                                currentSession.addSubject(subjectName, grade, isExam);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Grade format error: " + gradeStr);
                        }
                    }
                }
            }

            if (currentRecordBook != null) {
                recordBooks.add(currentRecordBook);
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Data format error: " + e.getMessage());
        }

        return recordBooks;
    }

    public static void writeExcellentStudents(List<RecordBook> recordBooks, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("EXCELLENT STUDENTS");
            writer.println("(all grades >= 9)");
            writer.println("==================");

            int excellentCount = 0;

            for (RecordBook student : recordBooks) {
                if (student.isExcellently()) {
                    writer.println("\n" + student.toString());
                    excellentCount++;
                }
            }

            if (excellentCount == 0) {
                writer.println("No excellent students found");
            } else {
                writer.println("\nTotal excellent students: " + excellentCount);
            }

        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}