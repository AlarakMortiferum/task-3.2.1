package ru.netology.testmode.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataGeneratorTest {

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
}