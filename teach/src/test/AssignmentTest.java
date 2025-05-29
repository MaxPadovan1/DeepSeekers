import com.example.teach.model.Assignment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AssignmentTest {

    @Test
    void testConstructorAndGettersForReleasedAssignment() {
        Assignment a = new Assignment("A1", "SUB1", "Homework 1", "Solve problems", "2025-05-01", true);
        assertEquals("A1", a.getId());
        assertEquals("SUB1", a.getSubjectId());
        assertEquals("Homework 1", a.getTitle());
        assertEquals("Solve problems", a.getDescription());
        assertEquals("2025-05-01", a.getDueDate());
        assertTrue(a.isReleased());
    }

    @Test
    void testConstructorAndGettersForUnreleasedAssignment() {
        Assignment a = new Assignment("A2", "SUB2", "Project", "Group project on databases", "2025-06-15", false);
        assertEquals("A2", a.getId());
        assertEquals("SUB2", a.getSubjectId());
        assertEquals("Project", a.getTitle());
        assertEquals("Group project on databases", a.getDescription());
        assertEquals("2025-06-15", a.getDueDate());
        assertFalse(a.isReleased());
    }

    @Test
    void testToString() {
        Assignment a = new Assignment("A3", "SUB3", "Essay", "Write a 1000-word essay", "2025-07-20", true);
        String expected = "Essay (Due: 2025-07-20)";
        assertEquals(expected, a.toString());
    }
}

