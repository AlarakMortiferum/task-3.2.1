package ru.netology.testmode.data;

import org.junit.jupiter.api.*;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DbServiceTest {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/app";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "pass";

    private DbService dbService;

    @BeforeEach
    void setUp() throws SQLException {
        // Убедитесь, что передаётся ровно 3 аргумента
        dbService = new DbService(DB_URL, DB_USER, DB_PASSWORD);
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Убедитесь, что этот метод существует в DbService
        dbService.cleanDatabase();
    }

    @Test
    void shouldAddAndFindUser() throws SQLException {
        User user = new User("test_user", "password123", "active");
        dbService.addUser(user);
        assertTrue(dbService.userExists("test_user"));
    }

    @Test
    void shouldNotFindNonExistentUser() throws SQLException {
        // Теперь assertFalse будет распознан
        assertFalse(dbService.userExists("non_existent_user"));
    }
}