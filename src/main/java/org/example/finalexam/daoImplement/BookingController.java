package org.example.finalexam.daoImplement;

import org.example.finalexam.dao.BookingDAO;
import org.example.finalexam.model.Booking;
import org.example.finalexam.model.Screen;
import org.example.finalexam.model.User;
import org.example.finalexam.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class BookingController implements BookingDAO {
    @Override
    public void addBooking(Booking booking) throws SQLException {
        String insertBooking = """
                INSERT INTO Booking (reserved_seat, booking_date, screen_id, user_id)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstBooking = conn.prepareStatement(insertBooking, Statement.RETURN_GENERATED_KEYS)) {

                pstBooking.setInt(1, booking.getReserved_seat());
                pstBooking.setDate(2, new java.sql.Date(booking.getBooking_date().getTime()));
                pstBooking.setInt(3, booking.getScreen().getId());
                pstBooking.setInt(4, booking.getUser().getId());
                pstBooking.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public void updateBooking(Booking booking) throws SQLException {
        String query = """
                UPDATE Booking
                SET reserved_seat = ?, booking_date = ?, screen_id = ?, theater_id = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, booking.getReserved_seat());
            pst.setDate(2, new java.sql.Date(booking.getBooking_date().getTime()));
            pst.setInt(3, booking.getScreen().getId());
            pst.setInt(4, booking.getUser().getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void deleteBooking(int bookingID) throws SQLException {
        String query = "DELETE FROM Booking B WHERE B.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, bookingID);
            pst.executeUpdate();
        }
    }

    @Override
    public List<Booking> getAllBookings() throws SQLException {
        String query = """
                SELECT B.id, B.reserved_seat, B.booking_date, S.id, S.timing, S.movie_name, S.seat_available, U.id, U.name, U.contact_info, U.booking_history
                FROM Booking B
                JOIN Screen S ON B.screen_id = S.id
                JOIN User_ U ON B.user_id = U.id;
                """;
        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                bookings.add(buildBooking(rs));
            }
        }
        return bookings;
    }

    @Override
    public List<Integer> getBookingAlreadyBookSeatByScreenID(int screenID) throws SQLException {
        String query = """
                SELECT B.reserved_seat
                FROM Booking B
                WHERE B.screen_id = ?
                """;
        List<Integer> bookingSeats = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)){
            pst.setInt(1, screenID);

             try(ResultSet rs = pst.executeQuery()) {
                 while (rs.next()) {
                     bookingSeats.add(rs.getInt("reserved_seat"));
                 }
             }
        }
        return bookingSeats;
    }

    @Override
    public Booking getBookingById(int id) throws SQLException {
        String query = """
                SELECT B.id, B.reserved_seat, B.booking_date, S.id, S.timing, S.movie_name, S.seat_available, U.id, U.name, U.contact_info, U.booking_history
                FROM Booking B
                JOIN Screen S ON B.screen_id = S.id
                JOIN User_ U ON B.user_id = U.id
                WHERE B.id = ?;
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
             pst.setInt(1, id);
             ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                return buildBooking(rs);
            }
        }
        return null;
    }

    private User buildUser(ResultSet rs) throws SQLException {
        User.Builder builder = new User.Builder();

        builder.setID(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setContactInfo(rs.getString("contact_info"))
                .setBookingHistory(rs.getString("booking_history"))
                .build();

        return builder.build();
    }

    private Screen buildScreen(ResultSet rs) throws SQLException {
        Screen.Builder builder = new Screen.Builder();

        builder.setId(rs.getInt("id"))
                .setTiming(rs.getInt("timing"))
                .setMovieName(rs.getString("movie_name"))
                .setSeatAvailable(rs.getInt("seat_available"))
                .build();

        return builder.build();
    }

    private Booking buildBooking(ResultSet rs) throws SQLException {
        Booking.Builder builder = new Booking.Builder();
        User user = buildUser(rs);
        Screen screen = buildScreen(rs);

        builder.setID(rs.getInt("id"))
                .setReserved_seat(rs.getInt("reserved_seat"))
                .setBooking_date(rs.getDate("booking_date"))
                .setUser(user)
                .setScreen(screen)
                .build();

        return builder.build();
    }
}
