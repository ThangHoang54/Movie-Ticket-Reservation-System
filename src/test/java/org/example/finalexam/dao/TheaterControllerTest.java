package org.example.finalexam.dao;

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

/**
 * @author Hoang Minh Thang - s3999925
 */

public class TheaterControllerTest {
    @Mock
    private TheaterDAO mockTheaterDAO;
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
    void testAddTheater() throws SQLException {
        // Arrange
        Theater newTheater = new Theater.Builder()
                .setID(1)
                .setName("CGV Cinema")
                .setAddress("342 Hai Ba Trung")
                .setScreens(Collections.emptyList())
                .build();

        doNothing().when(mockTheaterDAO).addTheater(newTheater);
        // Act
        mockTheaterDAO.addTheater(newTheater);
        // Assert
        verify(mockTheaterDAO, times(1)).addTheater(newTheater);
    }

    @Test
    void testUpdateTheater() throws SQLException {
        // Arrange
        Theater updatedTheater = new Theater.Builder()
                .setID(1)
                .setName("Galaxy Cinema")
                .setAddress("342/32 VincomMall ")
                .setScreens(Collections.emptyList())
                .build();

        doNothing().when(mockTheaterDAO).updateTheater(updatedTheater);
        // Act
        mockTheaterDAO.updateTheater(updatedTheater);
        // Assert
        verify(mockTheaterDAO, times(1)).updateTheater(updatedTheater);
    }

    @Test
    void testDeleteTheater() throws SQLException {
        // Arrange
        doNothing().when(mockTheaterDAO).deleteTheater(1);
        // Act
        mockTheaterDAO.deleteTheater(1);
        // Assert
        verify(mockTheaterDAO, times(1)).deleteTheater(1);
    }

    @Test
    void testGetAllTheaters() throws SQLException {
        // Arrange
        List<Theater> theaters = Arrays.asList(
                new Theater.Builder()
                        .setID(1)
                        .setName("MooByLip")
                        .setAddress("Landmark81 Vinhome")
                        .setScreens(Collections.emptyList())
                        .build(),
                new Theater.Builder()
                        .setID(2)
                        .setName("Galaxy Cinema")
                        .setAddress("342 Hai Ba Trung")
                        .setScreens(Collections.emptyList())
                        .build()
        );

        when(mockTheaterDAO.getAllTheaters()).thenReturn(theaters);
        // Act
        List<Theater> result = mockTheaterDAO.getAllTheaters();
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetTheaterIdByName() throws SQLException {
        // Arrange
        when(mockTheaterDAO.getTheaterIDByName("MooByLip")).thenReturn(1);
        // Act
        int result = mockTheaterDAO.getTheaterIDByName("MooByLip");
        // Assert
        assertEquals(1, result);
    }

    @Test
    void testGetTheaterById() throws SQLException {
        // Arrange
        Theater theater = new Theater.Builder()
                .setID(1)
                .setName("ABC Group")
                .setAddress("64 Wall Street")
                .setScreens(Collections.emptyList())
                .build();

        when(mockTheaterDAO.getTheaterById(1)).thenReturn(theater);
        // Act
        Theater result = mockTheaterDAO.getTheaterById(1);
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("ABC Group", result.getName());
    }
}
