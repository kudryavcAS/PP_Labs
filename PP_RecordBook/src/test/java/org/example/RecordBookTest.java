package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.qameta.allure.*;

import static org.junit.jupiter.api.Assertions.*;

@Epic("RecordBook Management")
@Feature("Student Academic Performance")
class RecordBookTest {

    @Test
    @DisplayName("Student with all exams >= 9 and all passes = 1 should be excellent")
    @Story("Excellent student validation")
    @Severity(SeverityLevel.CRITICAL)
    void testExcellentStudentAllHighGrades() {

        RecordBook recordBook = new RecordBook("Ivanov", "Petr", 2, "I-1");
        RecordBook.Session session = recordBook.addSession(1);
        session.addSubject("Mathematics", 9, true);
        session.addSubject("Physics", 10, true);
        session.addSubject("History", 1, false);

        assertTrue(recordBook.isExcellently(),
                "Student with all exams >= 9 and passes = 1 should be excellent");
    }

    @Test
    @DisplayName("Student with exam grade 8 should not be excellent")
    @Story("Excellent student validation")
    @Severity(SeverityLevel.CRITICAL)
    void testNotExcellentDueToLowExamGrade() {
        RecordBook recordBook = new RecordBook("Sidorov", "Alexey", 2, "PI-11");
        RecordBook.Session session = recordBook.addSession(1);
        session.addSubject("Mathematics", 8, true);
        session.addSubject("Physics", 10, true);
        session.addSubject("History", 1, false);

        assertFalse(recordBook.isExcellently(),
                "Student with exam grade < 9 should not be excellent");
    }

    @Test
    @DisplayName("Student with failed passes (grade 0) should not be excellent")
    @Story("Excellent student validation")
    @Severity(SeverityLevel.CRITICAL)
    void testNotExcellentDueToFailedpasses() {
        RecordBook recordBook = new RecordBook("Kozlov", "Dmitry", 3, "PM-6");
        RecordBook.Session session = recordBook.addSession(1);
        session.addSubject("Mathematics", 10, true);
        session.addSubject("Physics", 9, true);
        session.addSubject("History", 0, false);

        assertFalse(recordBook.isExcellently(),
                "Student with failed passes should not be excellent");
    }

    @Test
    @DisplayName("Student with multiple sessions all excellent should be excellent")
    @Story("Multiple sessions handling")
    @Severity(SeverityLevel.NORMAL)
    void testExcellentStudentWithMultipleSessions() {
        RecordBook recordBook = new RecordBook("Petrova", "Maria", 1, "KB-9");

        RecordBook.Session session1 = recordBook.addSession(1);
        session1.addSubject("Mathematics", 9, true);
        session1.addSubject("Physics", 10, true);
        session1.addSubject("History", 1, false);

        RecordBook.Session session2 = recordBook.addSession(2);
        session2.addSubject("Programming", 9, true);
        session2.addSubject("Chemistry", 10, true);
        session2.addSubject("English", 1, false);

        assertTrue(recordBook.isExcellently(),
                "Student with all sessions excellent should be excellent");
    }

    @Test
    @DisplayName("Student with one failed subject in second session should not be excellent")
    @Story("Multiple sessions handling")
    @Severity(SeverityLevel.NORMAL)
    void testNotExcellentDueToFailedSubjectInLaterSession() {
        RecordBook recordBook = new RecordBook("Smirnov", "Igor", 2, "I-2");

        RecordBook.Session session1 = recordBook.addSession(1);
        session1.addSubject("Mathematics", 10, true);
        session1.addSubject("Physics", 10, true);

        RecordBook.Session session2 = recordBook.addSession(2);
        session2.addSubject("Programming", 8, true);
        session2.addSubject("Chemistry", 10, true);

        assertFalse(recordBook.isExcellently(),
                "Student with failed subject in any session should not be excellent");
    }

    @Test
    @DisplayName("Student with only passes all passed should be excellent")
    @Story("Pass-only subjects")
    @Severity(SeverityLevel.NORMAL)
    void testExcellentStudentWithOnlypasses() {
        RecordBook recordBook = new RecordBook("Novikov", "Sergey", 1, "I-3");
        RecordBook.Session session = recordBook.addSession(1);
        session.addSubject("Physical Education", 1, false);
        session.addSubject("History", 1, false);
        session.addSubject("Philosophy", 1, false);

        assertTrue(recordBook.isExcellently(),
                "Student with all passes passed should be excellent");
    }

    @Test
    @DisplayName("Student with passes grade 2 should not be excellent")
    @Story("Pass validation")
    @Severity(SeverityLevel.NORMAL)
    void testNotExcellentDueToInvalidpassesGrade() {
        RecordBook recordBook = new RecordBook("Frolov", "Andrey", 2, "IK-12");
        RecordBook.Session session = recordBook.addSession(1);
        session.addSubject("Mathematics", 9, true);
        session.addSubject("Physics", 10, true);
        session.addSubject("History", 0, false);

        assertFalse(recordBook.isExcellently(),
                "Student with passes grade != 1 should not be excellent");
    }

    @Test
    @DisplayName("Empty record book should not be excellent")
    @Story("Edge cases")
    @Severity(SeverityLevel.MINOR)
    void testEmptyRecordBook() {
        RecordBook recordBook = new RecordBook("Empty", "Student", 1, "AI-1");

        assertTrue(recordBook.isExcellently(),
                "Empty record book should be considered excellent (no failed subjects)");
    }
}