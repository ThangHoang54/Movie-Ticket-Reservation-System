package org.example.finalexam.daoImplement;

import org.example.finalexam.dao.UserDAO;
import org.example.finalexam.model.Booking;
import org.example.finalexam.model.User;
import org.example.finalexam.service.UserService;
import org.example.finalexam.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Hoang Minh Thang - s3999925
 */

public class UserController implements UserDAO {

    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }
    @Override
    public void addUser(User user) throws SQLException {
        String insertUser = """
                INSERT INTO User_ (name, contact_info, booking_history)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstUser = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {

                pstUser.setString(1, user.getName());
                pstUser.setString(2, user.getContact_info());
                pstUser.setString(3, user.getBooking_history());
                pstUser.executeUpdate();

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
    public void updateUser(User user) throws SQLException {
        String query = """
                UPDATE User_
                SET name = ?, contact_info = ?, booking_history = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, user.getName());
            pst.setString(2, user.getContact_info());
            pst.setString(3, user.getBooking_history());
            pst.setInt(4, user.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT U.id, U.name, U.contact_info, U.booking_history FROM User_ U";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                users.add(buildUser(rs));
            }
        }
        return users;
    }

    @Override
    public User getUserById(int id) throws SQLException {
        String query = "SELECT U.id, U.name, U.contact_info, U.booking_history FROM User_ U WHERE U.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return buildUser(rs);
            }
        }
        return null;
    }

    @Override
    public int getUserIDByName(String name) throws SQLException {
        String query = "SELECT U.id FROM User_ U WHERE U.name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);){
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    private User buildUser(ResultSet rs) throws SQLException {
        User.Builder builder = new User.Builder();

        int id = rs.getInt("id");
        List<Booking> bookings = userService.getBookingsByUserId(id);

        builder.setID(id)
                .setName(rs.getString("name"))
                .setBookingHistory(rs.getString("booking_history"))
                .setContactInfo(rs.getString("contact_info"))
                .setBookings(bookings)
                .build();

        return builder.build();
    }
}
