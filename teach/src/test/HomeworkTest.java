import com.example.teach.model.Homework;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HomeworkTest {

    @Test
    public void testHomeworkConstructorAndGetters() {
        String subjectId = "math101";
        String week = "Week 3";
        String title = "Algebra Homework";
        String description = "Solve all problems in chapter 2";
        String dueDate = "2025-06-01";
        String releaseDate = "2025-05-25";
        String openDate = "2025-05-27";
        String id = "hw001";

        Homework homework = new Homework(subjectId, week, title, description, dueDate, releaseDate, openDate, id);

        assertEquals(subjectId, homework.getSubjectId());
        assertEquals(week, homework.getWeek());
        assertEquals(title, homework.getTitle());
        assertEquals(description, homework.getDescription());
        assertEquals(dueDate, homework.getDueDate());
        assertEquals(releaseDate, homework.getReleaseDate());
        assertEquals(openDate, homework.getOpenDate());
        assertEquals(id, homework.getId());
    }
}



