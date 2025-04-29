import com.example.teach.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudyDAOTest extends DatabaseTestBase {

    private StudyDAO studyDao;


    @BeforeEach
    void setUp() throws Exception {
        super.cleanTables();
        studyDao = new StudyDAO();

        // Seed a subject for Study foreign key
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO Subjects(id, name) VALUES('CS', 'Python')");
        }
    }

    @Test
    void addAndFetchStudyWorks() throws Exception {
        Study s1 = new Study("S1", "CS", "Python", "Object oriented design");
        studyDao.add(s1);

        List<Study> studies = studyDao.getBySubject("CS");

        assertEquals(1, studies.size());
        Study fetched = studies.get(0);

        assertEquals("S1", fetched.getId());
        assertEquals("CS", fetched.getSubjectId());
        assertEquals("Python", fetched.getTitle());
        assertEquals("Object oriented design", fetched.getContent());
    }

    @Test
    void getBySubjectReturnsEmptyIfNoneExist() throws Exception {
        List<Study> studies = studyDao.getBySubject("CS");
        assertTrue(studies.isEmpty());
    }

    @Test
    void addMultipleStudiesAndFetch() throws Exception {
        Study s1 = new Study("S2", "CS", "C#", "Classes and Interfaces");
        Study s2 = new Study("S3", "CS", "SQL", "Databases and commands");
        studyDao.add(s1);
        studyDao.add(s2);

        List<Study> studies = studyDao.getBySubject("CS");

        assertEquals(2, studies.size());
        assertTrue(studies.stream().anyMatch(s -> s.getTitle().equals("C#")));
        assertTrue(studies.stream().anyMatch(s -> s.getTitle().equals("SQL")));
    }

    @Test
    void getBySubjectReturnsEmptyForNonexistentSubject() throws Exception {
        List<Study> studies = studyDao.getBySubject("NON_EXISTENT");
        assertTrue(studies.isEmpty());
    }
}
