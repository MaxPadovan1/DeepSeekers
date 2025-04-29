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
            st.executeUpdate("INSERT INTO Subjects(id, name) VALUES('MATH', 'Mathematics')");
        }
    }

    @Test
    void addAndFetchAssignmentWorks() throws Exception {
        Assignment a1 = new Assignment("A1", "MATH", "Algebra", "Solve problems", "2025-05-10");
        assignmentDao.add(a1);

        List<Assignment> assignments = assignmentDao.getBySubject("MATH");

        assertEquals(1, assignments.size());
        Assignment fetched = assignments.get(0);

        assertEquals("A1", fetched.getId());
        assertEquals("MATH", fetched.getSubjectId());
        assertEquals("Algebra", fetched.getTitle());
        assertEquals("Solve problems", fetched.getDescription());
        assertEquals("2025-05-10", fetched.getDueDate());
    }

    @Test
    void getBySubjectReturnsEmptyIfNoneExist() throws Exception {
        List<Assignment> assignments = assignmentDao.getBySubject("MATH");
        assertTrue(assignments.isEmpty());
    }

    @Test
    void addMultipleAssignmentsAndFetch() throws Exception {
        Assignment a1 = new Assignment("A2", "MATH", "Geometry", "Triangles", "2025-05-20");
        Assignment a2 = new Assignment("A3", "MATH", "Statistics", "Mean and Median", "2025-05-25");
        assignmentDao.add(a1);
        assignmentDao.add(a2);

        List<Assignment> assignments = assignmentDao.getBySubject("MATH");

        assertEquals(2, assignments.size());
        assertTrue(assignments.stream().anyMatch(a -> a.getTitle().equals("Geometry")));
        assertTrue(assignments.stream().anyMatch(a -> a.getTitle().equals("Statistics")));
    }

    @Test
    void getBySubjectReturnsEmptyForNonexistentSubject() throws Exception {
        List<Assignment> assignments = assignmentDao.getBySubject("NON_EXISTENT");
        assertTrue(assignments.isEmpty());
    }
}
