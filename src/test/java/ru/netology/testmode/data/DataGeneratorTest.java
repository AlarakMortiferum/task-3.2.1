package ru.netology.testmode.data;

import org.junit.jupiter.api.Test;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DataGeneratorTest {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/app";
    private static final String DB_USER = "app";
    private static final String DB_PASS = "pass";

    @Test
    void shouldGenerateActiveUser() {
        User user = DataGenerator.getActiveUser();
        assertEquals("vasya", user.getLogin());
        assertEquals("qwerty123", user.getPassword());
        assertEquals("active", user.getStatus());
    }

    @Test
    void shouldGenerateRandomUser() {
        User user = DataGenerator.getRandomUser();
        assertNotNull(user.getLogin());
        assertNotNull(user.getPassword());
        assertEquals("active", user.getStatus());
    }

    @Test
    void shouldAddGeneratedUserToDb() throws SQLException {
        DbService db = new DbService(DB_URL, DB_USER, DB_PASS);
        User user = DataGenerator.getRandomUser();

        db.addUser(user);

        assertTrue(db.userExists(user.getLogin()));
    }
}
