import com.example.teach.model.LessonPlan;
import com.example.teach.model.LessonPlanDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LessonPlanDAOTest extends DatabaseTestBase {

    private LessonPlanDAO lessonPlanDao;

    @BeforeEach
    void setUp() throws Exception {
        super.cleanTables();  // Clears tables before each test
        lessonPlanDao = new LessonPlanDAO();

        // Insert a subject for FK compliance
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO Subjects(id, name) VALUES('CS', 'Computer Science')");
        }
    }

    @Test
    void saveAndFindByIdWorks() throws Exception {
        LessonPlan lp = new LessonPlan("LP1", "CS", "Intro", "Intro to programming");
        lessonPlanDao.save(lp);

        Optional<LessonPlan> result = lessonPlanDao.findById("LP1");
        assertTrue(result.isPresent());
        assertEquals("CS", result.get().getSubjectId());
        assertEquals("Intro", result.get().getTitle());
        assertEquals("Intro to programming", result.get().getDetails());
    }

    @Test
    void findByIdReturnsEmptyIfNotFound() throws Exception {
        Optional<LessonPlan> result = lessonPlanDao.findById("NON_EXISTENT");
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllReturnsAllLessonPlans() throws Exception {
        lessonPlanDao.save(new LessonPlan("LP2", "CS", "Data Structures", "Stacks and Queues"));
        lessonPlanDao.save(new LessonPlan("LP3", "CS", "Algorithms", "Sorting and Searching"));

        List<LessonPlan> all = lessonPlanDao.findAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(lp -> lp.getTitle().equals("Data Structures")));
        assertTrue(all.stream().anyMatch(lp -> lp.getTitle().equals("Algorithms")));
    }

    @Test
    void findBySubjectFiltersCorrectly() throws Exception {
        lessonPlanDao.save(new LessonPlan("LP4", "CS", "CS Plan", "CS Content"));

        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO Subjects(id, name) VALUES('MATH', 'Mathematics')");
        }

        lessonPlanDao.save(new LessonPlan("LP5", "MATH", "Math Plan", "Math Content"));

        List<LessonPlan> csPlans = lessonPlanDao.findBySubject("CS");
        assertEquals(1, csPlans.size());
        assertEquals("CS Plan", csPlans.get(0).getTitle());
    }

    @Test
    void updateModifiesExistingRecord() throws Exception {
        LessonPlan lp = new LessonPlan("LP6", "CS", "Old Title", "Old Content");
        lessonPlanDao.save(lp);

        lp.setTitle("New Title");
        lp.setDetails("New Content");
        lessonPlanDao.update(lp);

        LessonPlan updated = lessonPlanDao.findById("LP6").orElseThrow();
        assertEquals("New Title", updated.getTitle());
        assertEquals("New Content", updated.getDetails());
    }

    @Test
    void deleteRemovesLessonPlan() throws Exception {
        lessonPlanDao.save(new LessonPlan("LP7", "CS", "To Delete", "Content"));
        assertTrue(lessonPlanDao.findById("LP7").isPresent());

        lessonPlanDao.delete("LP7");

        assertTrue(lessonPlanDao.findById("LP7").isEmpty());
    }
}
