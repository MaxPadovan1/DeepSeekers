import com.example.teach.model.SQLiteDAO;
import com.example.teach.model.SQliteConnection;
import com.example.teach.model.UserDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.Statement;

public abstract class DatabaseTestBase {
    protected static Connection conn;
    protected UserDAO dao;

    @BeforeAll
    public static void initSchema() throws Exception {

        // Force schema creation
        new SQLiteDAO();  // runs createSchema() in its constructor

        // Grab the shared Connection
        conn = SQliteConnection.getInstance();
    }

    @BeforeEach
    public void cleanTables() throws Exception {
        try (Statement st = conn.createStatement()) {
            // Delete in correct order to avoid FK violations
            st.executeUpdate("DELETE FROM Submissions");
            st.executeUpdate("DELETE FROM study_files");
            st.executeUpdate("DELETE FROM StudentSubjects");
            st.executeUpdate("DELETE FROM Teachers");
            st.executeUpdate("DELETE FROM Students");
            st.executeUpdate("DELETE FROM Users");
            st.executeUpdate("DELETE FROM Assignments");
            st.executeUpdate("DELETE FROM Homework");
            st.executeUpdate("DELETE FROM Study");
            st.executeUpdate("DELETE FROM LessonPlans");
            st.executeUpdate("DELETE FROM Subjects");
        }
        dao = new UserDAO();
    }
}
