import com.example.teach.model.Homework;
import com.example.teach.model.HomeworkDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeworkDAOTest extends DatabaseTestBase {

    private HomeworkDAO homeworkDao;

    @BeforeEach
    void setUp() throws Exception {
        super.cleanTables();
        homeworkDao = new HomeworkDAO();

        // Seed subject for foreign key constraint
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO Subjects(id, name) VALUES('CS', 'Python')");
        }
    }

    @Test
    void addAndFetchHomeworkWorks() throws Exception {
        Homework hw = new Homework("CS", "Week 1", "Intro", "Solve Q1-5", "2025-06-01", "2025-05-25", "2025-05-26", "HW1");
        homeworkDao.add(hw);

        List<Homework> list = homeworkDao.getBySubject("CS");

        assertEquals(1, list.size());
        Homework fetched = list.get(0);
        assertEquals("HW1", fetched.getId());
        assertEquals("CS", fetched.getSubjectId());
        assertEquals("Week 1", fetched.getWeek());
        assertEquals("Intro", fetched.getTitle());
        assertEquals("Solve Q1-5", fetched.getDescription());
        assertEquals("2025-06-01", fetched.getDueDate());
        assertEquals("2025-05-25", fetched.getReleaseDate());
        assertEquals("2025-05-26", fetched.getOpenDate());
    }

    @Test
    void getBySubjectReturnsEmptyIfNoneExist() throws Exception {
        List<Homework> list = homeworkDao.getBySubject("CS");
        assertTrue(list.isEmpty());
    }

    @Test
    void deleteHomeworkById() throws Exception {
        Homework hw = new Homework("CS", "Week 2", "Functions", "Complete exercises", "2025-06-10", "2025-06-01", "2025-06-03", "HW2");
        homeworkDao.add(hw);

        homeworkDao.delete("HW2");

        List<Homework> after = homeworkDao.getBySubject("CS");
        assertTrue(after.isEmpty());
    }
}
