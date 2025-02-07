package org.example.finalexam.daoImplement;

import org.example.finalexam.dao.ScreenDAO;
import org.example.finalexam.model.Screen;
import org.example.finalexam.model.Theater;
import org.example.finalexam.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class ScreenController implements ScreenDAO {
    @Override
    public void addScreen(Screen screen) throws SQLException {
        String insertScreen = """
                INSERT INTO Screen (timimg, movie_name, price, seat_available, theater_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstScreen = conn.prepareStatement(insertScreen, Statement.RETURN_GENERATED_KEYS)) {

                pstScreen.setInt(1, screen.getTiming());
                pstScreen.setString(2, screen.getMovie_name());
                pstScreen.setDouble(3, screen.getPrice());
                pstScreen.setInt(4, screen.getSeat_available());
                pstScreen.setInt(5, screen.getTheater().getId());
                pstScreen.executeUpdate();

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
    public Screen getScreenById(int id) throws SQLException {
        String query = """
                SELECT S.id, S.timing, S.movie_name, S.price S.seat_available, T.id, T.name, T.address
                FROM Screen S
                JOIN Theater T ON S.theater_id = T.id 
                WHERE S.id = ?;
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    return buildScreen(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void updateScreen(Screen screen) throws SQLException {
        String query = """
                UPDATE Screen
                SET timing = ?, movie_name = ?, price = ?, seat_available = ?, theater_id = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, screen.getTiming());
            pst.setString(2, screen.getMovie_name());
            pst.setDouble(3, screen.getPrice());
            pst.setInt(4, screen.getSeat_available());
            pst.setInt(5, screen.getTheater().getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void deleteScreen(int id) throws SQLException {
        String query = "DELETE FROM Screen S WHERE S.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    @Override
    public List<Screen> getAllScreens() throws SQLException {
        List<Screen> screens = new ArrayList<>();
        String query = """
                SELECT S.id, S.timing, S.movie_name, S.price, S.seat_available, T.id, T.name, T.address
                FROM Screen S
                JOIN Theater T ON S.theater_id = T.id;
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);){
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    screens.add(buildScreen(rs));
                }
            }
        }
        return screens;
    }

    @Override
    public int getScreenIDByScreenName(String screenName) throws SQLException {
        String query = "SELECT S.id FROM Screen S WHERE S.movie_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);){
            ps.setString(1, screenName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1;
    }

    @Override
    public int getScreenTotalSeatByMovieName(String screenName) throws SQLException {
        String query = """
                SELECT S.seat_available
                FROM Screen S 
                WHERE S.movie_name = ?;
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, screenName);
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                   return rs.getInt("seat_available");
                }
            }
        }
        return -1;
    }

    private Screen buildScreen(ResultSet rs) throws SQLException {
        Screen.Builder builder = new Screen.Builder();
        Theater theater = buildTheater(rs);

        builder.setId(rs.getInt("id"))
                .setMovieName(rs.getString("movie_name"))
                .setPrice(rs.getDouble("price"))
                .setSeatAvailable(rs.getInt("seat_available"))
                .setTiming(rs.getInt("timing"))
                .setTheater(theater)
                .build();

        return builder.build();
    }

    private Theater buildTheater(ResultSet rs) throws SQLException {
        Theater.Builder builder = new Theater.Builder();

        builder.setID(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setAddress(rs.getString("address"))
                .build();

        return builder.build();
    }
}
