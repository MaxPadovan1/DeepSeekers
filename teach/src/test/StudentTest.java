import com.example.teach.model.Student;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
class StudentTest {
    @Test
    void validPrefixAccepted() {
        Student s = new Student("S123", "h", "A", "B", "e@", List.of());
        assertEquals("S123", s.getId());
    }

    @Test
    void invalidPrefixThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Student("X123", "h", "A", "B", "e@", List.of())
        );
        assertTrue(ex.getMessage().contains("must start with 'S'"));
    }
}

