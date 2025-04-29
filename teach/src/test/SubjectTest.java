import com.example.teach.model.Subject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubjectTest {
    @Test
    void constructorAndGetters() {
        Subject s = new Subject("MATH101", "Calculus I");
        assertEquals("MATH101", s.getId());
        assertEquals("Calculus I", s.getName());
        assertEquals("Calculus I", s.toString());
    }
}
