package org.example.finalexam.daoImplement;

import org.example.finalexam.dao.UserInfoDAO;
import org.example.finalexam.model.Booking;
import org.example.finalexam.model.User;
import org.example.finalexam.service.UserService;
import org.example.finalexam.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class UserInfoController implements UserInfoDAO {
    private final UserService userService;

    public UserInfoController() {
        this.userService = new UserService();
    }
    @Override
    public User getUserByFullNameAndEmail(String fullName, String email) throws SQLException {
        String query = "SELECT U.id, U.name, U.contact_info, U.booking_history FROM User_ U WHERE U.contact_info = ?";
        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            //ps.setString(1, fullName);
            ps.setString(1, email);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String booking_history = rs.getString("booking_history");
                    String contact_info = rs.getString("contact_info");
                    List<Booking> bookings = userService.getBookingsByUserId(id);
                    return new User.Builder()
                            .setID(id)
                            .setName(name)
                            .setBookingHistory(booking_history)
                            .setContactInfo(contact_info)
                            .setBookings(bookings)
                            .build();
                }
            }
        }
        return null;
    }
}
