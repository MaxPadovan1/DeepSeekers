
import com.example.teach.model.SQLiteDAO;
import com.example.teach.model.SQliteConnection;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.Statement;

public abstract class DatabaseTestBase {
    protected static Connection conn;

    @BeforeAll
    static void setupDatabase() throws Exception {
        // initialize schema
        new SQLiteDAO();

        // grab the shared connection
        conn = SQliteConnection.getInstance();

        // wipe all tables for a fresh state
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM StudentSubjects");
            st.executeUpdate("DELETE FROM Teachers");
            st.executeUpdate("DELETE FROM Students");
            st.executeUpdate("DELETE FROM Users");
            st.executeUpdate("DELETE FROM Assignments");
            st.executeUpdate("DELETE FROM Homeworks");
            st.executeUpdate("DELETE FROM CourseContent");
            st.executeUpdate("DELETE FROM Subjects");
        }
    }
}

