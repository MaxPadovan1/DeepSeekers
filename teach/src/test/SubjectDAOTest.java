import com.example.teach.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubjectDAOTest extends DatabaseTestBase {

    private SubjectDAO subjectDao;

    @BeforeEach
    void setUp() throws Exception {
        super.cleanTables();  // clean database tables
        subjectDao = new SubjectDAO();

        // Seed some subjects for testing
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO Subjects(id, name) VALUES('MATH', 'Mathematics')");
            st.executeUpdate("INSERT INTO Subjects(id, name) VALUES('SCI', 'Science')");
        }
    }

    @Test
    void findByIdReturnsCorrectSubject() throws Exception {
        Subject math = subjectDao.findById("MATH");
        assertNotNull(math);
        assertEquals("MATH", math.getId());
        assertEquals("Mathematics", math.getName());

        Subject sci = subjectDao.findById("SCI");
        assertNotNull(sci);
        assertEquals("SCI", sci.getId());
        assertEquals("Science", sci.getName());
    }

    @Test
    void findByIdReturnsNullIfNotFound() throws Exception {
        Subject notExist = subjectDao.findById("UNKNOWN");
        assertNull(notExist);
    }

    @Test
    void getAllReturnsAllSubjects() throws Exception {
        List<Subject> subjects = subjectDao.getAll();
        assertEquals(2, subjects.size());

        assertTrue(subjects.stream().anyMatch(s -> s.getId().equals("MATH") && s.getName().equals("Mathematics")));
        assertTrue(subjects.stream().anyMatch(s -> s.getId().equals("SCI") && s.getName().equals("Science")));
    }

    @Test
    void getAllReturnsEmptyListIfNoSubjects() throws Exception {
        // Clean subjects
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM Subjects");
        }

        List<Subject> subjects = subjectDao.getAll();
        assertTrue(subjects.isEmpty());
    }
}
