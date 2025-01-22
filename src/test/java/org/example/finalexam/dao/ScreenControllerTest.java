package org.example.finalexam.dao;

import org.example.finalexam.model.Screen;
import org.example.finalexam.model.Theater;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ScreenControllerTest {
    @Mock
    private ScreenDAO mockScreenDAO;
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testAddScreen() throws SQLException {
        // Arrange
        Theater newTheater = new Theater.Builder()
                .setID(1)
                .setName("New Theater")
                .setAddress("New Address")
                .setScreens(Collections.emptyList())
                .build();

        Screen newScreen = new Screen.Builder()
                .setId(1)
                .setTiming(120)
                .setMovieName("Lego Movie")
                .setSeatAvailable(100)
                .setTheater(newTheater)
                .build();

        doNothing().when(mockScreenDAO).addScreen(newScreen);
        // Act
        mockScreenDAO.addScreen(newScreen);
        // Assert
        verify(mockScreenDAO, times(1)).addScreen(newScreen);
    }

    @Test
    void testUpdateScreen() throws SQLException {
        // Arrange
        Theater updatedTheater = new Theater.Builder()
                .setID(1)
                .setName("New Theater")
                .setAddress("New Address")
                .setScreens(Collections.emptyList())
                .build();

        Screen updatedScreen = new Screen.Builder()
                .setId(1)
                .setTiming(110)
                .setMovieName("Lego Movie 2")
                .setSeatAvailable(60)
                .setTheater(updatedTheater)
                .build();

        doNothing().when(mockScreenDAO).updateScreen(updatedScreen);
        // Act
        mockScreenDAO.updateScreen(updatedScreen);
        // Assert
        verify(mockScreenDAO, times(1)).updateScreen(updatedScreen);
    }

    @Test
    void testDeleteScreen() throws SQLException {
        // Arrange
        doNothing().when(mockScreenDAO).deleteScreen(1);
        // Act
        mockScreenDAO.deleteScreen(1);
        // Assert
        verify(mockScreenDAO, times(1)).deleteScreen(1);
    }

    @Test
    void testGetAllScreens() throws SQLException {
        // Arrange
        Theater theater = new Theater.Builder()
                .setID(1)
                .setName("New Theater")
                .setAddress("New Address")
                .setScreens(Collections.emptyList())
                .build();

        List<Screen> screens = Arrays.asList(
                new Screen.Builder()
                        .setId(1)
                        .setTiming(100)
                        .setMovieName("Jumaji 2")
                        .setSeatAvailable(98)
                        .setTheater(theater)
                        .build(),
                new Screen.Builder()
                        .setId(2)
                        .setTiming(120)
                        .setMovieName("Cinderella")
                        .setSeatAvailable(60)
                        .setTheater(theater)
                        .build()
        );
        when(mockScreenDAO.getAllScreens()).thenReturn(screens);
        // Act
        List<Screen> result = mockScreenDAO.getAllScreens();
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetScreenIdByScreenName() throws SQLException {
        // Arrange
        when(mockScreenDAO.getScreenIDByScreenName("Cinderella")).thenReturn(2);
        // Act
        int result = mockScreenDAO.getScreenIDByScreenName("Cinderella");
        // Assert
        assertEquals(2, result);
    }

    @Test
    void testGetScreenById() throws SQLException {
        // Arrange
        Theater theater = new Theater.Builder()
                .setID(1)
                .setName("New Theater")
                .setAddress("New Address")
                .setScreens(Collections.emptyList())
                .build();

        Screen screen = new Screen.Builder()
                .setId(1)
                .setTiming(120)
                .setMovieName("Lego NINJAGO Movie")
                .setSeatAvailable(90)
                .setTheater(theater)
                .build();

        when(mockScreenDAO.getScreenById(1)).thenReturn(screen);
        // Act
        Screen result = mockScreenDAO.getScreenById(1);
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Lego NINJAGO Movie", result.getMovie_name());
    }
}
