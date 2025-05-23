import com.example.teach.model.Assignment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AssignmentTest {
    @Test
    void constructorAndGetters() {
        Assignment a = new Assignment("A1", "SUB1", "Homework 1", "Solve problems", "2025-05-01", true);
        assertEquals("A1", a.getId());
        assertEquals("SUB1", a.getSubjectId());
        assertEquals("Homework 1", a.getTitle());
        assertEquals("Solve problems", a.getDescription());
        assertEquals("2025-05-01", a.getDueDate());
        assertTrue(a.isReleased());
    }
}
