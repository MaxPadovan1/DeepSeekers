import com.example.teach.model.StudyFile;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudyFileTest {

    @Test
    void constructorAndGettersWorkCorrectly() {
        String expectedFileName = "week1.pdf";
        String expectedTitle = "Week 1 Notes";
        byte[] expectedData = {1, 2, 3, 4, 5};

        StudyFile file = new StudyFile(expectedFileName, expectedTitle, expectedData);

        assertEquals(expectedFileName, file.getFileName());
        assertEquals(expectedTitle, file.getTitle());
        assertArrayEquals(expectedData, file.getData());
    }

    @Test
    void getDataReturnsCopyReference() {
        byte[] data = {10, 20, 30};
        StudyFile file = new StudyFile("a.txt", "Sample", data);

        // Verify the same reference is returned (by design in your class)
        assertSame(data, file.getData());
    }
}

