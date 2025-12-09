package org.students;

import java.io.*;
import java.util.ArrayList;

abstract public class StudentOperation {
    protected ArrayList<Student> studentsFirst = new ArrayList<>();
    protected ArrayList<Student> studentsSecond = new ArrayList<>();
    protected ArrayList<Student> operationResult = new ArrayList<>();

    public ArrayList<Student> getStudentFirst() {
        return studentsFirst;
    }

    public ArrayList<Student> getStudentSecond() {
        return studentsSecond;
    }

    public ArrayList<Student> getOperationResult() {
        return operationResult;
    }

    void readSecondFiles(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                Student student = new Student();
                student.num = Long.parseLong(line.trim());

                line = reader.readLine();
                if (line == null) break;
                student.name = line.trim();

                line = reader.readLine();
                if (line == null) break;
                student.group = Integer.parseInt(line.trim());

                line = reader.readLine();
                if (line == null) break;
                student.grade = Double.parseDouble(line.trim());

                studentsSecond.add(student);
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (NumberFormatException e) {
            System.err.println("Error in the format of numeric data in the file: " + filename);
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException("error when reading files");
        }
    }

    void readFirstFiles(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                Student student = new Student();
                student.num = Long.parseLong(line.trim());

                line = reader.readLine();
                if (line == null) break;
                student.name = line.trim();

                line = reader.readLine();
                if (line == null) break;
                student.group = Integer.parseInt(line.trim());

                line = reader.readLine();
                if (line == null) break;
                student.grade = Double.parseDouble(line.trim());

                studentsFirst.add(student);
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (NumberFormatException e) {
            System.err.println("Error in the format of numeric data in the file: " + filename);
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException("error when reading files");
        }
    }

    void printToFile(String filename, ArrayList<Student> students) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Student student : students) {
                writer.println(student.num);
                writer.println(student.name);
                writer.println(student.group);
                writer.println(student.grade);
            }

        } catch (IOException e) {
            System.err.println("Error writing to a file: " + filename);
            e.printStackTrace();
        }

    }

    abstract void operation();
}
