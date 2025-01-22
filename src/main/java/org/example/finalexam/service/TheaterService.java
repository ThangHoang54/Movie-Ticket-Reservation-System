package org.example.finalexam.service;

import org.example.finalexam.model.Screen;
import org.example.finalexam.model.Theater;
import org.example.finalexam.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public class TheaterService {

    public List<Screen> getScreenByTheaterId(int Id) throws SQLException {
        List<Screen> screens = new ArrayList<>();
        String query = """
                SELECT S.id, S.timing, S.movie_name, S.seat_available, T.id, T.name, T.address
                FROM Screen S
                JOIN Theater T ON S.theater_id = T.id
                JOIN theater_screen TS ON S.id = TS.screen_id
                JOIN Theater T1 ON T1.id = TS.theater_id
                WHERE T1.id = ?;
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);){
          stmt.setInt(1, Id);
          try (ResultSet rs = stmt.executeQuery()){
              while (rs.next()) {
                  screens.add(buildScreen(rs));
              }
          }
        }
        return screens;
    }

    private Screen buildScreen(ResultSet rs) throws SQLException {
        Screen.Builder builder = new Screen.Builder();
        Theater theater = buildTheater(rs);

        builder.setId(rs.getInt("id"))
                .setMovieName(rs.getString("movie_name"))
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
