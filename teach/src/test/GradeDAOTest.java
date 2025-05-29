
import com.example.teach.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeDAOTest extends DatabaseTestBase {

    private GradeDAO gradeDAO;

    @BeforeEach
    void setUp() throws Exception {
        super.cleanTables();
        gradeDAO = new GradeDAO();

        // Seed required rows for foreign key integrity
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO Subjects(id, name) VALUES('CS', 'Programming')");
            st.executeUpdate("INSERT INTO Assignments(id, subject_id, title, description, due_date, is_released) " +
                    "VALUES('A1', 'CS', 'Assignment 1', 'Do something', '2025-06-10', 1)");
            st.executeUpdate("INSERT INTO Users(id, passwordHash, firstName, lastName, role) " +
                    "VALUES('student123', 'hashed', 'Alice', 'Smith', 'student')");
            st.executeUpdate("INSERT INTO Students(id) VALUES('student123')");
        }
    }

    @Test
    void saveAndFetchGradeByAssignmentAndStudent() {
        Grade grade = new Grade("G1", "A1", "student123", "85%", "Well done", "2025-06-10T12:00");
        boolean success = gradeDAO.saveOrUpdateGrade(grade);
        assertTrue(success);

        Grade stored = gradeDAO.getGradeByAssignmentAndStudent("A1", "student123");
        assertNotNull(stored);
        assertEquals("G1", stored.getId());
        assertEquals("85%", stored.getGrade());
        assertEquals("Well done", stored.getFeedback());
        assertEquals("2025-06-10T12:00", stored.getSubmittedTime());
    }

    @Test
    void updateGradeReplacesPrevious() {
        Grade original = new Grade("G2", "A1", "student123", "75%", "Good effort", "2025-06-10T10:00");
        assertTrue(gradeDAO.saveOrUpdateGrade(original));

        Grade updated = new Grade("G2", "A1", "student123", "92%", "Excellent", "2025-06-10T13:00");
        assertTrue(gradeDAO.saveOrUpdateGrade(updated));

        Grade result = gradeDAO.getGradeByAssignmentAndStudent("A1", "student123");
        assertNotNull(result);
        assertEquals("92%", result.getGrade());
        assertEquals("Excellent", result.getFeedback());
    }

    @Test
    void getGradesForStudentReturnsAllGrades() {
        gradeDAO.saveOrUpdateGrade(new Grade("G3", "A1", "student123", "88%", "Nice work", "2025-06-11T09:00"));

        List<Grade> grades = gradeDAO.getGradesForStudent("student123");
        assertEquals(1, grades.size());

        Grade g = grades.get(0);
        assertEquals("G3", g.getId());
    }

    @Test
    void getGradesForAssignmentReturnsAllGrades() {
        gradeDAO.saveOrUpdateGrade(new Grade("G4", "A1", "student123", "91%", "Very good", "2025-06-11T14:00"));

        List<Grade> grades = gradeDAO.getGradesForAssignment("A1");
        assertEquals(1, grades.size());

        Grade g = grades.get(0);
        assertEquals("G4", g.getId());
        assertEquals("91%", g.getGrade());
    }

    @Test
    void returnsNullIfGradeNotFound() {
        Grade result = gradeDAO.getGradeByAssignmentAndStudent("A1", "nonexistent");
        assertNull(result);
    }
}

