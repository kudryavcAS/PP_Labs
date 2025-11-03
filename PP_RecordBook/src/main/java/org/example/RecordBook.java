package org.example;

import java.util.*;

public class RecordBook {
    private final String studentLastName;
    private final String studentFirstName;
    private final int course;
    private final String group;
    private final List<Session> sessions;

    public RecordBook(String lastName, String firstName, int course, String group) {
        this.studentLastName = lastName;
        this.studentFirstName = firstName;
        this.course = course;
        this.group = group;
        this.sessions = new ArrayList<>();
    }

    public static class Session {
        private final int sessionNumber;
        private final List<Subject> subjects;

        public Session(int sessionNumber) {
            this.sessionNumber = sessionNumber;
            this.subjects = new ArrayList<>();
        }

        public void addSubject(String subjectName, int grade, boolean isExam) {
            subjects.add(new Subject(subjectName, grade, isExam));
        }

        public List<Subject> getSubjects() {
            return subjects;
        }

        public int getSessionNumber() {
            return sessionNumber;
        }

        public static class Subject {
            private final String name;
            private final int grade;
            private final boolean isExam;

            public Subject(String name, int grade, boolean isExam) {
                this.name = name;
                this.grade = grade;
                this.isExam = isExam;
            }

            public String getName() {
                return name;
            }

            public int getGrade() {
                return grade;
            }

            public boolean isExam() {
                return isExam;
            }

            public String getGradeStatus() {
                if (isExam) {
                    return String.valueOf(grade);
                } else {
                    return grade > 0 ? "passed" : "failed";
                }
            }
        }
    }

    public Session addSession(int sessionNumber) {
        Session session = new Session(sessionNumber);
        sessions.add(session);
        return session;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public boolean isExcellently() {
        for (Session session : sessions) {
            for (Session.Subject subject : session.getSubjects()) {
                if (subject.getGrade() < 9 && subject.getGrade() != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public String getFullName() {
        return studentLastName + " " + studentFirstName;
    }

    public int getCourse() {
        return course;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s %s, course: %d, group: %s\n",
                studentLastName, studentFirstName, course, group));

        for (Session session : sessions) {
            sb.append(String.format("Session %d:\n", session.getSessionNumber()));

            for (Session.Subject subject : session.getSubjects()) {
                sb.append(String.format("  %s - %s\n",
                        subject.getName(), subject.getGradeStatus()));
            }
        }

        return sb.toString();
    }
}