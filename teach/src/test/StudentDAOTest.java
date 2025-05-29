import com.example.teach.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentDAOTest extends DatabaseTestBase {

    private StudentDAO studentDao;

    @BeforeEach
    void setUp() throws Exception {
        super.cleanTables();
        studentDao = new StudentDAO();

        // Seed subject
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO Subjects(id, name) VALUES('CS', 'Python')");
        }

        // Seed student and user
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO Users(id, passwordHash, firstName, lastName, role, email) VALUES('S001', 'hashed', 'John', 'Doe', 'student', 'john@example.com')");
            st.executeUpdate("INSERT INTO Students(id) VALUES('S001')");
            st.executeUpdate("INSERT INTO StudentSubjects(student_id, subject_id) VALUES('S001', 'CS')");
        }
    }

    @Test
    void getStudentByIdReturnsCorrectStudent() {
        Student s = studentDao.getStudentById("S001");
        assertNotNull(s);
        assertEquals("S001", s.getId());
        assertEquals("John", s.getFirstName());
        assertEquals("Doe", s.getLastName());
        assertEquals("john@example.com", s.getEmail());
    }

    @Test
    void getStudentsBySubjectReturnsStudentList() {
        List<Student> students = studentDao.getStudentsBySubject("CS");
        assertEquals(1, students.size());
        Student s = students.get(0);
        assertEquals("S001", s.getId());
        assertEquals("John", s.getFirstName());
    }

    @Test
    void getStudentsBySubjectReturnsEmptyIfNone() {
        List<Student> students = studentDao.getStudentsBySubject("NON_EXISTENT");
        assertTrue(students.isEmpty());
    }

    @Test
    void getStudentByIdReturnsNullIfNotExists() {
        assertNull(studentDao.getStudentById("missing_id"));
    }
}
