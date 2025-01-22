package org.example.finalexam.dao;

import org.example.finalexam.model.Booking;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public interface BookingDAO {
    void addBooking(Booking booking) throws SQLException;
    void updateBooking(Booking booking) throws SQLException;
    void deleteBooking(int bookingID) throws SQLException;
    List<Booking> getAllBookings() throws SQLException;
    Booking getBookingById(int id) throws SQLException;
}
