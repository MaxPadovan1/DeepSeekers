import com.example.teach.model.LessonPlan;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LessonPlanTest {

    @Test
    void constructorAndGettersWorkCorrectly() {
        LessonPlan lp = new LessonPlan("LP1", "CS", "Intro to Loops", "Cover for, while, and do-while loops");

        assertEquals("LP1", lp.getId());
        assertEquals("CS", lp.getSubjectId());
        assertEquals("Intro to Loops", lp.getTitle());
        assertEquals("Cover for, while, and do-while loops", lp.getDetails());
    }

    @Test
    void settersUpdateFieldsCorrectly() {
        LessonPlan lp = new LessonPlan("LP2", "CS", "Initial", "Temp");

        lp.setSubjectId("MATH");
        lp.setTitle("Probability Basics");
        lp.setDetails("Teach basic concepts of probability and expected value");

        assertEquals("MATH", lp.getSubjectId());
        assertEquals("Probability Basics", lp.getTitle());
        assertEquals("Teach basic concepts of probability and expected value", lp.getDetails());
    }
}
