package org.example.finalexam.dao;

import org.example.finalexam.model.Booking;
import org.example.finalexam.model.Screen;
import org.example.finalexam.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class BookingControllerTest {
    @Mock
    private BookingDAO mockBookingDAO;
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
    void testAddBooking() throws SQLException {
        // Arrange
        User newUser = new User.Builder()
                .setName("Wang Lee")
                .setContactInfo("Lee2367@gmail.com")
                .setBookingHistory("----")
                .setBookings(Collections.emptyList())
                .build();
        Screen newScreen = new Screen.Builder()
                .setId(1)
                .setTiming(120)
                .setMovieName("Lego Movie")
                .setSeatAvailable(100)
                .build();

        Booking newBooking = new Booking.Builder()
                .setID(1)
                .setReserved_seat(65)
                .setUser(newUser)
                .setScreen(newScreen)
                .build();

        doNothing().when(mockBookingDAO).addBooking(newBooking);
        // Act
        mockBookingDAO.addBooking(newBooking);
        // Assert
        verify(mockBookingDAO, times(1)).addBooking(newBooking);
    }

    @Test
    void testUpdateBooking() throws SQLException {
        // Arrange
        User updatedUser = new User.Builder()
                .setName("Wang Lee")
                .setContactInfo("Lee2367@gmail.com")
                .setBookingHistory("----")
                .setBookings(Collections.emptyList())
                .build();
        Screen updatedScreen = new Screen.Builder()
                .setId(1)
                .setTiming(120)
                .setMovieName("Lego Movie")
                .setSeatAvailable(100)
                .build();

        Booking updatedBooking = new Booking.Builder()
                .setID(1)
                .setReserved_seat(65)
                .setUser(updatedUser)
                .setScreen(updatedScreen)
                .build();

        doNothing().when(mockBookingDAO).updateBooking(updatedBooking);
        // Act
        mockBookingDAO.updateBooking(updatedBooking);
        // Assert
        verify(mockBookingDAO, times(1)).updateBooking(updatedBooking);
    }

    @Test
    void testDeleteBooking() throws SQLException {
        // Arrange
        doNothing().when(mockBookingDAO).deleteBooking(1);
        // Act
        mockBookingDAO.deleteBooking(1);
        // Assert
        verify(mockBookingDAO, times(1)).deleteBooking(1);
    }

    @Test
    void testGetAllBookings() throws SQLException {
        // Arrange
        // Arrange
        User newUser = new User.Builder()
                .setName("Wang Lee")
                .setContactInfo("Lee2367@gmail.com")
                .setBookingHistory("----")
                .setBookings(Collections.emptyList())
                .build();
        Screen newScreen = new Screen.Builder()
                .setId(1)
                .setTiming(120)
                .setMovieName("Lego Movie")
                .setSeatAvailable(100)
                .build();

        List<Booking> bookings = Arrays.asList(
                new Booking.Builder()
                        .setID(1)
                        .setReserved_seat(2)
                        .setUser(newUser)
                        .setScreen(newScreen)
                        .build(),
                new Booking.Builder()
                        .setID(2)
                        .setReserved_seat(36)
                        .setUser(newUser)
                        .setScreen(newScreen)
                        .build()
        );

        when(mockBookingDAO.getAllBookings()).thenReturn(bookings);
        // Act
        List<Booking> result = mockBookingDAO.getAllBookings();
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetBookingById() throws SQLException {
        // Arrange
        User user = new User.Builder()
                .setName("Wang Lee")
                .setContactInfo("Lee2367@gmail.com")
                .setBookingHistory("----")
                .setBookings(Collections.emptyList())
                .build();
        Screen screen = new Screen.Builder()
                .setId(1)
                .setTiming(120)
                .setMovieName("Lego Movie")
                .setSeatAvailable(100)
                .build();

        Booking booking = new Booking.Builder()
                .setID(1)
                .setReserved_seat(65)
                .setUser(user)
                .setScreen(screen)
                .build();

        when(mockBookingDAO.getBookingById(1)).thenReturn(booking);
        // Act
        Booking result = mockBookingDAO.getBookingById(1);
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Lego Movie", result.getScreen().getMovie_name());
    }
}
