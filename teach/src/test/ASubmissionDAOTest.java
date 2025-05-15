import com.example.teach.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ASubmissionDAOTest extends DatabaseTestBase {

    private ASubmissionDAO ASubmissionDao;

    @BeforeEach
    void setUp() throws Exception {
        super.cleanTables();
        ASubmissionDao = new ASubmissionDAO();

        // Add minimal required rows for foreign keys
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO Subjects(id, name) VALUES('CS', 'Programming')");
            st.executeUpdate("INSERT INTO Assignments(id, subject_id, title, description, due_date, is_released) " +
                    "VALUES('A1', 'CS', 'Title', 'Desc', '2025-05-10', 1)");
            st.executeUpdate("INSERT INTO Users(id, passwordHash, firstName, lastName, role) " +
                    "VALUES('student123', 'hashed', 'John', 'Doe', 'student')");
            st.executeUpdate("INSERT INTO Students(id) VALUES('student123')");
        }
    }

    @Test
    void submitAndFetchSubmission() throws Exception {
        ASubmission s = new ASubmission(
                "S1", "A1", "student123",
                "submissions/CS/A1/student123.txt", "2025-05-10T15:00"
        );

        ASubmissionDao.submitAssignment(s);

        List<ASubmission> results = ASubmissionDao.getSubmissionsByAssignmentId("A1");
        assertEquals(1, results.size());

        ASubmission stored = results.get(0);
        assertEquals("S1", stored.getId());
        assertEquals("student123", stored.getStudentId());
        assertEquals("submissions/CS/A1/student123.txt", stored.getFilePath());
        assertEquals("2025-05-10T15:00", stored.getTimestamp());
    }

    @Test
    void returnsEmptyListIfNoSubmissions() throws Exception {
        List<ASubmission> results = ASubmissionDao.getSubmissionsByAssignmentId("A1");
        assertTrue(results.isEmpty());
    }
}

