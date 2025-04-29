
import com.example.teach.model.Subject;
import com.example.teach.model.Teacher;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {
    @Test
    void validPrefixAccepted() {
        Subject subj = new Subject("CS","Computers");
        Teacher t = new Teacher("T999", "h", "First", "Last", "e@", subj);
        assertEquals("T999", t.getId());
        assertSame(subj, t.getSubject());
    }

    @Test
    void invalidPrefixThrows() {
        Subject subj = new Subject("CS","Computers");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Teacher("S123", "h", "First", "Last", "e@", subj)
        );
        assertTrue(ex.getMessage().contains("must start with 'T'"));
    }
}

