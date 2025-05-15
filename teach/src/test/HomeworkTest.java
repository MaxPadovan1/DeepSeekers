import com.example.teach.model.Homework;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HomeworkTest {
    @Test
    void constructorAndGetters() {
        Homework h = new Homework(
                "SUB2",                 // subjectid
                "2",                  // ✅ week
                "Read Chapter",       // title
                "Read chapters 3–5",  // description
                "2025-06-01",         // dueDate
                "2025-05-01",         // releaseDate
                "2025-05-20",      // openDate
                "H1"               // Id
        );

        assertEquals("H1", h.getId());
        assertEquals("SUB2", h.getSubjectId());
        assertEquals("2", h.getWeek()); // ✅ 新增断言
        assertEquals("Read Chapter", h.getTitle());
        assertEquals("Read chapters 3–5", h.getDescription());
        assertEquals("2025-06-01", h.getDueDate());
    }
}



