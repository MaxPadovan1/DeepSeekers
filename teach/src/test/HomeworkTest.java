import com.example.teach.model.Homework;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HomeworkTest {
    @Test
    void constructorAndGetters() {
        Homework h = new Homework("H1", "SUB2", "Read Chapter", "Read chapters 3–5", "2025-06-01");
        assertEquals("H1", h.getId());
        assertEquals("SUB2", h.getSubjectId());
        assertEquals("Read Chapter", h.getTitle());
        assertEquals("Read chapters 3–5", h.getDescription());
        assertEquals("2025-06-01", h.getDueDate());
    }
}

