package org.example.finalexam.dao;

import org.example.finalexam.model.User;
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

public class UserControllerTest {
    @Mock
    private UserDAO mockUserDAO;
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
    void testAddUser() throws SQLException {
        // Arrange
        User newUser = new User.Builder()
                .setID(1)
                .setName("Pie Trump")
                .setContactInfo("trumpJ@gmail.com")
                .setBookingHistory("bookingN")
                .setBookings(Collections.emptyList())
                .build();

        doNothing().when(mockUserDAO).addUser(newUser);
        // Act
        mockUserDAO.addUser(newUser);
        // Assert
        verify(mockUserDAO, times(1)).addUser(newUser);
    }

    @Test
    void testUpdateUser() throws SQLException {
        // Arrange
        User updatedUser = new User.Builder()
                .setID(1)
                .setName("Pie Trump Jose")
                .setContactInfo("trumpJ@gmail.com")
                .setBookingHistory("bookingN")
                .setBookings(Collections.emptyList())
                .build();

        doNothing().when(mockUserDAO).updateUser(updatedUser);
        // Act
        mockUserDAO.updateUser(updatedUser);
        // Assert
        verify(mockUserDAO, times(1)).updateUser(updatedUser);
    }

    @Test
    void testGetAllUser() throws SQLException {
        // Arrange
        List<User> users = Arrays.asList(
                new User.Builder()
                        .setID(1)
                        .setName("Ngoc Phuc Bui")
                        .setContactInfo("phucbui@example.com")
                        .setBookingHistory("booking N")
                        .build(),
                new User.Builder()
                        .setID(2)
                        .setName("TranXuanPhuc")
                        .setContactInfo("phucxuan@example.com")
                        .setBookingHistory("booking-N")
                        .build()
        );

        when(mockUserDAO.getAllUsers()).thenReturn(users);
        // Act
        List<User> result = mockUserDAO.getAllUsers();
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetUserIdByName() throws SQLException {
        // Arrange
        when(mockUserDAO.getUserIDByName("Tran Xuan Phuc")).thenReturn(2);
        // Act
        int result = mockUserDAO.getUserIDByName("Tran Xuan Phuc");
        // Assert
        assertEquals(2, result);
    }

    @Test
    void testGetUserById() throws SQLException {
        // Arrange
        User user = new User.Builder()
                .setID(1)
                .setName("Johnney Dang")
                .setContactInfo("johnney@example.com")
                .setBookingHistory("booking--N")
                .build();

        when(mockUserDAO.getUserById(1)).thenReturn(user);
        // Act
        User result = mockUserDAO.getUserById(1);
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Johnney Dang", result.getName());
    }
}
