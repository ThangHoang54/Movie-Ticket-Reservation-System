package org.example.finalexam.daoImplement;

import org.example.finalexam.dao.TheaterDAO;
import org.example.finalexam.model.Screen;
import org.example.finalexam.model.Theater;
import org.example.finalexam.service.TheaterService;
import org.example.finalexam.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Hoang Minh Thang - s3999925
 */

public class TheaterController implements TheaterDAO {
    private final TheaterService theaterService;

    public TheaterController() {
        this.theaterService = new TheaterService();
    }

    @Override
    public void addTheater(Theater theater) throws SQLException {
        String insertTheater = """
                INSERT INTO Theater (name, address)
                VALUES (?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstTheater = conn.prepareStatement(insertTheater, Statement.RETURN_GENERATED_KEYS)) {

                pstTheater.setString(1, theater.getName());
                pstTheater.setString(2, theater.getAddress());
                pstTheater.executeUpdate();

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
    public void updateTheater(Theater theater) throws SQLException {
        String query = """
                UPDATE Theater
                SET name = ?, address = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, theater.getName());
            pst.setString(2, theater.getAddress());
            pst.executeUpdate();
        }
    }

    @Override
    public void deleteTheater(int theaterID) throws SQLException {
        String query = "DELETE FROM Theater T WHERE T.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, theaterID);
            pst.executeUpdate();
        }
    }

    @Override
    public Theater getTheaterById(int theaterID) throws SQLException {
        String query = "SELECT * FROM Theater T WHERE T.id = ?";

        try (Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, theaterID);
            ResultSet rs = ps.executeQuery(query);

            while (rs.next()) {
                return buildTheater(rs);
            }
        }
        return null;
    }

    @Override
    public List<Theater> getAllTheaters() throws SQLException {
        List<Theater> theaters = new ArrayList<>();
        String query = "SELECT * FROM Theater";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()){
            while (rs.next()) {
                theaters.add(buildTheater(rs));
            }
        }
        return theaters;
    }

    @Override
    public int getTheaterIDByName(String theaterName) throws SQLException {
        String query = "SELECT id FROM Theater WHERE Theater.name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);){
            ps.setString(1, theaterName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    private Theater buildTheater(ResultSet rs) throws SQLException {
        Theater.Builder builder = new Theater.Builder();

        List<Screen> screens = theaterService.getScreenByTheaterId(rs.getInt("id"));
        builder.setID(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setAddress(rs.getString("address"))
                .setScreens(screens)
                .build();

        return builder.build();
    }
}
