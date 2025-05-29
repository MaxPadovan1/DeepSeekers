import com.example.teach.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ASubmissionDAOTest extends DatabaseTestBase {

    private ASubmissionDAO submissionDao;

    @BeforeEach
    void setUp() throws Exception {
        super.cleanTables();
        submissionDao = new ASubmissionDAO();

        // Seed required rows for foreign key integrity
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

        submissionDao.submitAssignment(s);

        List<ASubmission> results = submissionDao.getSubmissionsByAssignmentId("A1");
        assertEquals(1, results.size());

        ASubmission stored = results.get(0);
        assertEquals("S1", stored.getId());
        assertEquals("student123", stored.getStudentId());
        assertEquals("submissions/CS/A1/student123.txt", stored.getFilePath());
        assertEquals("2025-05-10T15:00", stored.getTimestamp());
    }

    @Test
    void returnsEmptyListIfNoSubmissions() throws Exception {
        List<ASubmission> results = submissionDao.getSubmissionsByAssignmentId("A1");
        assertTrue(results.isEmpty());
    }

    @Test
    void getSubmissionByStudentAndAssignmentReturnsCorrectSubmission() throws Exception {
        ASubmission s = new ASubmission(
                "S2", "A1", "student123",
                "submissions/CS/A1/s2.txt", "2025-05-11T10:00"
        );
        submissionDao.submitAssignment(s);

        ASubmission result = submissionDao.getSubmissionByStudentAndAssignment("student123", "A1");
        assertNotNull(result);
        assertEquals("S2", result.getId());
        assertEquals("submissions/CS/A1/s2.txt", result.getFilePath());
        assertEquals("2025-05-11T10:00", result.getTimestamp());
    }

    @Test
    void getSubmissionByStudentAndAssignmentReturnsNullIfNoneExists() throws Exception {
        ASubmission result = submissionDao.getSubmissionByStudentAndAssignment("student123", "A1");
        assertNull(result);
    }

    @Test
    void upsertSubmissionInsertsWhenNotExists() throws Exception {
        ASubmission s = new ASubmission(
                "S3", "A1", "student123",
                "submissions/CS/A1/s3.txt", "2025-05-12T12:00"
        );
        submissionDao.upsertSubmission(s);

        ASubmission result = submissionDao.getSubmissionByStudentAndAssignment("student123", "A1");
        assertNotNull(result);
        assertEquals("S3", result.getId());
        assertEquals("submissions/CS/A1/s3.txt", result.getFilePath());
    }

    @Test
    void upsertSubmissionUpdatesExistingSubmission() throws Exception {
        ASubmission original = new ASubmission(
                "S4", "A1", "student123",
                "submissions/CS/A1/s4_v1.txt", "2025-05-13T09:00"
        );
        submissionDao.submitAssignment(original);

        ASubmission updated = new ASubmission(
                "S4", "A1", "student123",  // Same ID, assignment, and student
                "submissions/CS/A1/s4_v2.txt", "2025-05-13T11:00"
        );
        submissionDao.upsertSubmission(updated);

        ASubmission result = submissionDao.getSubmissionByStudentAndAssignment("student123", "A1");
        assertNotNull(result);
        assertEquals("S4", result.getId()); // Same ID
        assertEquals("submissions/CS/A1/s4_v2.txt", result.getFilePath());
        assertEquals("2025-05-13T11:00", result.getTimestamp());
    }
}


