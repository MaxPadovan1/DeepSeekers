import com.example.teach.model.*;
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

        // Seed a subject for homework foreign key
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO Subjects(id, name) VALUES('ENG', 'English')");
        }
    }

    @Test
    void addAndFetchHomeworkWorks() throws Exception {
        Homework h1 = new Homework("H1", "ENG", "Essay", "Write about summer", "2025-06-01","2025-05-01","2025-05-20");
        homeworkDao.add(h1);

        List<Homework> homeworks = homeworkDao.getBySubject("ENG");

        assertEquals(1, homeworks.size());
        Homework fetched = homeworks.get(0);

        assertEquals("H1", fetched.getId());
        assertEquals("ENG", fetched.getSubjectId());
        assertEquals("Essay", fetched.getTitle());
        assertEquals("Write about summer", fetched.getDescription());
        assertEquals("2025-06-01", fetched.getDueDate());
        assertEquals("2025-05-01",fetched.getOpenDate());
        assertEquals("2025-05-20",fetched.getOpenDate());
    }

    @Test
    void getBySubjectReturnsEmptyIfNoneExist() throws Exception {
        List<Homework> homeworks = homeworkDao.getBySubject("ENG");
        assertTrue(homeworks.isEmpty());
    }

    @Test
    void addMultipleHomeworksAndFetch() throws Exception {
        Homework h1 = new Homework("H2", "ENG", "Grammar", "Tenses exercise", "2025-06-10","2025-05-01","2025-05-20");
        Homework h2 = new Homework("H3", "ENG", "Vocabulary", "Learn 50 new words", "2025-06-15","2025-05-01","2025-05-20");
        homeworkDao.add(h1);
        homeworkDao.add(h2);

        List<Homework> homeworks = homeworkDao.getBySubject("ENG");

        assertEquals(2, homeworks.size());
        assertTrue(homeworks.stream().anyMatch(h -> h.getTitle().equals("Grammar")));
        assertTrue(homeworks.stream().anyMatch(h -> h.getTitle().equals("Vocabulary")));
    }

    @Test
    void getBySubjectReturnsEmptyForNonexistentSubject() throws Exception {
        List<Homework> homeworks = homeworkDao.getBySubject("NON_EXISTENT");
        assertTrue(homeworks.isEmpty());
    }
}

