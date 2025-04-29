import com.example.teach.model.Subject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubjectTest {
    @Test
    void constructorAndGetters() {
        Subject s = new Subject("CS101", "Algorithms");
        assertEquals("CS101", s.getId());
        assertEquals("Algorithms", s.getName());
        assertEquals("Algorithms", s.toString());
    }
}
