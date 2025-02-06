package org.example.finalexam.service;

import org.example.finalexam.model.Booking;
import org.example.finalexam.model.Screen;
import org.example.finalexam.model.User;
import org.example.finalexam.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Hoang Minh Thang - s3999925
 */

public class UserService {

    public List<Booking> getBookingsByUserId(int userId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String query = """
                SELECT B.id, B.reserved_seat, B.booking_date, S.id, S.timing, S.movie_name, S.seat_available, U.id, U.name, U.contact_info, U.booking_history
                FROM Booking B
                JOIN Screen S ON B.screen_id = S.id
                JOIN User_ U ON B.user_id = U.id
                JOIN User_Booking UB ON B.id = UB.booking_id
                JOIN User_ U1 ON U1.id = UB.user_id
                WHERE U1.id = ?;
                """;
        try (Connection conn = DatabaseConnection.getConnection()){
             PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(buildBooking(rs));
                }
            }
        }
        return bookings;
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
                .setPrice(rs.getDouble("price"))
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
