package org.example.finalexam.dao;

import org.example.finalexam.model.Theater;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public interface TheaterDAO {
    void addTheater(Theater theater) throws SQLException;
    void updateTheater(Theater theater) throws SQLException;
    void deleteTheater(int theaterID) throws SQLException;
    Theater getTheaterById(int theaterID) throws SQLException;
    List<Theater> getAllTheaters() throws SQLException;
    int getTheaterIDByName(String theaterName) throws SQLException;
}
