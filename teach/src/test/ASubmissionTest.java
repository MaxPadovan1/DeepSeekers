import com.example.teach.model.ASubmission;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ASubmissionTest {

    @Test
    void constructorAndGetters() {
        ASubmission s = new ASubmission(
                "S1", "A1", "student123",
                "submissions/CS/A1/student123.txt", "2025-05-10T15:00"
        );

        assertEquals("S1", s.getId());
        assertEquals("A1", s.getAssignmentId());
        assertEquals("Student: student123", s.studentNameProperty().get());
        assertEquals("submissions/CS/A1/student123.txt", s.getFilePath());
        assertEquals("2025-05-10T15:00", s.getTimestamp());
    }

    @Test
    void javafxPropertiesExposeCorrectValues() {
        ASubmission s = new ASubmission(
                "S1", "A1", "student123",
                "submissions/CS/A1/student123.txt", "2025-05-10T15:00"
        );

        assertEquals("Student: student123", s.studentNameProperty().get());
        assertEquals("submissions/CS/A1/student123.txt", s.filePathProperty().get());
        assertEquals("2025-05-10T15:00", s.timestampProperty().get());
    }
}

