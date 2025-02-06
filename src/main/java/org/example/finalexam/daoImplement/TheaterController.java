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
                String name = rs.getString("name");
                String address = rs.getString("address");

                List<Screen> screens = theaterService.getScreenByTheaterId(theaterID);

                return new Theater.Builder()
                        .setID(theaterID)
                        .setName(name)
                        .setAddress(address)
                        .setScreens(screens)
                        .build();
            }
        }
        return null;
    }

    @Override
    public List<Theater> getAllTheaters() throws SQLException {
        List<Theater> theaters = new ArrayList<>();
        String query = "SELECT T.id, T.name, T.address FROM Theater T";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()){
            // First, collect all theater IDs
            while (rs.next()) {
                int theaterID = rs.getInt("id");
                String name = rs.getString("name");
                String address = rs.getString("address");

                // Create a temporary Theater object without screen
                Theater newTheater = new Theater.Builder()
                        .setID(theaterID)
                        .setName(name)
                        .setAddress(address)
                        .build();
                theaters.add(newTheater);
            }
            // Now fetch screens for all theaters
            for (Theater theater : theaters) {
                List<Screen> screens = theaterService.getScreenByTheaterId(theater.getId());
                theater.setScreens(screens); // then set screens to each theater
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
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
}
