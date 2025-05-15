import com.example.teach.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssignmentDAOTest extends DatabaseTestBase {

    private AssignmentDAO assignmentDao;

    @BeforeEach
    void setUp() throws Exception {
        super.cleanTables();
        assignmentDao = new AssignmentDAO();

        // Seed a subject for assignment foreign key
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO Subjects(id, name) VALUES('CS', 'Python')");
        }
    }

    @Test
    void addAndFetchAssignmentWorks() throws Exception {
        Assignment a1 = new Assignment("A1", "CS", "Python", "Create program", "2025-05-10", false);
        assignmentDao.add(a1);

        List<Assignment> assignments = assignmentDao.getBySubject("CS");

        assertEquals(1, assignments.size());
        Assignment fetched = assignments.get(0);

        assertEquals("A1", fetched.getId());
        assertEquals("CS", fetched.getSubjectId());
        assertEquals("Python", fetched.getTitle());
        assertEquals("Create program", fetched.getDescription());
        assertEquals("2025-05-10", fetched.getDueDate());
        assertFalse(fetched.isReleased());
    }

    @Test
    void getBySubjectReturnsEmptyIfNoneExist() throws Exception {
        List<Assignment> assignments = assignmentDao.getBySubject("CS");
        assertTrue(assignments.isEmpty());
    }

    @Test
    void addMultipleAssignmentsAndFetch() throws Exception {
        Assignment a1 = new Assignment("A2", "CS", "C++", "Write tests", "2025-05-20", false);
        Assignment a2 = new Assignment("A3", "CS", "C#", "Learn OOP", "2025-05-25", true);
        assignmentDao.add(a1);
        assignmentDao.add(a2);

        List<Assignment> assignments = assignmentDao.getBySubject("CS");

        assertEquals(2, assignments.size());
        assertTrue(assignments.stream().anyMatch(a -> a.getTitle().equals("C++") && !a.isReleased()));
        assertTrue(assignments.stream().anyMatch(a -> a.getTitle().equals("C#") && a.isReleased()));
    }

    @Test
    void getBySubjectReturnsEmptyForNonexistentSubject() throws Exception {
        List<Assignment> assignments = assignmentDao.getBySubject("NON_EXISTENT");
        assertTrue(assignments.isEmpty());
    }

    @Test
    void releaseAssignmentUpdatesField() throws Exception {
        Assignment a = new Assignment("A4", "CS", "Java", "Inheritance", "2025-05-30", false);
        assignmentDao.add(a);

        // Release it
        assignmentDao.releaseAssignment("A4");

        List<Assignment> assignments = assignmentDao.getBySubject("CS");
        assertEquals(1, assignments.size());
        assertTrue(assignments.get(0).isReleased());

        // Should appear in getReleasedAssignments()
        List<Assignment> released = assignmentDao.getReleasedAssignments("CS");
        assertEquals(1, released.size());
        assertEquals("A4", released.get(0).getId());
    }
}
