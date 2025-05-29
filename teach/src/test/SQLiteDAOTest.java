import com.example.teach.model.SQLiteDAO;
import com.example.teach.model.SQliteConnection;
import com.example.teach.model.StudyFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteDAOTest extends DatabaseTestBase {

    private static final String SUBJECT_ID = "TEST101";

    @BeforeEach
    void init() throws Exception {
        conn = SQliteConnection.connect(); // always gives a fresh open connection
        cleanTables();
    }

    @Test
    void testSaveStudyFileAndGetReleasedFiles() {
        // Arrange
        byte[] content = "Hello World!".getBytes(StandardCharsets.UTF_8);
        SQLiteDAO.saveStudyFile(SUBJECT_ID, "Week 1", "test.txt", "Test File", content, true);

        // Act
        List<StudyFile> files = SQLiteDAO.getReleasedFilesForWeek(SUBJECT_ID, "Week 1");

        // Assert
        assertEquals(1, files.size());
        StudyFile file = files.get(0);
        assertEquals("test.txt", file.getFileName());
        assertEquals("Test File", file.getTitle());
        assertArrayEquals(content, file.getData());
    }

    @Test
    void testReleasePendingFiles() {
        // Arrange
        byte[] content = "Pending file".getBytes(StandardCharsets.UTF_8);
        SQLiteDAO.saveStudyFile(SUBJECT_ID, null, "pending.txt", "Pending File", content, false);

        // Act: release the file
        SQLiteDAO.releasePendingFiles(SUBJECT_ID, "Week 2");

        // Assert: should now be returned as a released file
        List<StudyFile> releasedFiles = SQLiteDAO.getReleasedFilesForWeek(SUBJECT_ID, "Week 2");
        assertEquals(1, releasedFiles.size());
        StudyFile file = releasedFiles.get(0);
        assertEquals("pending.txt", file.getFileName());
        assertEquals("Pending File", file.getTitle());
    }

    @Test
    void testGetReleasedFilesReturnsEmptyIfNone() {
        List<StudyFile> files = SQLiteDAO.getReleasedFilesForWeek(SUBJECT_ID, "Week 5");
        assertTrue(files.isEmpty());
    }
}
