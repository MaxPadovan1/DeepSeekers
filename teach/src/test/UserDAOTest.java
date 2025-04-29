
import com.example.teach.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest extends DatabaseTestBase{

    @Test
    void signUpAndLoginStudent() throws Exception {
        // no subjects yet
        Student stu = new Student("S1", "pw1", "Kendall", "K", "k@e", List.of());
        assertTrue(dao.signUp(stu));
        assertTrue(dao.exists("S1"));

        User found = dao.findByCredentials("S1", "pw1");
        assertNotNull(found);
        assertTrue(found instanceof Student);
        Student s2 = (Student) found;
        assertEquals("Kendall", s2.getFirstName());
        assertTrue(s2.getSubjects().isEmpty());
    }

    @Test
    void signUpAndLoginTeacher() throws Exception {
        // seed a subject
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO Subjects(id,name) VALUES('T1','TestSub')");
        }
        Subject subj = new Subject("T1","TestSub");
        Teacher tch = new Teacher("T2", "pw2", "Logan", "L", "l@e", subj);

        assertTrue(dao.signUp(tch));
        assertTrue(dao.exists("T2"));

        User found = dao.findByCredentials("T2", "pw2");
        assertNotNull(found);
        assertTrue(found instanceof Teacher);
        Teacher t2 = (Teacher) found;
        assertEquals("Logan", t2.getFirstName());
        assertNotNull(t2.getSubject());
        assertEquals("TestSub", t2.getSubject().getName());
    }

    @Test
    void updateProfileWorks() throws Exception {
        Student stu = new Student("S3", "pw3", "Shiv", "S", "s@e", List.of());
        dao.signUp(stu);

        // modify and update
        stu.setFirstName("Dee");
        stu.setLastName("Olson");
        stu.setEmail("dee@e");
        assertTrue(dao.updateProfile(stu));

        // reload and verify
        Student s2 = (Student) dao.findByCredentials("S3", "pw3");
        assertEquals("Dee", s2.getFirstName());
        assertEquals("Olson", s2.getLastName());
        assertEquals("dee@e", s2.getEmail());
    }

    @Test
    void changePasswordSuccessfully() throws Exception {
        Student stu = new Student("S4", "oldHash", "Roman", "R", "r@e", List.of());
        dao.signUp(stu);

        // correct old hash
        assertTrue(dao.changePassword("S4", "oldHash", "newHash"));

        // login only with new hash
        assertNull(dao.findByCredentials("S4", "oldHash"));
        assertNotNull(dao.findByCredentials("S4", "newHash"));
    }

    @Test
    void changePasswordFailsOnWrongOldHash() throws Exception {
        Student stu = new Student("S5", "h5", "Siobhan", "S", "s@e", List.of());
        dao.signUp(stu);

        assertFalse(dao.changePassword("S5", "wrong", "new5"));
        // original still valid
        assertNotNull(dao.findByCredentials("S5", "h5"));
    }

    @Test
    void resetPasswordViaEmailSuccessfully() throws Exception {
        Student stu = new Student("S6", "h6", "Greg", "G", "g@e", List.of());
        dao.signUp(stu);

        // correct email
        assertTrue(dao.resetPassword("S6", "g@e", "rh6"));

        assertNull(dao.findByCredentials("S6", "h6"));
        assertNotNull(dao.findByCredentials("S6", "rh6"));
    }

    @Test
    void resetPasswordFailsOnWrongEmail() throws Exception {
        Student stu = new Student("S7", "h7", "Gina", "G", "g@e", List.of());
        dao.signUp(stu);

        assertFalse(dao.resetPassword("S7", "wrong@e", "rh7"));
        assertNotNull(dao.findByCredentials("S7", "h7"));
    }

    @Test
    void signUpStudentSubjectLimitEnforced() throws Exception {
        // creating a student with >4 subjects should return false
        List<Subject> tooMany = List.of(
                new Subject("A",""), new Subject("B",""),
                new Subject("C",""), new Subject("D",""),
                new Subject("E","")
        );
        Student stu = new Student("S8", "pw8", "Hank", "H", "h@e", tooMany);
        assertFalse(dao.signUp(stu));
        assertFalse(dao.exists("S8"));
    }

    @Test
    void signUpDuplicateIdFails() throws Exception {
        Student stu1 = new Student("S100", "pass", "Charlie", "C", "charlie@e.com",
                List.of(new Subject("T1", "TestSub")));
        Student stu2 = new Student("S100", "pass2", "Chris", "C", "chris@e.com",
                List.of(new Subject("T1", "TestSub")));

        assertTrue(dao.signUp(stu1));
        assertFalse(dao.signUp(stu2));
    }

    @Test
    void signUpStudentNullSubjectsSucceeds() throws Exception {
        Student stu = new Student("S101", "pass", "Eve", "E", "eve@e.com", null);

        assertTrue(dao.signUp(stu));
        assertTrue(dao.exists("S101"));
    }

    @Test
    void signUpStudentExactlyFourSubjectsSucceeds() throws Exception {
        List<Subject> subjects = List.of(
                new Subject("A", ""), new Subject("B", ""),
                new Subject("C", ""), new Subject("D", "")
        );
        Student stu = new Student("S102", "pass", "Frank", "F", "frank@e.com", subjects);

        assertTrue(dao.signUp(stu));
        assertTrue(dao.exists("S102"));
    }
}


