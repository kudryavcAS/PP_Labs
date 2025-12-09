package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.util.*;

public class RecordBookDemo {

    public static void main(String[] args) {
        List<RecordBook> recordBooks = readFromJsonFile("input.json");

        writeExcellentStudentsToJson(recordBooks, "output.json");
        writeExcellentStudents(recordBooks, "output.txt");
    }

    public static List<RecordBook> readFromJsonFile(String filename) {
        List<RecordBook> recordBooks = new ArrayList<>();

        try (FileReader reader = new FileReader(filename)) {
            Gson gson = new Gson();

            JsonStudent[] jsonStudents = gson.fromJson(reader, JsonStudent[].class);

            for (JsonStudent jsonStudent : jsonStudents) {
                RecordBook recordBook = new RecordBook(
                        jsonStudent.lastName,
                        jsonStudent.firstName,
                        jsonStudent.course,
                        jsonStudent.group
                );

                for (JsonSession jsonSession : jsonStudent.sessions) {
                    RecordBook.Session session = recordBook.addSession(jsonSession.sessionNumber);

                    for (JsonSubject jsonSubject : jsonSession.subjects) {
                        session.addSubject(
                                jsonSubject.name,
                                jsonSubject.grade,
                                jsonSubject.isExam
                        );
                    }
                }

                recordBooks.add(recordBook);
            }

        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }

        return recordBooks;
    }

    public static void writeExcellentStudentsToJson(List<RecordBook> recordBooks, String filename) {
        List<JsonStudent> excellentStudents = new ArrayList<>();

        for (RecordBook student : recordBooks) {
            if (student.isExcellently()) {
                excellentStudents.add(convertToJsonStudent(student));
            }
        }

        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(excellentStudents, writer);
            System.out.println("Excellent students written to: " + filename);

        } catch (IOException e) {
            System.out.println("Error writing JSON file: " + e.getMessage());
        }
    }

    private static JsonStudent convertToJsonStudent(RecordBook recordBook) {
        JsonStudent jsonStudent = new JsonStudent();
        jsonStudent.lastName = recordBook.getStudentLastName();
        jsonStudent.firstName = recordBook.getStudentFirstName();
        jsonStudent.course = recordBook.getCourse();
        jsonStudent.group = recordBook.getGroup();
        jsonStudent.sessions = new ArrayList<>();

        for (RecordBook.Session session : recordBook.getSessions()) {
            JsonSession jsonSession = new JsonSession();
            jsonSession.sessionNumber = session.getSessionNumber();
            jsonSession.subjects = new ArrayList<>();

            for (RecordBook.Session.Subject subject : session.getSubjects()) {
                JsonSubject jsonSubject = new JsonSubject();
                jsonSubject.name = subject.getName();
                jsonSubject.grade = subject.getGrade();
                jsonSubject.isExam = subject.isExam();
                jsonSession.subjects.add(jsonSubject);
            }

            jsonStudent.sessions.add(jsonSession);
        }

        return jsonStudent;
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

    static class JsonStudent {
        String lastName;
        String firstName;
        int course;
        String group;
        List<JsonSession> sessions;
    }

    static class JsonSession {
        int sessionNumber;
        List<JsonSubject> subjects;
    }

    static class JsonSubject {
        String name;
        int grade;
        boolean isExam;
    }
}