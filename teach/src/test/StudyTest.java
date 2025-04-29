
import com.example.teach.model.Study;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudyTest {
    @Test
    void constructorAndGetters() {
        Study s = new Study("ST1", "SUB3", "Intro", "Study material content");
        assertEquals("ST1", s.getId());
        assertEquals("SUB3", s.getSubjectId());
        assertEquals("Intro", s.getTitle());
        assertEquals("Study material content", s.getContent());
    }
}

