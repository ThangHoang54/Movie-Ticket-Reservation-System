package org.example.finalexam.dao;

import org.example.finalexam.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public interface UserDAO {
    void addUser(User user) throws SQLException;
    void updateUser(User user) throws SQLException;
    List<User> getAllUsers() throws SQLException;
    User getUserById(int id) throws SQLException;
    int getUserIDByName(String name) throws SQLException;
}
